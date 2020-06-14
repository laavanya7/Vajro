package com.example.vajro.RetrofitService

import android.content.Context
import android.content.ContextWrapper
import android.net.ConnectivityManager
import com.example.vajro.R

class NetworkUtil(context: Context) : ContextWrapper(context) {
    companion object {
        fun isConnected(context: Context,toast:Boolean=true): Boolean {
            val manager = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            val connection = manager.activeNetworkInfo
            var result = connection != null && connection.isConnected
            if(!result && toast){
                context.getString(R.string.error_internet).toast(context)
            }
            return result
        }
    }
}