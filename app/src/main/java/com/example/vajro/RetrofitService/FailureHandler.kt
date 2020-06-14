package com.example.vajro

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.vajro.RetrofitService.ErrorHandler
import com.example.vajro.RetrofitService.NetworkUtil
import com.example.vajro.RetrofitService.toast
import retrofit2.HttpException
import java.net.SocketException

object FailureHandler {
    fun processFailure(context: Context, t: Throwable?) {
        when {
            !NetworkUtil.isConnected(context) -> context.getString(R.string.error_connection).toast(context)

            t is HttpException -> {

                ErrorHandler.processError(context, t.response().code(), t.response().errorBody())

                Log.d("response code", t.response().code().toString())
                Log.d("response body", t.response().errorBody().toString())
            }
            t is SocketException -> {

                context.getString(R.string.error_server).toast(context)
            }
            else -> {
                Toast.makeText(context, context.getString(R.string.error_failure), Toast.LENGTH_SHORT).show()
                Log.d("error", t.toString())
            }

        }
    }
}