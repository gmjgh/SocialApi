package com.social.api.domain.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.annotations.NaturalId
import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
@PrimaryKeyJoinColumn(name = "id")
@Table(name = "USERS", uniqueConstraints = [UniqueConstraint(columnNames = ["email", "username"])])
data class User(
        @Email
        @Column(name = "email", nullable = false)
        var email: String,

        @NaturalId
        @Column(name = "username", unique = true, nullable = false)
        @NotNull
        @Size(min = 3, max = 255)
        var username: String,

        @Column(name = "password")
        @Size(min = 8)
        @get:JsonIgnore
        @set:JsonProperty
        var password: String? = null,

        @Column(name = "subscription_active")
        var isSubscriptionActive: Boolean = false,

        @Column(name = "first_name")
        var firstName: String? = null,

        @Column(name = "last_name")
        var lastName: String? = null,

        var imageUrl: String? = null,

        @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
        var organizations: List<Organization>? = null,

        @NotNull
        @Enumerated(EnumType.STRING)
        var provider: AuthProvider,

        var providerId: String? = null) {
    @Id
    @GeneratedValue
    var id: Long? = null
}