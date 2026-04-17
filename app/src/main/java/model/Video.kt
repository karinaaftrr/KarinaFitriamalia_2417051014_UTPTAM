package com.example.karinafitriamalia_2417051014_utptam.model
import androidx.annotation.DrawableRes

data class Video(
    val id: String,
    val title: String,
    val channelName: String,
    val views: String,
    val duration: String,
    @DrawableRes val imageRes: Int,
    val isLive: Boolean = false
)