package com.spring.reproducer.webTestClient.jackson.module.reproducer

import com.fasterxml.jackson.databind.ObjectMapper
import org.javamoney.moneta.Money
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import java.math.BigDecimal

@SpringBootTest
@AutoConfigureWebTestClient
internal class UserControllerTest(@Autowired private val webTestClient: WebTestClient, @Autowired private val objecMapper: ObjectMapper) {

    @Test
    fun `find should find same user`() {
        val newUser = User(Money.of(BigDecimal.TEN, "EUR"))
        webTestClient.post()
                .uri("/users")
                .syncBody(newUser)
                .exchange()
                .expectBody<User>().isEqualTo(newUser)
    }

}