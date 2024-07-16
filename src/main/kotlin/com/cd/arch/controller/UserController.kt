package com.cd.arch.controller

import com.cd.arch.domain.User
import com.cd.arch.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService) {

    @GetMapping("/{id}")
    fun getUser(@PathVariable id: Long): ResponseEntity<User> {
        val user = userService.getUserById(id)
        return if (user != null) {
            ResponseEntity.ok(user)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PatchMapping("/{id}")
    fun updateUserField(
        @PathVariable id: Long,
        @RequestParam fieldName: String,
        @RequestParam fieldValue: String
    ): ResponseEntity<User> {
        return try {
            val updateUser = userService.updateUserField(id, fieldName, fieldValue)
            ResponseEntity.ok(updateUser)
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(null)
        }
    }
}