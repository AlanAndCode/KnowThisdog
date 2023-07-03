package com.example.knowthisdog.api.dogdetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import coil.load
import com.example.knowthisdog.Dog
import com.example.knowthisdog.R
import com.example.knowthisdog.databinding.ActivityDogDetailBinding

class DogDetailActivity : AppCompatActivity() {

    companion object{
      const val DOG_KEY = "dog"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDogDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val dog = intent?.extras?.getParcelable<Dog>(DOG_KEY)

        if(dog == null) {
            Toast.makeText(this, "Dog not found", Toast.LENGTH_LONG)
            finish()
            return
        }

        binding.dogIndex.text = getString(R.string.dog_index_format, dog.index)
        binding.lifeExpectancy.text = getString(R.string.dog_life_expectancy, dog.lifeExpectancy)
        binding.dog = dog
        binding.dogImage.load("https://example.com/image.jpg")
        binding.closeButton.setOnClickListener{
            finish()
        }
    }
}