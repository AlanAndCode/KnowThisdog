package com.example.knowthisdog.main

import androidx.camera.core.ImageProxy
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.knowthisdog.api.ApiResponseStatus
import com.example.knowthisdog.auth.model.Dog
import com.example.knowthisdog.dogList.DogRepository
import com.example.knowthisdog.machinelearning.Classifier
import com.example.knowthisdog.machinelearning.ClassifierRepository
import com.example.knowthisdog.machinelearning.DogRecognition
import kotlinx.coroutines.launch
import java.nio.MappedByteBuffer

class MainViewModel: ViewModel() {

    private val _dog = MutableLiveData<Dog>()
    val dog: LiveData<Dog>
        get() = _dog

    private val _status = MutableLiveData<ApiResponseStatus<Dog>>()
    val status: LiveData<ApiResponseStatus<Dog>>
        get() = _status

    private val _dogRecognition = MutableLiveData<DogRecognition>()
    val dogRecognition: LiveData<DogRecognition>
        get() = _dogRecognition

    private val dogRepository = DogRepository()
    private lateinit var classifierRepository: ClassifierRepository

fun setupClassifier(tfLiteModel: MappedByteBuffer, labels: List<String>){
   val classifier = Classifier(tfLiteModel, labels)
    classifierRepository = ClassifierRepository(classifier)
}

    fun recognizeImage(imageProxy: ImageProxy){
viewModelScope.launch {
    _dogRecognition.value = classifierRepository.recognizeImage(imageProxy)
    imageProxy.close()
}
    }
    fun getDogByMlId(mlDogId: String){
        viewModelScope.launch {
            handleResponseStatus(dogRepository.getDogByMlId(mlDogId))
        }
    }

    private fun handleResponseStatus(apiResponseStatus: ApiResponseStatus<Dog>) {
        if (apiResponseStatus is ApiResponseStatus.Success) {
            _dog.value = apiResponseStatus.data!!
        }

        _status.value = apiResponseStatus

    }



}