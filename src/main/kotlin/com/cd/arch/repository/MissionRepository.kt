package com.cd.arch.repository

import com.cd.arch.domain.Mission
import org.springframework.data.jpa.repository.JpaRepository

interface MissionRepository : JpaRepository<Mission, Long>