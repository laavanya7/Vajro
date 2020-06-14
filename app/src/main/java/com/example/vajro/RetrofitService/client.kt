package com.example.vajro.RetrofitService

import com.example.vajro.BuildConfig
import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit

object client {

    private const val HOSTURL = "https://www.mocky.io/"

    val gson = GsonBuilder()
        .setLenient()
        .create()


    val retrofit = Retrofit.Builder()
        .baseUrl(HOSTURL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(getClient())
        .build()

    private fun getClient(): OkHttpClient {



        val client: OkHttpClient?
        client = OkHttpClient.Builder()
            .addNetworkInterceptor(object : Interceptor {

                private val FOLLOW_REDIRECT = "follow-redirect"
                val FOLLOW_HEADER = "$FOLLOW_REDIRECT:true"
                private val LOCATION = "Location"

                override fun intercept(chain: Interceptor.Chain): Response {

                    var request = chain.request()
                    val shouldRedirect = request.header(FOLLOW_REDIRECT) != null
                    if (shouldRedirect) {
                        request = request.newBuilder().removeHeader(FOLLOW_REDIRECT).build()
                    }
                    val response = chain.proceed(request)
                    return if (response.code() == HttpURLConnection.HTTP_CREATED && response.header(LOCATION) != null) {
                        response.newBuilder().code(HttpURLConnection.HTTP_SEE_OTHER).build()
                    } else response
                }

            })
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE })
            .connectTimeout(15, TimeUnit.SECONDS)
            .build()
        return client
    }

   val apiInterface = retrofit.create(ApiInterface::class.java) as ApiInterface



}