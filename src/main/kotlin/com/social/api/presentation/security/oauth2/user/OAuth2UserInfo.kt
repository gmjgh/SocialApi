package com.social.api.presentation.security.oauth2.user

interface OAuth2UserInfo {

    val id: String

    val name: String

    val email: String

    val imageUrl: String?
}
