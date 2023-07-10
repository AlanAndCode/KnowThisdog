package com.example.knowthisdog.dogList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.knowthisdog.auth.model.Dog
import com.example.knowthisdog.api.ApiResponseStatus
import com.example.knowthisdog.api.dto.AddDogToUserDTO
import com.example.knowthisdog.api.makeNetworkCall
import kotlinx.coroutines.launch

class DogListViewModel : ViewModel() {

private val _doglist = MutableLiveData<List<Dog>>()
val dogList: LiveData<List<Dog>>
get() = _doglist

    private val _status = MutableLiveData<ApiResponseStatus<Any>>()
    val status: LiveData<ApiResponseStatus<Any>>
        get() = _status

    private val dogRepository = DogRepository()

    init {
        downloadUserDogs()
    }

    private fun downloadUserDogs(){
        viewModelScope.launch {
            _status.value = ApiResponseStatus.Loading()
            handleResponseStatus(dogRepository.getUserDogs())
        }
        }

    private fun downloadDogs() {

              viewModelScope.launch{
                  _status.value = ApiResponseStatus.Loading()
                 handleResponseStatus(dogRepository.downloadDogs())


      }
    }

    fun addDogToUser(dogId: Long) {
        viewModelScope.launch {
            _status.value = ApiResponseStatus.Loading()
            handleAddDogToUserResponseStatus(dogRepository.addDogToUser(dogId))
        }

    }
@Suppress("UNCHECKED_CAST")
    private fun handleResponseStatus(apiResponseStatus: ApiResponseStatus<List<Dog>>){
        if(apiResponseStatus is ApiResponseStatus.Success){
            _doglist.value = apiResponseStatus.data!!
        }

        _status.value = apiResponseStatus as ApiResponseStatus<Any>
    }

    private fun handleAddDogToUserResponseStatus(apiResponseStatus: ApiResponseStatus<Any>) {
 if(apiResponseStatus is ApiResponseStatus.Success){
downloadDogs()

 }
        _status.value = apiResponseStatus
    }

}