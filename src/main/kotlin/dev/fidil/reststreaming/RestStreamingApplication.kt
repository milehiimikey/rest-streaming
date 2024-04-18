package dev.fidil.reststreaming

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RestStreamingApplication

fun main(args: Array<String>) {
    runApplication<RestStreamingApplication>(*args)
}
