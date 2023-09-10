package com.example.knowthisdog.dogList

import androidx.compose.runtime.mutableStateOf
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

var doglist = mutableStateOf<List<Dog>>(listOf())
    private set

    var status = mutableStateOf<ApiResponseStatus<Any>?>(null)
        private set

    private val dogRepository = DogRepository()

    init {

        getDogCollection()
    }


    private fun getDogCollection(){
        viewModelScope.launch {
            status.value = ApiResponseStatus.Loading()
            handleResponseStatus(dogRepository.getDogCollection())
        }
    }


@Suppress("UNCHECKED_CAST")
    private fun handleResponseStatus(apiResponseStatus: ApiResponseStatus<List<Dog>>){
        if(apiResponseStatus is ApiResponseStatus.Success){
            doglist.value = apiResponseStatus.data!!
        }

        status.value = apiResponseStatus as ApiResponseStatus<Any>
    }



}