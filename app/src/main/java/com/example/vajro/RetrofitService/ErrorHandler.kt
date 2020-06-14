package com.example.vajro.RetrofitService

import android.content.Context
import com.example.vajro.R
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject

object ErrorHandler {

    fun processError(context: Context, statusCode: Int, body: ResponseBody?) {
        var message: String? = null
        try {
            val jsonObject = JSONObject(body?.string())
            when {
                jsonObject.has("message") -> message = jsonObject.getString("message")
                else -> (0 until jsonObject.length())
                        .map { JSONArray(jsonObject.getString(jsonObject.names().getString(it))) }
                        .forEach { message = it.getString(0) }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        when (statusCode) {
            400 -> message?.toast(context)
           /* 403 -> {
                message?.toast(context)
                preference.clearUser()
                MyActivity.launchClearStack(context, SplashActivity::class)
            }*/
            404 -> {
                if (message.isNullOrBlank()) "There\'s nothing in here".toast(context)
                else message?.toast(context)
            }
            500 -> {
                if (message.isNullOrBlank()) context.getString(R.string.error_500).toast(context)
                else message?.toast(context)
            }
            else -> context.getString(R.string.error_failure).toast(context)
        }
    }

}