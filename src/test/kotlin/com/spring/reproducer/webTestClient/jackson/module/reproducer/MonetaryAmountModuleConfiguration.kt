package com.spring.reproducer.webTestClient.jackson.module.reproducer

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.TreeNode
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.node.TextNode
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.javamoney.moneta.Money
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.*
import javax.money.MonetaryAmount

@Configuration
class MonetaryAmountModuleConfiguration {

    @Bean
    fun kotlinModule() = KotlinModule()

    @Bean
    fun monetaryAmountSerializer(): Module = SimpleModule().addSerializer(MonetaryAmount::class.java, MonetaryAmountSerializer())

    @Bean
    fun monetaryAmountDeserializer(): Module = SimpleModule().addDeserializer(MonetaryAmount::class.java, MonetaryAmountDeserializer())
}

class MonetaryAmountSerializer : StdSerializer<MonetaryAmount>(MonetaryAmount::class.java) {

    override fun serialize(amount: MonetaryAmount, gen: JsonGenerator, provider: SerializerProvider) {
        NumberFormat.getNumberInstance(Locale.ENGLISH).also {
            it.maximumFractionDigits = amount.currency.defaultFractionDigits
            it.minimumFractionDigits = amount.currency.defaultFractionDigits
            it.roundingMode = RoundingMode.FLOOR
            gen.run {
                writeStartObject()
                writeFieldName("amount")
                writeString(amount.format(it))
                writeFieldName("currency")
                writeString(amount.currency.currencyCode)
                writeEndObject()
            }
        }
    }
}

class MonetaryAmountDeserializer : StdDeserializer<MonetaryAmount>(MonetaryAmount::class.java) {

    override fun deserialize(parser: JsonParser, ctxt: DeserializationContext) =
            parser.codec.readTree<TreeNode>(parser).let { Money.of(it.amount(), it.currency()) }

    private fun TreeNode.amount() = key<TextNode>("amount").toString().trim('\"').toBigDecimal()

    private fun TreeNode.currency() = key<TextNode>("currency").asText()

    private inline fun <reified T> TreeNode.key(key: String) = get(key) as? T ?: throw IllegalArgumentException("oops")
}

fun MonetaryAmount.format(format: NumberFormat) = format.format(number).filterNot { it == ',' }
