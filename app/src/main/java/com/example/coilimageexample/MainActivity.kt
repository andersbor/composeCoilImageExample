package com.example.coilimageexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CoilImageExampleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Pictures(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

// Coil docs https://coil-kt.github.io/coil/
@Composable
fun Pictures(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp)
            .verticalScroll(rememberScrollState()), // Enable scrolling,
    ) {
        // libs.versions.toml: Kotlin upgrade, kotlin = "2.1.0" # "2.0.21"

        // AndroidManifest.xml
        // <uses-permission android:name="android.permission.INTERNET" />
        AsyncImage(
            // AndroidManifest.xml android:usesCleartextTraffic="true"
            // to allow for non-secure network request
            model = "http://www.anbo-easj.dk/cv/andersBorjesson.jpg",
            contentDescription = "my picture",
            modifier = Modifier.height(100.dp),
            contentScale = ContentScale.Fit
        )

        var statusMessage by remember { mutableStateOf("") }
        Text(text = statusMessage)
        val context = LocalContext.current
        val imageRequest = remember(context) {
            ImageRequest.Builder(context)
                .data("https://www.anbo-easj.dk/cv/andersBorjesson.jpg")
                .crossfade(true)
                .listener(
                    onStart = { statusMessage = "started loading" },
                    onSuccess = { request, result -> statusMessage = "loaded" },
                    onError = { request, result ->
                        statusMessage = "error loading ${request.data}: ${result.throwable.message}"
                    },
                    onCancel = { },
                )
                .build()
        }
        AsyncImage(
            model = imageRequest,
            placeholder = painterResource(R.drawable.ic_launcher_foreground),
            contentDescription = "my picture",
            modifier = Modifier.height(100.dp),
            error = painterResource(R.drawable.ic_launcher_foreground)
        )

        val catIDs = remember { listOf("0ztFbDrgDV2K7yJ1", "0GC9MRUAqxhBzPyA", "non-existing", "0RU7ZkgzyvWv8UJG", "1CF7xZmlX0t8QpgP") }
        for (catID in catIDs) {
            AsyncImage(
                model = "https://cataas.com/cat/${catID}",
                contentDescription = "cat",
                error = painterResource(R.drawable.ic_launcher_foreground),
                placeholder = painterResource(R.drawable.ic_launcher_foreground),
                modifier = Modifier
                    .height(100.dp)
                    .padding(5.dp)
            )
        }
    }
}