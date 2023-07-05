package com.example.knowthisdog.auth

import com.example.knowthisdog.api.ApiResponseStatus
import com.example.knowthisdog.api.DogsApi
import com.example.knowthisdog.api.dto.DogDTOMapper
import com.example.knowthisdog.api.dto.SignUpDTO
import com.example.knowthisdog.api.dto.UserDTOMapper
import com.example.knowthisdog.api.makeNetworkCall
import com.example.knowthisdog.model.User

class AuthRepository {
    suspend fun signUp(email: String, password: String,
                       passwordConfirmation: String): ApiResponseStatus<User> = makeNetworkCall {
        val signUpDTO = SignUpDTO(email, password, passwordConfirmation)
        val signUpResponse = DogsApi.retrofitService.signUp(signUpDTO)


        if( signUpResponse.isSucess) {
            throw Exception(signUpResponse.message)
        }

        val userDTO = signUpResponse.data.user
        val userDTOMapper = UserDTOMapper()
        userDTOMapper.fromUserDTOToUserDomain(userDTO)
    }
}