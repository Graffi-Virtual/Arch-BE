package com.cd.arch.controller

import com.cd.arch.repository.MissionRepository
import com.cd.arch.repository.MissionStepRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class DataCheckController(
    private val missionRepository: MissionRepository,
    private val missionStepRepository: MissionStepRepository,
    private val messageRepository: MissionRepository
) {
    @GetMapping("/missions")
    fun getMissions() = missionRepository.findAll()

    @GetMapping("/missionSteps")
    fun getMissionSteps() = missionStepRepository.findAll()

    @GetMapping("messages")
    fun getMessages() = messageRepository.findAll()
}