package com.social.api.presentation.security.oauth2.user

class GoogleOAuth2UserInfo(attributes: Map<String, Any>) : OAuth2UserInfo {

    override val id: String = attributes["sub"].toString()

    override val name: String = attributes["name"].toString()

    override val email: String = attributes["email"].toString()

    override val imageUrl: String = attributes["picture"].toString()

}
