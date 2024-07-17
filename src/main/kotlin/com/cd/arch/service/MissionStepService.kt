package com.cd.arch.service

import com.cd.arch.repository.MissionRepository
import com.cd.arch.repository.MissionStepRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class MissionStepService(
    private val missionRepository: MissionRepository,
    private val missionStepRepository: MissionStepRepository
) {
    @Transactional
    fun completeMissionStep(missionId: Long, stepNumber: Int) {
        val missionStep = missionStepRepository.findByMissionIdAndStepNumber(missionId, stepNumber)
            ?: throw IllegalArgumentException("Mission Step [$missionId] does not exist")

        missionStep.isComplete = true
        missionStep.completedAt = LocalDateTime.now()

        missionStepRepository.save(missionStep)
    }
}