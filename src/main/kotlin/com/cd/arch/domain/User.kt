package com.cd.arch.domain

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime

@Entity
data class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(unique = true, nullable = false)
    val email: String,

    @Column(nullable = false)
    val name: String,

    @Column
    var emergencyContact: String = "",

    @Column
    var birthdate: String = "",

    @Column
    var educationLevel: String = "",

    @Column
    var interests: String = "",

    @Column
    var additionalInfo: String = "",

    @Column
    val picture: String = "",

    @CreationTimestamp
    val createdAt: LocalDateTime = LocalDateTime.now(),
)
