package com.example.karinafitriamalia_2417051014_utptam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.karinafitriamalia_2417051014_utptam.model.Video
import com.example.karinafitriamalia_2417051014_utptam.model.VideoYoutube
import com.example.karinafitriamalia_2417051014_utptam.ui.theme.KarinaFitriamalia_2417051014_UTPTAMTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class Halaman { BERANDA, DETAIL, LOADING, KOLEKSI }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KarinaFitriamalia_2417051014_UTPTAMTheme {

                var halaman by remember { mutableStateOf(Halaman.BERANDA) }
                var videoTerpilih by remember { mutableStateOf<Video?>(null) }
                var tabAktif by remember { mutableIntStateOf(0) }
                val riwayat = remember { mutableStateListOf<Video>() }

                LaunchedEffect(halaman) {
                    if (halaman == Halaman.LOADING) {
                        delay(2000)
                        halaman = Halaman.BERANDA
                    }
                }

                Surface(modifier = Modifier.fillMaxSize()) {
                    when (halaman) {

                        Halaman.LOADING -> HalamanLoading()

                        Halaman.DETAIL -> videoTerpilih?.let {
                            HalamanDetail(video = it) {
                                halaman = Halaman.LOADING
                            }
                        }

                        else -> Scaffold(
                            bottomBar = {
                                NavigationBar {
                                    NavigationBarItem(
                                        selected = tabAktif == 0,
                                        onClick = {
                                            tabAktif = 0
                                            halaman = Halaman.BERANDA
                                        },
                                        icon = { Icon(Icons.Default.Home, null) },
                                        label = { Text("Beranda") }
                                    )

                                    NavigationBarItem(
                                        selected = tabAktif == 1,
                                        onClick = {
                                            tabAktif = 1
                                            halaman = Halaman.KOLEKSI
                                        },
                                        icon = { Icon(Icons.Default.List, null) },
                                        label = { Text("Koleksi") }
                                    )
                                }
                            }
                        ) { padding ->

                            Box(modifier = Modifier.padding(padding)) {
                                if (tabAktif == 0) {
                                    HalamanBeranda { video ->
                                        if (!riwayat.contains(video)) riwayat.add(0, video)
                                        videoTerpilih = video
                                        halaman = Halaman.DETAIL
                                    }
                                } else {
                                    HalamanKoleksi(riwayat)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HalamanLoading() {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        scope.launch {
            delay(1500)
            isLoading = false
            snackbarHostState.showSnackbar("Video berhasil dimuat!")
        }
    }

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (isLoading) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator()
                Spacer(Modifier.height(16.dp))
                Text("Memuat Video...")
            }
        }
        SnackbarHost(snackbarHostState, Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
fun HalamanBeranda(onKlikVideo: (Video) -> Unit) {
    LazyColumn {
        item {
            Text(
                "YouTAM",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )
        }

        items(VideoYoutube.dummyVideos) { video ->
            KartuVideo(video) { onKlikVideo(video) }
        }
    }
}

@Composable
fun KartuVideo(video: Video, onClick: () -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Image(
            painter = painterResource(video.imageRes),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.height(8.dp))

        Text(video.title, fontWeight = FontWeight.Bold)
        Text(video.channelName, fontSize = 12.sp)
        Text(video.views, fontSize = 12.sp)
    }
}

@Composable
fun HalamanDetail(video: Video, onBack: () -> Unit) {
    Column {
        Box {
            Image(
                painter = painterResource(video.imageRes),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                contentScale = ContentScale.Crop
            )

            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
            }
        }

        Text(video.title, Modifier.padding(16.dp), fontWeight = FontWeight.Bold)
        Text(video.channelName, Modifier.padding(horizontal = 16.dp))
        Text(video.views, Modifier.padding(horizontal = 16.dp))
    }
}

@Composable
fun HalamanKoleksi(riwayat: List<Video>) {
    LazyColumn {
        item {
            Text("Koleksi", fontSize = 20.sp, modifier = Modifier.padding(16.dp))
        }

        items(riwayat) { video ->
            Text(video.title, modifier = Modifier.padding(16.dp))
        }
    }
}