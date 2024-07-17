package com.cd.arch.service

import com.cd.arch.domain.Message
import com.cd.arch.domain.Mission
import com.cd.arch.domain.MissionStep
import com.cd.arch.domain.User
import com.cd.arch.repository.MessageRepository
import com.cd.arch.repository.MissionRepository
import com.cd.arch.repository.MissionStepRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.time.LocalDateTime

data class OpenAIRequest(
    val model: String,
    val prompt: String,
    val max_tokens: Int
)

data class Choice(
    val text: String
)

data class OpenAiApiResponse(
    val choices: List<Choice>
)

data class OpenAiResponse(
    val type: String,
    val content: String,
    val mission1: String?,
    val mission2: String?,
    val mission3: String?
)

@Service
class OpenAiService(
    @Value("\${openai.api.key}") private val apiKey: String,
    private val missionRepository: MissionRepository,
    private val missionStepRepository: MissionStepRepository,
    private val messageRepository: MessageRepository
) {
    private val webClient = WebClient.builder()
        .baseUrl("https://api.openai.com/v1")
        .defaultHeader("Authorization", "Bearer $apiKey")
        .build()

    fun getCompletion(prompt: String, userId: Long): Mono<String> {
        val request = OpenAIRequest(
            model = "gpt-3.5-turbo-instruct",
            prompt = prompt,
            max_tokens = 100
        )

        return webClient.post()
            .uri("/completions")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .retrieve()
            .bodyToMono(OpenAiApiResponse::class.java)
            .map { response -> response.choices.first().text }
            .flatMap { responseText ->
                val openAiResponse = parseResponse(responseText)
                processResponse(openAiResponse, userId)
                Mono.just(responseText)
            }
    }

    private fun parseResponse(responseText: String): OpenAiResponse {
        val mapper = jacksonObjectMapper()
        return mapper.readValue(responseText)
    }

    private fun processResponse(response: OpenAiResponse, userId: Long) {
        val user = User(id = userId, email = "", name = "")

        when (response.type) {
            "Mission" -> {
                val mission = missionRepository.save(Mission(name = response.content))
                listOfNotNull(response.mission1, response.mission2, response.mission3).forEachIndexed { index, step ->
                    missionStepRepository.save(MissionStep(mission = mission, stepNumber = index + 1, instruction = step, user = user))
                }
            }
            "Chat" -> {
                messageRepository.save(Message(content = response.content, user = user, createdAt = LocalDateTime.now()))
            }
        }
    }
}
