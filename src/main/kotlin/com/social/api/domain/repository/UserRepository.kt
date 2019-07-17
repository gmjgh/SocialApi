package com.social.api.domain.repository

import com.social.api.domain.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {

    fun findByUsername(username: String): User?

    fun findByEmail(email: String): User?

    fun existsByEmail(email: String?): Boolean

}