package com.social.api.presentation.security.oauth2.user

import com.social.api.domain.model.AuthProvider

object OAuth2UserInfoFactory {

    fun getOAuth2UserInfo(registrationId: String, attributes: Map<String, Any>): OAuth2UserInfo {
        return when {
            registrationId.equals(AuthProvider.GOOGLE.toString(), ignoreCase = true) -> GoogleOAuth2UserInfo(attributes)
            registrationId.equals(AuthProvider.FACEBOOK.toString(), ignoreCase = true) -> FacebookOAuth2UserInfo(attributes)
            registrationId.equals(AuthProvider.GITHUB.toString(), ignoreCase = true) -> GithubOAuth2UserInfo(attributes)
            else -> throw IllegalStateException("Sorry! Login with $registrationId is not supported yet.")
        }
    }
}
