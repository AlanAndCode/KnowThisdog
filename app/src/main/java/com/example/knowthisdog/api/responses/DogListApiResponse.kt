package com.example.knowthisdog.api.responses

import com.squareup.moshi.Json

class DogListApiResponse (val message: String,
                          @field:Json(name = "is_sucess") val isSucess: Boolean,
                          val data: DogListResponse)