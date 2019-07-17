package com.social.api.presentation.security.oauth2

import com.social.api.domain.model.AuthProvider
import com.social.api.domain.model.User
import com.social.api.domain.repository.UserRepository
import com.social.api.presentation.exception.OAuth2AuthenticationProcessingException
import com.social.api.presentation.security.UserPrincipal
import com.social.api.presentation.security.oauth2.user.OAuth2UserInfo
import com.social.api.presentation.security.oauth2.user.OAuth2UserInfoFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.InternalAuthenticationServiceException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils

@Service
class CustomOAuth2UserService : DefaultOAuth2UserService() {

    @Autowired
    lateinit var userRepository: UserRepository

    @Throws(OAuth2AuthenticationException::class)
    override fun loadUser(oAuth2UserRequest: OAuth2UserRequest): OAuth2User {
        val oAuth2User = super.loadUser(oAuth2UserRequest)

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User)
        } catch (ex: AuthenticationException) {
            throw ex
        } catch (ex: Exception) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw InternalAuthenticationServiceException(ex.message, ex.cause)
        }

    }

    private fun processOAuth2User(oAuth2UserRequest: OAuth2UserRequest, oAuth2User: OAuth2User): OAuth2User {
        val oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.clientRegistration.registrationId, oAuth2User.attributes)
        if (StringUtils.isEmpty(oAuth2UserInfo.email)) {
            throw OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider")
        }

        var user = userRepository.findByEmail(oAuth2UserInfo.email)
        user = if (user != null) {
            if (!user.provider.equals(AuthProvider.valueOf(oAuth2UserRequest.clientRegistration.registrationId))) {
                throw OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
                        user.provider + " account. Please use your " + user.provider +
                        " account to login.")
            }
            updateExistingUser(user, oAuth2UserInfo)
        } else {
            registerNewUser(oAuth2UserRequest, oAuth2UserInfo)
        }

        return UserPrincipal.create(user, oAuth2User.attributes)
    }

    private fun registerNewUser(oAuth2UserRequest: OAuth2UserRequest, oAuth2UserInfo: OAuth2UserInfo): User =
            userRepository.save(User(oAuth2UserInfo.email,
                    oAuth2UserInfo.name,
                    imageUrl = oAuth2UserInfo.imageUrl,
                    provider = AuthProvider.valueOf(oAuth2UserRequest.clientRegistration.registrationId),
                    providerId = oAuth2UserInfo.id))

    private fun updateExistingUser(existingUser: User, oAuth2UserInfo: OAuth2UserInfo): User =
            existingUser.run {
                username = oAuth2UserInfo.name
                imageUrl = oAuth2UserInfo.imageUrl
                userRepository.save(this)
            }

}
