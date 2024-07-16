package com.cd.arch.domain

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
data class MissionStep(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne
    @JoinColumn(name = "mission_id", nullable = false)
    val mission: Mission,

    @Column(nullable = false)
    val stepNumber: Int,

    @Column(nullable = false)
    val instruction: String,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(nullable = false)
    val isComplete: Boolean = false,

    @Column(nullable = true)
    val completedAt: LocalDateTime? = null,

    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)
