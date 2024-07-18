package com.cd.arch.service

import com.cd.arch.domain.Message
import com.cd.arch.domain.Mission
import com.cd.arch.domain.MissionStep
import com.cd.arch.domain.User
import com.cd.arch.repository.MessageRepository
import com.cd.arch.repository.MissionRepository
import com.cd.arch.repository.MissionStepRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.time.LocalDateTime
import com.theokanning.openai.completion.chat.ChatCompletionRequest
import com.theokanning.openai.completion.chat.ChatMessage
import com.theokanning.openai.service.OpenAiService

data class AssistantResponse(
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
    private val openAiService = OpenAiService(apiKey)

    fun getCompletion(prompt: String, userId: Long): Mono<String> {
        val messages = listOf(
            ChatMessage("system", "You are a friendly AI designed to help children with borderline intellectual functioning. Always respond in one of the two specified formats based on the user's input. If the input seems like it requires a step-by-step process or instructions, respond in the 'Mission' format. Otherwise, respond in the 'Chat' format. Mission format: Type: Mission, Content: {content}, Mission1: {Mission1}, Mission2: {Mission2}, Mission3: {Mission3}. Chat format: Type: Chat, Content: {answer}, Mission1: None, Mission2: None, Mission3: None."),
            ChatMessage("user", prompt)
        )

        val request = ChatCompletionRequest.builder()
            .model("gpt-4")
            .messages(messages)
            .build()

        return Mono.fromCallable {
            openAiService.createChatCompletion(request).choices.first().message.content
        }.flatMap { responseText ->
            println("Open AI Response: $responseText")
            val assistantResponse = parseResponse(responseText)
            processResponse(assistantResponse, userId)
            Mono.just(responseText)
        }
    }

    private fun parseResponse(responseText: String): AssistantResponse {
        val mapper = jacksonObjectMapper()
        return try {
            mapper.readValue(responseText)
        } catch (e: Exception) {
            println("JSON Parsing Error: ${e.message}")
            AssistantResponse(type = "Chat", content = responseText, mission1 = null, mission2 = null, mission3 = null)
        }
    }

    private fun processResponse(response: AssistantResponse, userId: Long) {
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
