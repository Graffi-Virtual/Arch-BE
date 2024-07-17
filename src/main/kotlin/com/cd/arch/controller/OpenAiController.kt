package com.cd.arch.controller

import com.cd.arch.service.OpenAiService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class OpenAiController(
    private val openAiService: OpenAiService
) {
    @GetMapping("/openai")
    fun getOpenAiResponse(@RequestParam prompt: String, @RequestParam userId: Long): Mono<String> {
        return openAiService.getCompletion(prompt, userId)
    }
}