package com.example.knowthisdog.api.dogdetail

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.knowthisdog.api.ApiResponseStatus
import com.example.knowthisdog.dogList.DogRepository
import kotlinx.coroutines.launch

class DogDetailViewModel: ViewModel() {

    var  status = mutableStateOf<ApiResponseStatus<Any>?>(ApiResponseStatus.Loading())
       private set
    private val dogRepository = DogRepository()
    fun addDogToUser(dogId: Long) {
        viewModelScope.launch {
            status.value = ApiResponseStatus.Loading()
            handleAddDogToUserResponseStatus(dogRepository.addDogToUser(dogId))
        }

    }

    private fun handleAddDogToUserResponseStatus(apiResponseStatus: ApiResponseStatus<Any>) {
        status.value = apiResponseStatus
    }
}