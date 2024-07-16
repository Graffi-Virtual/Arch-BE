package com.cd.arch.config

import com.cd.arch.domain.User

data class OAuthAttributes(
    val attributes: Map<String, Any>,
    val nameAttributeKey: String,
    val name: String,
    val email: String,
    val picture: String,
    val emergencyContact: String?,
    val birthdate: String?,
    val educationLevel: String?,
    val interests: String?,
    val additionalInfo: String?
) {
    companion object {
        fun of(userNameAttributeName: String, attributes: Map<String, Any>): OAuthAttributes {
            return OAuthAttributes(
                name = attributes["name"] as String,
                email = attributes["email"] as String,
                picture = attributes["picture"] as String,
                emergencyContact = attributes["emergency_contact"] as String?,
                birthdate = attributes["birthdate"] as String?,
                educationLevel = attributes["education_level"] as String?,
                interests = attributes["interests"] as String?,
                additionalInfo = attributes["additional_info"] as String?,
                attributes = attributes,
                nameAttributeKey = userNameAttributeName
            )
        }
    }

    fun toEntity(): User {
        return User(
            name = name,
            email = email,
            picture = picture,
            emergencyContact = emergencyContact ?: "",
            birthdate = birthdate ?: "",
            educationLevel = educationLevel ?: "",
            interests = interests ?: "",
            additionalInfo = additionalInfo ?: ""
        )
    }
}
