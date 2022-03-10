package com.example.pinterest.networking

import com.mirkamol.retrofitexample.model.ResponceItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

@JvmSuppressWildcards
interface ApiService {

    @GET("photos")
    fun getPhotos(@Query("page") page:Int, @Query("per_page") per_page: Int): Call<List<ResponceItem>>

//    @GET("photos")
//    fun getPhotos(): Call<List<ResponceItem>>

    @GET("photos/{id}")
    fun getPhoto(@Path("id") id:String): Call<ResponceItem>

    //Wallpapers
    @GET("topics/wallpapers/photos")
    fun listPhotos2(): Call<ArrayList<ResponceItem>>

    //Nature
    @GET("topics/nature/photos")
    fun listPhotos1(): Call<ArrayList<ResponceItem>>
}