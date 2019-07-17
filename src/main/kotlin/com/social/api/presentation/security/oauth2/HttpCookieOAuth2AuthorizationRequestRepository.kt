package com.social.api.presentation.security.oauth2

import com.social.api.*
import com.nimbusds.oauth2.sdk.util.StringUtils
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest
import org.springframework.stereotype.Component

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class HttpCookieOAuth2AuthorizationRequestRepository : AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    override fun loadAuthorizationRequest(request: HttpServletRequest): OAuth2AuthorizationRequest? =
            request.getCookie(OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)?.fromBase64<OAuth2AuthorizationRequest>()

    override fun saveAuthorizationRequest(authorizationRequest: OAuth2AuthorizationRequest?, request: HttpServletRequest, response: HttpServletResponse) {
        if (authorizationRequest == null) {
            request.deleteCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
                    .deleteCookie(response, REDIRECT_URI_PARAM_COOKIE_NAME)
            return
        }

        response.addNewCookie(OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME, authorizationRequest.toBase64(), cookieExpireSeconds)
        val redirectUriAfterLogin = request.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME)
        if (StringUtils.isNotBlank(redirectUriAfterLogin)) {
            response.addNewCookie(REDIRECT_URI_PARAM_COOKIE_NAME, redirectUriAfterLogin, cookieExpireSeconds)
        }
    }

    override fun removeAuthorizationRequest(request: HttpServletRequest): OAuth2AuthorizationRequest? =
            loadAuthorizationRequest(request)


    fun removeAuthorizationRequestCookies(request: HttpServletRequest, response: HttpServletResponse) {
        request.deleteCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
                .deleteCookie(response, REDIRECT_URI_PARAM_COOKIE_NAME)
    }

    companion object {
        val OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request"
        val REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri"
        private val cookieExpireSeconds = 180
    }
}
