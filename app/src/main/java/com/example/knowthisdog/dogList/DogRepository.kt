package com.example.knowthisdog.dogList

import com.example.knowthisdog.model.Dog
import com.example.knowthisdog.api.ApiResponseStatus
import com.example.knowthisdog.api.DogsApi.retrofitService
import com.example.knowthisdog.api.dto.DogDTOMapper
import com.example.knowthisdog.api.makeNetworkCall

class DogRepository {

    suspend fun downloadDogs(): ApiResponseStatus<List<Dog>> = makeNetworkCall {
            val dogListApiResponse = retrofitService.getAllDogs()
            val dogDTOList = dogListApiResponse.data.dogs
            val dogDTOMapper = DogDTOMapper()
            dogDTOMapper.fromDogDTOListToDogDomainList(dogDTOList)
        }
}