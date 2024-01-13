package com.example.app

import android.media.Image
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.repository.model.Character
import com.example.app.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getData()
        setContent {
            MainContent(viewModel)
        }
    }
}

@Composable
fun MainContent(viewModel: MainViewModel) {
    AppTheme{
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val uiState by viewModel.immutablePeopleData.observeAsState(UIState(isLoading = true))
            Showcase(uiState = uiState, modifier = Modifier.fillMaxSize())
        }
    }
}


@Composable
fun Showcase(uiState: UIState<List<Character>>, modifier: Modifier = Modifier) {
    when {
        uiState.isLoading -> ShowLoadingIndicator()
        uiState.error != null -> ShowErrorMessage(uiState.error.toString())
        else -> uiState.data?.let { ShowCharactersData(it, modifier) }
    }
}

@Composable
fun ShowCharactersData(characters: List<Character>?, modifier: Modifier) {
    if (characters?.isNotEmpty() == true) {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
        ) {
            items(characters) { character ->
                CharacterCard(character = character)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    } else {
        Text(
            text = "No characters data.",
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CharacterCard(character: Character) {
    Log.d("CharacterCard", "${character.name} | ${character.gender} | ${character.hair_color} | ${character.mass}")

    val imageResource = getImageResource(character = character)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(
            modifier = Modifier
                .background(Color.White)
                .clip(RoundedCornerShape(8.dp))
        ) {
            CharacterCardContent(character = character, imageResource)
        }
    }
}

@Composable
fun CharacterCardContent(character: Character, imageResource: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .border(2.dp, Color.Gray, shape = RoundedCornerShape(10.dp))
            .padding(8.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = character.name,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            CharacterDetail("Name", character.name)
            CharacterDetail("Gender", character.gender)
            CharacterDetail("Hair Color", character.hair_color)
            CharacterDetail("Mass", character.mass)
        }

        Image(
            painter = painterResource(id = imageResource),
            contentDescription = null,
            modifier = Modifier
                .width(100.dp)
                .height(100.dp),
            contentScale = ContentScale.FillBounds
        )
    }
}


@Composable
fun CharacterDetail(detailName: String, detailValue: String) {
    Row(
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "$detailName: ",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Color.Black
        )
        Text(
            text = detailValue,
            fontSize = 16.sp,
            color = Color.Black
        )
    }
}

fun getImageResource(character: Character): Int {
    return when {
        character.gender.contains("female") -> R.drawable.female
        character.gender.contains("male") -> R.drawable.male
        else -> R.drawable.na
    }
}

@Composable
fun ShowLoadingIndicator() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(64.dp),
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun ShowErrorMessage(error: String?) {
    if (error != null) {
        Text(
            text = "Error: $error",
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            color = Color.Red,
            textAlign = TextAlign.Center
        )
    }
}