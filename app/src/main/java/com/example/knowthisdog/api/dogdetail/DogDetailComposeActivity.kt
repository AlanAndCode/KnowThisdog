package com.example.knowthisdog.api.dogdetail

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import coil.annotation.ExperimentalCoilApi
import com.example.knowthisdog.R
import com.example.knowthisdog.api.dogdetail.DogDetailActivity.Companion.DOG_KEY
import com.example.knowthisdog.api.dogdetail.DogDetailActivity.Companion.IS_RECOGNITION_KEY
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
            KnowThisDogTheme {
                // A surface container using the 'background' color from the theme
                DogDetailScreen(dog = dog, status.value)
            }
        }
    }
}


