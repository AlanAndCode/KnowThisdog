package com.example.knowthisdog.dogList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.knowthisdog.Dog
import kotlinx.coroutines.launch

class DogListViewModel : ViewModel() {

private val _doglist = MutableLiveData<List<Dog>>()
val dogList: LiveData<List<Dog>>
get() = _doglist


    private val dogRepository = DogRepository()

    init {
        downloadDogs()
    }

    private fun downloadDogs() {
      viewModelScope.launch {
          _doglist.value = dogRepository.downloadDogs()
      }
    }

}