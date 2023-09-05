package com.example.knowthisdog.api.dogdetail

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.knowthisdog.R
import com.example.knowthisdog.auth.model.Dog

@SuppressLint("ResourceType")
@Composable
fun DogDetailScreen() {
    Box(
        modifier = Modifier
            .background(colorResource(id = R.color.secondary_background))
            .padding(start = 8.dp, end = 8.dp, bottom = 16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        val dog = Dog(1L,78,"Pug",
        "Herding", 70.0, 75.0, "", "10 - 12",
            "Friendly, playful", "5", "6")
DogInformation(dog)
    }
}

@Composable
fun DogInformation(dog: Dog) {
  Box(modifier = Modifier
      .fillMaxWidth()
      .padding(top = 180.dp)

  ) {
      Surface(
          modifier = Modifier
              .fillMaxWidth(),
          shape = RoundedCornerShape(4.dp),
          color = colorResource(id = android.R.color.white)
      ){
Column(
    modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp),
    horizontalAlignment = Alignment.CenterHorizontally
) {
Text(
    modifier = Modifier
        .fillMaxWidth(),
    text = stringResource(R.string.dog_index_format,dog.index),
    fontSize = 32.sp,
    color = colorResource(id = R.color.text_black),
    textAlign = TextAlign.End
)
    Text(
       text = dog.temperament,
        textAlign = TextAlign.Center,
        fontSize = 16.sp,
        color = colorResource(id = R.color.text_black),
        fontWeight = FontWeight.Medium,
        modifier = Modifier.padding(top = 8.dp)
        )

    Text(
        stringResource(id = R.string.dog_life_expectancy, dog.lifeExpectancy),
        textAlign = TextAlign.Center,
        fontSize = 16.sp,
        color = colorResource(id = R.color.text_black)
    )
   Divider(
       modifier = Modifier
           .padding(top = 8.dp, start = 8.dp, end = 8.dp, bottom = 16.dp),
        color = colorResource(id = R.color.divider),
       thickness = 1.dp
   )
    Row( modifier = Modifier
        .fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically)
        DogDataColumn(
            modifier = Modifier.weight(1f),
            stringResource(id = R.string.female), dog.weightFemale, dog.heightFemale)

        VerticalDivider()
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text( text = dog.type,
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
                color = colorResource(id = R.color.text_black),
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(top = 8.dp)
                )
            Text( text = stringResource(id = R.string.group),
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = colorResource(id = R.color.dark_gray),
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        VerticalDivider()

        DogDataColumn(
            modifier = Modifier.weight(1f),
            stringResource(id = R.string.male), dog.weightMale, dog.heightMale)
    }
}
      }
  }
}

@Composable
private fun VerticalDivider() {
    Divider(
        modifier = Modifier
            .height(42.dp)
            .width(1.dp),
        color = colorResource(id = R.color.divider)
    )
}

@Composable
private fun DogDataColumn(
                          modifier: Modifier = Modifier,
                          genre: String,
                          weight: String,
                          height: Double) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = genre,
            textAlign = TextAlign.Center,
            color = colorResource(id = R.color.text_black),
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = weight,
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            color = colorResource(id = R.color.text_black),
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = stringResource(id = R.string.weight),
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            color = colorResource(id = R.color.dark_gray),
        )
        Text(
            text = height.toString(),
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            color = colorResource(id = R.color.text_black),
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = stringResource(id = R.string.height),
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            color = colorResource(id = R.color.dark_gray),

            )
    }
}

@Preview
@Composable
fun DogDetailScreenPreview(){
    DogDetailScreen()
}