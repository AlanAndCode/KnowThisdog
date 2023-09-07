package com.example.knowthisdog.dogList

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.recyclerview.widget.GridLayoutManager
import coil.annotation.ExperimentalCoilApi
import com.example.knowthisdog.api.ApiResponseStatus
import com.example.knowthisdog.api.dogdetail.DogDetailComposeActivity
import com.example.knowthisdog.databinding.ActivityDogListBinding
private const val GRID_SPAN_COUNT = 3
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalCoilApi
class DogListActivity : AppCompatActivity() {

    private val dogListViewModel: DogListViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDogListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val loadingWheel = binding.loadingWheel

        val recycler = binding.dogRecycler
        recycler.layoutManager = GridLayoutManager(this, GRID_SPAN_COUNT)


        val adapter = DogAdapter()

        adapter.setOnItemClickListener {
            //passing dog clicked to dog detail
            val intent = Intent(this, DogDetailComposeActivity::class.java)
            intent.putExtra(DogDetailComposeActivity.DOG_KEY, it)
            startActivity(intent)
        }


        recycler.adapter = adapter


        dogListViewModel.dogList.observe(this) { dogList ->
            adapter.submitList(dogList)
        }

        dogListViewModel.status.observe(this) { status ->
            when (status) {
                is ApiResponseStatus.Error -> {
                    loadingWheel.visibility = View.GONE
                    Toast.makeText(this, status.messageId, Toast.LENGTH_SHORT).show()

                }
                is ApiResponseStatus.Loading -> loadingWheel.visibility = View.VISIBLE
                is ApiResponseStatus.Success -> loadingWheel.visibility = View.GONE
            }
        }
    }
}