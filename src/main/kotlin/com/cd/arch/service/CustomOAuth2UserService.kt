package com.cd.arch.service

import com.cd.arch.config.OAuthAttributes
import com.cd.arch.domain.SessionUser
import com.cd.arch.domain.User
import com.cd.arch.repository.UserRepository
import jakarta.servlet.http.HttpSession
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class CustomOAuth2UserService(
    private val userRepository: UserRepository,
    private val httpSession: HttpSession,
) : OAuth2UserService<OAuth2UserRequest, OAuth2User>{
    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val delegate = DefaultOAuth2UserService()
        val oAuth2User = delegate.loadUser(userRequest)

        val userNameAttributeName = userRequest.clientRegistration.providerDetails.userInfoEndpoint.userNameAttributeName

        val attributes = OAuthAttributes.of(userNameAttributeName, oAuth2User.attributes)

        val user = saveOrUpdate(attributes)

        httpSession.setAttribute("user", SessionUser(user.name, user.email, user.picture))

        return DefaultOAuth2User(setOf(), attributes.attributes, attributes.nameAttributeKey)
    }

    private fun saveOrUpdate(attributes: OAuthAttributes): User {
        val user = userRepository.findByEmail(attributes.email)
            ?.copy(
                name = attributes.name,
                picture = attributes.picture,
                emergencyContact = attributes.emergencyContact ?: "",
                birthdate = attributes.birthdate ?: "",
                educationLevel = attributes.educationLevel ?: "",
                interests = attributes.interests ?: "",
                additionalInfo = attributes.additionalInfo ?: ""
            )
            ?: attributes.toEntity()

        return userRepository.save(user)
    }
}