package com.example.coilimageexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.coilimageexample.ui.theme.CoilImageExampleTheme

// libs.versions.toml: Kotlin upgrade, kotlin = "2.1.0" # "2.0.21"

// AndroidManifest.xml
// <uses-permission android:name="android.permission.INTERNET" />

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CoilImageExampleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PicturesScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

// Coil docs https://coil-kt.github.io/coil/
@Composable
fun PicturesScreen(modifier: Modifier = Modifier) {
    val catIDs = remember {
        listOf(
            "0ztFbDrgDV2K7yJ1",
            "0GC9MRUAqxhBzPyA",
            "non-existing",
            "0RU7ZkgzyvWv8UJG",
            "1CF7xZmlX0t8QpgP",
            "1DrcyohjhwcNaRIz",
            "1KeQpy7eHqi0SFmc"
        )
    }
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        item {
            Text(
                text = "SimpleHttpClearTextLoadedPicture",
                //modifier = Modifier.padding(5.dp)
            )
        }
        item {
            // AndroidManifest.xml android:usesCleartextTraffic="true"
            // to allow for non-secure network request
            SimpleHttpClearTextLoadedPicture("http://www.anbo-easj.dk/cv/andersBorjesson.jpg")
        }

        item { Text(text = "AdvancedLoadWithListener") }

        item {
            AdvancedLoadWithListener("https://www.anbo-easj.dk/cv/andersBorjesson.jpg")
        }

        item { Text("List of cats") }

        items(catIDs)
        { catId ->
            SimpleHttpClearTextLoadedPicture(
                "https://cataas.com/cat/${catId}"
            )
        }
    }
}

@Composable
fun SimpleHttpClearTextLoadedPicture(url: String) {
    Card(
        modifier = Modifier
            //.fillMaxWidth() // Fill width of the list
            .padding(vertical = 8.dp)
    ) {
        AsyncImage(
            model = url,
            contentDescription = "my picture",
            modifier = Modifier
                .height(100.dp)
                .padding(10.dp),
            contentScale = ContentScale.Fit,
            placeholder = painterResource(R.drawable.ic_launcher_background),
            error = painterResource(R.drawable.ic_launcher_foreground)
        )
    }
}

@Composable
fun AdvancedLoadWithListener(url: String) {
    var statusMessage by remember { mutableStateOf("") }

    val context = LocalContext.current
    val imageRequest = remember(context, url) {
        ImageRequest.Builder(context)
            .data(url)
            .crossfade(true)
            .listener(
                onStart = { statusMessage = "started loading" },
                onSuccess = { request, result -> statusMessage = "loaded" },
                onError = { request, result ->
                    statusMessage = "error loading ${request.data}: ${result.throwable.message}"
                },
                onCancel = { statusMessage = "Canceled loading" },
            )
            .build()
    }
    Card(
        colors = CardDefaults.cardColors(
            // Change background color slightly on error
            containerColor =
                if (statusMessage.contains(
                        "error",
                        ignoreCase = true
                    )
                ) MaterialTheme.colorScheme.errorContainer
                else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        AsyncImage(
            model = imageRequest,
            placeholder = painterResource(R.drawable.ic_launcher_foreground),
            contentDescription = "my picture",
            modifier = Modifier
                .height(100.dp)
                .padding(5.dp),
            error = painterResource(R.drawable.ic_launcher_foreground)
        )
        Text(text = statusMessage, modifier = Modifier.padding(5.dp))
    }
}