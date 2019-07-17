package com.social.api.presentation.security.oauth2

import com.social.api.getCookie
import com.social.api.presentation.config.AppProperties
import com.social.api.presentation.exception.BadRequestException
import com.social.api.presentation.security.TokenProvider
import com.social.api.presentation.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.Companion.REDIRECT_URI_PARAM_COOKIE_NAME
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder
import java.io.IOException
import java.net.URI
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class OAuth2AuthenticationSuccessHandler @Autowired
internal constructor(private val tokenProvider: TokenProvider, private val appProperties: AppProperties,
                     private val httpCookieOAuth2AuthorizationRequestRepository: HttpCookieOAuth2AuthorizationRequestRepository) : SimpleUrlAuthenticationSuccessHandler() {

    @Throws(IOException::class, ServletException::class)
    override fun onAuthenticationSuccess(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication) {
        val targetUrl = determineTargetUrl(request, response, authentication)

        if (response.isCommitted) {
            logger.debug("Response has already been committed. Unable to redirect to $targetUrl")
            return
        }

        clearAuthenticationAttributes(request, response)
        redirectStrategy.sendRedirect(request, response, targetUrl)
    }

    protected fun determineTargetUrl(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication): String {
        val redirectUri = request.getCookie(REDIRECT_URI_PARAM_COOKIE_NAME)?.value

        if (redirectUri != null && !isAuthorizedRedirectUri(redirectUri)) {
            throw BadRequestException("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication")
        }

        return UriComponentsBuilder.fromUriString(redirectUri ?: defaultTargetUrl)
                .queryParam("token", tokenProvider.createToken(authentication))
                .build().toUriString()
    }

    protected fun clearAuthenticationAttributes(request: HttpServletRequest, response: HttpServletResponse) {
        super.clearAuthenticationAttributes(request)
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response)
    }

    private fun isAuthorizedRedirectUri(uri: String): Boolean =
            appProperties.oauth2.authorizedRedirectUris
                    .stream()
                    .anyMatch {
                        // Only validate host and port. Let the clients use different paths if they want to
                        URI.create(it).run {
                            URI.create(uri).let {
                                host.equals(it.host, ignoreCase = true) && port == it.port
                            }

                        }
                    }
}
