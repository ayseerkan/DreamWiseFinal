package com.example.dreamwise

import com.google.gson.annotations.SerializedName

data class DreamData(
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("isNightmare") val isNightmare: Boolean,
    @SerializedName("date") val date: Long
)