package com.cd.arch.controller

import com.cd.arch.domain.SessionUser
import org.springframework.ui.Model
import jakarta.servlet.http.HttpSession
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class HtmlController(
    private val httpSession: HttpSession
) {
    @GetMapping("/")
    fun home(model: Model): String {
        model.addAttribute("hi", "hi")
        val user = httpSession.getAttribute("user") as SessionUser?

        if (user != null) {
            model.addAttribute("user", user)
        }

        return "home"
    }

    @GetMapping("/home")
    fun homePage(model: Model): String {
        model.addAttribute("hi", "hi")
        val user = httpSession.getAttribute("user") as SessionUser?

        if (user != null) {
            model.addAttribute("user", user)
        }

        return "home"
    }

    @GetMapping("/login")
    fun login(): String {
        return "login"
    }
}