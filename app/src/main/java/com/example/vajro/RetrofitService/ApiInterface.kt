package com.example.vajro.RetrofitService

import com.example.vajro.model.Products
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("v2/5def7b172f000063008e0aa2")
    fun getProducts(): Single<Products>

}