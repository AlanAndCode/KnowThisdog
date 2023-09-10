package com.example.knowthisdog.api.dogdetail

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import coil.annotation.ExperimentalCoilApi
import com.example.knowthisdog.R
import com.example.knowthisdog.api.ApiResponseStatus
import com.example.knowthisdog.api.dogdetail.ui.theme.KnowThisDogTheme
import com.example.knowthisdog.auth.model.Dog

@ExperimentalCoilApi
class DogDetailComposeActivity : ComponentActivity() {
    companion object{
        const val DOG_KEY = "dog"
        const val IS_RECOGNITION_KEY = "is_recognition"
    }

    private val viewModel: DogDetailViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val dog = intent?.extras?.getParcelable<Dog>(DOG_KEY)
        val isRecognition =  intent
            ?.extras
            ?.getBoolean(IS_RECOGNITION_KEY, false) ?: false
        if(dog == null) {
            Toast.makeText(this, getString(R.string.not_found_dog), Toast.LENGTH_LONG).show()
            finish()
            return
        }
        setContent {
            val status = viewModel.status
            if(status.value is ApiResponseStatus.Success){
                finish()
            } else {


                KnowThisDogTheme {
                    // A surface container using the 'background' color from the theme
                    DogDetailScreen(dog = dog,
                        status = status.value,
                        onButtonClicked = {
                            onButtonClicked(dog.id, isRecognition)
                        },
                        onErrorDialogDismiss = ::resetApiResponseStatus
                    )
                }
            }
        }
    }

    private fun resetApiResponseStatus() {
       viewModel.resetApiResponseStatus()
    }

    private fun onButtonClicked(dogId: Long, isRecognition: Boolean){
           if (isRecognition){
               viewModel.addDogToUser(dogId)
           } else {
               finish()
           }
    }
}


