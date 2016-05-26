package org.kug.spring

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.DeserializationFeature
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.kotlin.getForObject
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate

@SpringBootApplication
open class Application {
    @Bean
    open fun restTemplate() = RestTemplate()

    @Bean
    open fun objectMapperBuilder() =
            Jackson2ObjectMapperBuilder()
                    .featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
}

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

data class Title(@JsonProperty("Title") val title: String, @JsonProperty("Year") val year: String)
data class SearchResults(@JsonProperty("Search") val titles: List<Title>)

@RestController
@RequestMapping("/api/titles")
class TitleController(val restTemplate: RestTemplate) {
    @RequestMapping
    fun searchTitles(@RequestParam q: String) =
        restTemplate.getForObject<SearchResults>("http://www.omdbapi.com/?s=$q")
            .titles
}