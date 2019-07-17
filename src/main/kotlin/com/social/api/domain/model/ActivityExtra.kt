package com.social.api.domain.model

import javax.persistence.*

@Entity
@PrimaryKeyJoinColumn(name = "id")
@Table(name = "ACTIVITY_EXTRAS")
data class ActivityExtra(@Column
                         @Convert(converter = ExtraTypeConverter::class)
                         val type: ExtraType,
                         @Column
                         val description: String,
                         @Column
                         val name: String,
                         @ManyToOne
                         @JoinColumn(name = "extras")
                         var activity: Activity? = null,
                         val userId: Long? = activity?.userId,
                         @Id
                         @GeneratedValue
                         val id: Long? = null)