package com.social.api.presentation.controller

import com.social.api.USER
import com.social.api.domain.model.User
import com.social.api.domain.repository.UserRepository
import com.social.api.presentation.exception.ResourceNotFoundException
import com.social.api.presentation.security.CurrentUser
import com.social.api.presentation.security.UserPrincipal
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController {

    @Autowired
    lateinit var userRepository: UserRepository

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('$USER')")
    fun getCurrentUser(@CurrentUser userPrincipal: UserPrincipal): User {
        return userRepository.findById(userPrincipal.id!!)
                .orElseThrow { ResourceNotFoundException("User", "id", userPrincipal.id) }
    }
}
