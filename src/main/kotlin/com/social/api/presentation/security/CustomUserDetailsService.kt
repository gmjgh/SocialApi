package com.social.api.presentation.security

import com.social.api.domain.repository.UserRepository
import com.social.api.presentation.exception.ResourceNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CustomUserDetailsService : UserDetailsService {

    @Autowired
    lateinit var userRepository: UserRepository

    @Transactional
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(email: String): UserDetails =
            UserPrincipal.create(userRepository.findByEmail(email)
                    ?: throw UsernameNotFoundException("User not found with email : $email"))


    @Transactional
    fun loadUserById(id: Long): UserDetails =
        UserPrincipal.create(userRepository.findById(id)
                .orElseThrow { ResourceNotFoundException("User", "id", id) })


}