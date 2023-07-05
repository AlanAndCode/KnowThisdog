package com.example.knowthisdog.api

import com.example.knowthisdog.BASE_URL
import com.example.knowthisdog.GET_ALL_DOGS_URL
import com.example.knowthisdog.SIGN_UP_URL
import com.example.knowthisdog.api.dto.SignUpDTO
import com.example.knowthisdog.api.responses.DogListApiResponse
import com.example.knowthisdog.api.responses.SignUpApiResponse
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create())
    .build()

interface  ApiService{
    @GET(GET_ALL_DOGS_URL)
    suspend fun getAllDogs(): DogListApiResponse

    @POST(SIGN_UP_URL)
    suspend fun signUp(@Body signUpDTO: SignUpDTO): SignUpApiResponse
}

object DogsApi {
    val retrofitService : ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}