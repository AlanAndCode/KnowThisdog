package com.example.knowthisdog.dogList

import com.example.knowthisdog.Dog
import com.example.knowthisdog.api.DogsApi.retrofitService
import com.example.knowthisdog.api.dto.DogDTOMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DogRepository {

    suspend fun downloadDogs(): List<Dog>{
return withContext(Dispatchers.IO){
  val dogListApiResponse = retrofitService.getAllDogs()
  val dogDtoList = dogListApiResponse.data.dogs
  val dogDTOMapper = DogDTOMapper()
  dogDTOMapper.fromDogDTOListToDogDomainList(dogDtoList)

}
    }

}