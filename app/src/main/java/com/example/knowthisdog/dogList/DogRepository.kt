package com.example.knowthisdog.dogList

import com.example.knowthisdog.auth.model.Dog
import com.example.knowthisdog.api.ApiResponseStatus
import com.example.knowthisdog.api.DogsApi.retrofitService
import com.example.knowthisdog.api.dto.AddDogToUserDTO
import com.example.knowthisdog.api.dto.DogDTOMapper
import com.example.knowthisdog.api.makeNetworkCall

class DogRepository {

    suspend fun downloadDogs(): ApiResponseStatus<List<Dog>> = makeNetworkCall {
            val dogListApiResponse = retrofitService.getAllDogs()
            val dogDTOList = dogListApiResponse.data.dogs
            val dogDTOMapper = DogDTOMapper()
            dogDTOMapper.fromDogDTOListToDogDomainList(dogDTOList)
        }

    suspend fun addDogToUser(dogId: Long): ApiResponseStatus<Any> = makeNetworkCall {
        val addDogToUserDTO = AddDogToUserDTO(dogId)
        val defaultResponse = retrofitService.addDogToUser(addDogToUserDTO)


        if (!defaultResponse.isSucess){
            throw Exception(defaultResponse.message)
        }
    }


    suspend fun getUserDogs(): ApiResponseStatus<List<Dog>> = makeNetworkCall {
        val dogListApiResponse = retrofitService.getAllDogs()
        val dogDTOList = dogListApiResponse.data.dogs
        val dogDTOMapper = DogDTOMapper()
        dogDTOMapper.fromDogDTOListToDogDomainList(dogDTOList)
    }

}