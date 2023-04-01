package com.example.practica9

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @GET("/users")
    fun getData() : Call<List<UsersItem>>

    @GET("/users/{username}")
    fun getUser(@Path("username") username: String) : Call<UsersItem>
}