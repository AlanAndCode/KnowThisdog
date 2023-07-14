package com.example.knowthisdog.dogList

import com.example.knowthisdog.R
import com.example.knowthisdog.auth.model.Dog
import com.example.knowthisdog.api.ApiResponseStatus
import com.example.knowthisdog.api.DogsApi.retrofitService
import com.example.knowthisdog.api.dto.AddDogToUserDTO
import com.example.knowthisdog.api.dto.DogDTOMapper
import com.example.knowthisdog.api.makeNetworkCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class DogRepository {

    suspend fun getDogCollection(): ApiResponseStatus<List<Dog>> {
return withContext(Dispatchers.IO) {
    val allDogsListReponseDeferred =  async {  downloadDogs() }
    val userDogsListResponseDeferred = async {  getUserDogs() }

    val allDogsListReponse = allDogsListReponseDeferred.await()
    val userDogsListResponse = userDogsListResponseDeferred.await()

    if (allDogsListReponse is ApiResponseStatus.Error) {
        allDogsListReponse
    } else if (userDogsListResponse is ApiResponseStatus.Error) {
        userDogsListResponse
    } else if (allDogsListReponse is ApiResponseStatus.Success && userDogsListResponse is ApiResponseStatus.Success) {
        ApiResponseStatus.Success(
            getCollectionList(
                allDogsListReponse.data,
                userDogsListResponse.data
            )
        )
    } else {
        ApiResponseStatus.Error(R.string.unknown_error)
    }
}
    }

    private fun getCollectionList(allDogList: List<Dog>, userDogList: List<Dog>): List<Dog> = allDogList.map {
    if(userDogList.contains(it)){
it
    } else {
        Dog(it.id, it.index, "", "", it.heightFemale, it.heightMale, "", "", "", "", "", inCollection = false)
    }

    }.sorted()

   private suspend fun downloadDogs(): ApiResponseStatus<List<Dog>> = makeNetworkCall {
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


   private suspend fun getUserDogs(): ApiResponseStatus<List<Dog>> = makeNetworkCall {
        val dogListApiResponse = retrofitService.getAllDogs()
        val dogDTOList = dogListApiResponse.data.dogs
        val dogDTOMapper = DogDTOMapper()
        dogDTOMapper.fromDogDTOListToDogDomainList(dogDTOList)
    }

}