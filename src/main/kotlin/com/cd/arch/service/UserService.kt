package com.cd.arch.service

import com.cd.arch.domain.User
import com.cd.arch.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(private val userRepository: UserRepository) {

    fun getUserById(id: Long): User? {
        return userRepository.findById(id).orElse(null)
    }

    @Transactional
    fun updateUserField(id: Long, fieldName: String, fieldValue: String): User {
        val user = userRepository.findById(id).orElseThrow {Exception("User not found")}
        when (fieldName) {
            "emergencyContact" -> user.emergencyContact = fieldValue
            "birthdate" -> user.birthdate = fieldValue
            "educationLevel" -> user.educationLevel = fieldValue
            "interests" -> user.interests = fieldValue
            "additionalInfo" -> user.additionalInfo = fieldValue
            else -> throw Exception("Invalid field name")
        }
        return userRepository.save(user)
    }
}