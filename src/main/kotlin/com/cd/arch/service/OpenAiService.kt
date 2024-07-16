package com.cd.arch.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

data class OpenAIRequest(
    val model: String,
    val prompt: String,
    val max_tokens: Int
)

data class OpenAiResponse(
    val choices: List<Choice>
)

data class Choice(
    val text: String
)

@Service
class OpenAiService(
    @Value("\${openai.api.key}") private val apiKey: String
) {
    private val webClient = WebClient.builder()
        .baseUrl("https://api.openai.com/v1")
        .defaultHeader("Authorization", "Bearer $apiKey")
        .build()

    fun getCompletion(prompt: String): Mono<String> {
        val request = OpenAIRequest(
            model = "gpt-4",
            prompt = prompt,
            max_tokens = 100
        )

        return webClient.post()
            .uri("/completions")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .retrieve()
            .bodyToMono(OpenAiResponse::class.java)
            .map { response -> response.choices.first().text }
    }
}
