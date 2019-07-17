package com.social.api.domain.payload

data class ApiResponse(var isSuccess: Boolean, var message: String?, var body: Any = "")
