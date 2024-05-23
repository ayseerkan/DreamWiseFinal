package com.example.dreamwise

import com.google.gson.annotations.SerializedName

data class DreamResponse(
    @SerializedName("dreams") val dreams: List<DreamData>
)