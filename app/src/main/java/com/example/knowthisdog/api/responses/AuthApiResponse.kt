package com.example.knowthisdog.api.responses

import com.squareup.moshi.Json

class AuthApiResponse(
    val message: String,

    @field:Json(name = "is_sucess") val isSucess: Boolean,
val data: UserResponse,

    )