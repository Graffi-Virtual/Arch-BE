package com.cd.arch.config

import com.cd.arch.service.CustomOAuth2UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val customOAuth2UserService: CustomOAuth2UserService
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { csrf -> csrf.disable() }
            .headers { headers -> headers.frameOptions { frameOptions -> frameOptions.disable() } }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers( "/", "/home", "/css/**", "/js/**", "/images/**", "/webjars/**", "/login").permitAll()
                    .anyRequest().authenticated()
            }
            .oauth2Login { oauth2Login ->
                oauth2Login
                    .loginPage("/login")
                    .defaultSuccessUrl("/home", true)
                    .failureUrl("/login?error=true")
                    .userInfoEndpoint { userInfoEndpoint ->
                        userInfoEndpoint.userService(customOAuth2UserService)
                    }
            }
            .logout { logout ->
                logout
                    .logoutSuccessUrl("/")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
            }

        return http.build()
    }
}
