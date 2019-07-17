package com.social.api.presentation.controller

import com.social.api.domain.model.AuthProvider
import com.social.api.domain.model.User
import com.social.api.domain.payload.ApiResponse
import com.social.api.domain.payload.AuthResponse
import com.social.api.domain.payload.LoginRequest
import com.social.api.domain.payload.SignUpRequest
import com.social.api.domain.repository.UserRepository
import com.social.api.presentation.exception.BadRequestException
import com.social.api.presentation.security.TokenProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import javax.validation.Valid

@RestController
@RequestMapping("/auth")
class AuthController {

    @Autowired
    lateinit var authenticationManager: AuthenticationManager

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    lateinit var tokenProvider: TokenProvider

    @PostMapping("/login")
    fun authenticateUser(@Valid @RequestBody loginRequest: LoginRequest): ResponseEntity<*> {

        val authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                        loginRequest.email,
                        loginRequest.password
                )
        )

        SecurityContextHolder.getContext().authentication = authentication

        val token = tokenProvider.createToken(authentication)
        return ResponseEntity.ok(AuthResponse(token))
    }

    @PostMapping("/signup")
    fun registerUser(@Valid @RequestBody signUpRequest: SignUpRequest): ResponseEntity<*> {
        if (userRepository.existsByEmail(signUpRequest.email)) {
            throw BadRequestException("Email address already in use.")
        }

        return userRepository.save(User(signUpRequest.email,
                signUpRequest.name,
                passwordEncoder.encode(signUpRequest.password),
                provider = AuthProvider.LOCAL)).let { user ->
            ServletUriComponentsBuilder
                    .fromCurrentContextPath().path("/user/me")
                    .buildAndExpand(user.id).toUri().run {
                        ResponseEntity.created(this)
                                .body<Any>(ApiResponse(true,
                                        "User registered successfully@",
                                        user))
                    }
        }
    }

}
