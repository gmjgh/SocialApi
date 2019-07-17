package com.social.api.domain.payload

data class AuthResponse(var accessToken: String) {
    var tokenType = "Bearer"
}
