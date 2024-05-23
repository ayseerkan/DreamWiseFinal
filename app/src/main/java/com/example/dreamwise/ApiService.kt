package com.example.dreamwise

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("b/EIN4")  // Update this with your actual endpoint
    fun getDreams(): Call<DreamResponse>
}