package com.example.knowthisdog.dogList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.knowthisdog.model.Dog
import com.example.knowthisdog.api.ApiResponseStatus
import kotlinx.coroutines.launch

class DogListViewModel : ViewModel() {

private val _doglist = MutableLiveData<List<Dog>>()
val dogList: LiveData<List<Dog>>
get() = _doglist
    private val _status = MutableLiveData<ApiResponseStatus<List<Dog>>>()
    val status: LiveData<ApiResponseStatus<List<Dog>>>
        get() = _status
    private val dogRepository = DogRepository()

    init {
        downloadDogs()
    }

    private fun downloadDogs() {

              viewModelScope.launch{
                  _status.value = ApiResponseStatus.Loading()
                 handleResponseStatus(dogRepository.downloadDogs())


      }
    }

    private fun handleResponseStatus(apiResponseStatus: ApiResponseStatus<List<Dog>>){
        if(apiResponseStatus is ApiResponseStatus.Success){
            _doglist.value = apiResponseStatus.data
        }

        _status.value = apiResponseStatus
    }


}