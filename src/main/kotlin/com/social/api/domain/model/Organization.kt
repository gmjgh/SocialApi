package com.social.api.domain.model

import javax.persistence.*

@Entity
@PrimaryKeyJoinColumn(name = "id")
@Table(name = "ORGANIZATIONS")
data class Organization(
        var isDefault: Boolean? = true,
        @OneToMany(mappedBy = "organization", fetch = FetchType.LAZY)
        var schedule: List<Activity>?,
        @ManyToOne
        @JoinColumn(name = "organizations")
        var user: User? = null,
        @Id
        @GeneratedValue
        val id: Long? = null)


