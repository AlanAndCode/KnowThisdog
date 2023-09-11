package com.example.knowthisdog.dogList

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.knowthisdog.R
import com.example.knowthisdog.auth.model.Dog
private const val GRID_SPAN_COUNT = 3
    @ExperimentalMaterialApi
    @ExperimentalCoilApi
@Composable
fun DogListScreen(onNavigationIconClick: () -> Unit,dogList: List<Dog>, onDogClicked: (Dog) -> Unit) {
        Scaffold (
            topBar = { DogListScreenTopBar(onNavigationIconClick) }
        ){
            LazyVerticalGrid(
                columns = GridCells.Fixed(GRID_SPAN_COUNT),
                content = {
                    items(dogList){
                        DogGridItem(dog = it, onDogClicked)
                    }

                } )
        }

}

@Composable
fun DogListScreenTopBar(
    onClick: () -> Unit
) {
    TopAppBar(
        title = { Text(stringResource(R.string.my_dog_collection)) },
        backgroundColor = Color.White,
        contentColor = Color.Black,
        navigationIcon = { BackNavigationIcon(onClick) }
    )
}

@Composable
fun BackNavigationIcon(
    onClick: () -> Unit
) {
IconButton(onClick = { }) {
    Icon(painter = rememberVectorPainter(image = Icons.Sharp.ArrowBack),
        contentDescription = null)
}
}

@ExperimentalMaterialApi
@ExperimentalCoilApi
@Composable
fun DogGridItem(dog: Dog, onDogClicked: (Dog) -> Unit) {
    if (dog.inCollection) {
        Surface(
            modifier = Modifier
                .padding(8.dp)
                .height(100.dp)
                .width(100.dp),
            onClick = { onDogClicked(dog) },
            shape = RoundedCornerShape(4.dp)
        ) {
            Image(
                painter = rememberImagePainter(dog.imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .background(Color.White)
                    .semantics { testTag = "dog-${dog.name}" }
            )
        }
    } else {
        Surface(
            modifier = Modifier
                .padding(8.dp)
                .height(100.dp)
                .width(100.dp),
            color = Color.Red,
            shape = RoundedCornerShape(4.dp)
        ) {
            Text(
                text = dog.index.toString(),
                color = Color.White,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                textAlign = TextAlign.Center,
                fontSize = 42.sp,
                fontWeight = FontWeight.Black
            )
        }
    }
}

@Composable
fun DogItem(dog: Dog, onDogClicked: (Dog) -> Unit){
    if (dog.inCollection) {

        Text(
            modifier = Modifier
                .padding(16.dp)
                .clickable {
                    onDogClicked(dog)
                },
            text = dog.name
        )
    }else {
        Text( modifier = Modifier
            .padding(16.dp)
            .background(color = Color.Red),
       text = stringResource(id = R.string.dog_index_format, dog.index))

    }

}
