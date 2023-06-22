package com.example.knowthisdog.dogList

import com.example.knowthisdog.Dog
import com.example.knowthisdog.R
import com.example.knowthisdog.api.ApiResponseStatus
import com.example.knowthisdog.api.DogsApi.retrofitService
import com.example.knowthisdog.api.dto.DogDTOMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.UnknownHostException

class DogRepository {

    suspend fun downloadDogs(): ApiResponseStatus<List<Dog>> {
        return withContext(Dispatchers.IO) {
            try {
                val dogListApiResponse = retrofitService.getAllDogs()
                val dogDTOList = dogListApiResponse.data.dogs
                val dogDTOMapper = DogDTOMapper()
                ApiResponseStatus.Success(dogDTOMapper.fromDogDTOListToDogDomainList(dogDTOList))
            } catch (e: UnknownHostException) {
                ApiResponseStatus.Error(R.string.unknown_host_exception_error)
            } catch (e: Exception){
                ApiResponseStatus.Error(R.string.unknown_error)
            }
        }
    }
}