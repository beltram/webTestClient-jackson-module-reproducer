package com.spring.reproducer.webTestClient.jackson.module.reproducer

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.toMono

@RestController
@RequestMapping("/users")
class UserController {

    @PostMapping
    fun find(@RequestBody user: User) = user.toMono()
}