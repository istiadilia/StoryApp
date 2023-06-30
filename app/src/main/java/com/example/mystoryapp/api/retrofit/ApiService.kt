package com.example.mystoryapp.api.retrofit

import com.example.mystoryapp.api.response.AddStoryResponse
import com.example.mystoryapp.api.response.LoginResponse
import com.example.mystoryapp.api.response.RegisterResponse
import com.example.mystoryapp.api.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    fun registerUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ) : Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ) : Call<LoginResponse>

    @GET("stories")
    fun getStories(@Header("Authorization") token: String) : Call<StoryResponse>

    @Multipart
    @POST("stories")
    fun addStoryUser(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Header("Authorization") token: String
    ) : Call<AddStoryResponse>
}