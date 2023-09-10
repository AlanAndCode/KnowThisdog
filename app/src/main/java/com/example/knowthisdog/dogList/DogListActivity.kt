package com.example.knowthisdog.dogList

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import coil.annotation.ExperimentalCoilApi
import com.example.knowthisdog.api.dogdetail.DogDetailComposeActivity
import com.example.knowthisdog.api.dogdetail.ui.theme.KnowThisDogTheme
import com.example.knowthisdog.auth.model.Dog
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalCoilApi
class DogListActivity : ComponentActivity() {

    private val viewModel: DogListViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       
         setContent {

             KnowThisDogTheme {
                 val dogList = viewModel.doglist
                 DogListScreen(dogList = dogList.value,
                               onDogClicked = ::openDogDetailActivity
                 )
             }
         }
        
    }
    private fun openDogDetailActivity(dog: Dog){
        val intent = Intent(this, DogDetailComposeActivity::class.java)
        intent.putExtra(DogDetailComposeActivity.DOG_KEY, dog)
        startActivity(intent)
    }
}