package org.kug.spring

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
open class Application

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}

data class Quote(val person: String, val text: String)

@RestController
@RequestMapping("/api/quotes")
class QuoteController {
    val quotes = mutableSetOf(
            Quote("Frank Underwood", "Money is the Mc-mansion in Sarasota that starts falling apart after 10 years."),
            Quote("Frank Underwood", "Power is a lot like real estate. It’s all about location, location, location."))

    @RequestMapping
    fun allQuotes() = quotes

    @RequestMapping(method = arrayOf(RequestMethod.POST))
    fun addQuote(@RequestBody quote: Quote) = quotes.add(quote)
}