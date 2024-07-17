package com.cd.arch.controller

import com.cd.arch.service.MissionStepService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class MissionStepController(
    private val missionStepService: MissionStepService
) {
    @PatchMapping("/missions/{missionId}/steps/{stepNumber}/complete")
    fun completeMissionStep(@PathVariable missionId: Long, @PathVariable stepNumber: Int): ResponseEntity<Void> {
        missionStepService.completeMissionStep(missionId, stepNumber)
        return ResponseEntity.noContent().build()
    }
}