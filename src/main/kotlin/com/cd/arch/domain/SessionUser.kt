package com.cd.arch.domain

import java.io.Serializable

data class SessionUser(
    val name: String,
    val email: String,
    val picture: String
) : Serializable
