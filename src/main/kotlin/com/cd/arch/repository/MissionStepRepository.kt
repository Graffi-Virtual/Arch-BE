package com.cd.arch.repository

import com.cd.arch.domain.MissionStep
import org.springframework.data.jpa.repository.JpaRepository

interface MissionStepRepository : JpaRepository<MissionStep, Long> {
    fun findByMissionIdAndStepNumber(missionId: Long, stepNumber: Int): MissionStep?
}