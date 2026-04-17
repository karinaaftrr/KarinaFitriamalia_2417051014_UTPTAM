package com.example.karinafitriamalia_2417051014_utptam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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

                Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFFDF7F2)) {
                    when (halaman) {
                        Halaman.LOADING -> HalamanLoading()
                        Halaman.DETAIL -> videoTerpilih?.let {
                            HalamanDetail(video = it) {
                                halaman = Halaman.LOADING
                            }
                        }
                        else -> Scaffold(
                            bottomBar = {
                                NavigationBar(containerColor = Color.White) {
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
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = Color(0xFFE64A19))
            Spacer(Modifier.height(16.dp))
            Text("Memuat Video Terkini...", color = Color.Gray)
        }
    }
}

@Composable
fun HalamanBeranda(onKlikVideo: (Video) -> Unit) {
    LazyColumn {
        item {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_youtube),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("YouTAM", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            }
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
            .padding(bottom = 16.dp)
    ) {
        Image(
            painter = painterResource(video.imageRes),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
            contentScale = ContentScale.Crop
        )
        Row(Modifier.padding(12.dp)) {
            Icon(Icons.Default.AccountCircle, null, Modifier.size(40.dp))
            Spacer(Modifier.width(12.dp))
            Column {
                Text(video.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("${video.channelName} • ${video.views}", fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
fun HalamanDetail(video: Video, onBack: () -> Unit) {
    var isLiked by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxSize()) {
        Box {
            Image(
                painter = painterResource(video.imageRes),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                contentScale = ContentScale.Crop
            )
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .padding(16.dp)
                    .background(Color.Black.copy(alpha = 0.5f), CircleShape)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
            }
        }
        Column(Modifier.padding(16.dp)) {
            Text(video.title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(video.views, fontSize = 14.sp, color = Color.Gray)
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { isLiked = !isLiked }
                ) {
                    Icon(
                        if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        null,
                        tint = if (isLiked) Color(0xFFE64A19) else Color.Black
                    )
                    Text("Suka", fontSize = 12.sp)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Share, null)
                    Text("Bagikan", fontSize = 12.sp)
                }
            }
            HorizontalDivider()
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.AccountCircle, null, Modifier.size(48.dp))
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(video.channelName, fontWeight = FontWeight.Bold)
                    Text("1.5M Subscriber", fontSize = 12.sp, color = Color.Gray)
                }
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE64A19)),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text("Subscribe", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun HalamanKoleksi(riwayat: List<Video>) {
    LazyColumn(Modifier.fillMaxSize()) {
        item {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.AccountCircle, null, Modifier.size(60.dp))
                Spacer(Modifier.width(16.dp))
                Column {
                    Text("KARINA FITRIAMALIA", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text("@karinafitriamalia 2417051014 • Lihat channel", fontSize = 12.sp, color = Color(0xFFE64A19))
                }
            }
            Text("Riwayat", fontWeight = FontWeight.Bold, modifier = Modifier.padding(16.dp))
            LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(riwayat) { video ->
                    Column(Modifier.width(160.dp)) {
                        Image(
                            painter = painterResource(video.imageRes),
                            null,
                            Modifier
                                .fillMaxWidth()
                                .height(90.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Text(video.title, fontSize = 12.sp, maxLines = 2, modifier = Modifier.padding(top = 4.dp))
                    }
                }
            }
            HorizontalDivider(Modifier.padding(vertical = 24.dp))
            Text("Daftar Putar (Playlist)", fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 16.dp))
        }
        items(5) { index ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    Modifier.size(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = Color.DarkGray
                ) {
                    Icon(Icons.Default.List, null, tint = Color.White, modifier = Modifier.padding(12.dp))
                }
                Spacer(Modifier.width(16.dp))
                Column {
                    Text("Playlist Kuliah ${index + 1}", fontWeight = FontWeight.Bold)
                    Text("${(index + 1) * 3} video", fontSize = 12.sp, color = Color.Gray)
                }
            }
        }
    }
}