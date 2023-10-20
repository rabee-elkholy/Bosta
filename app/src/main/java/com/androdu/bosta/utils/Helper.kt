package com.androdu.bosta.utils

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import com.androdu.bosta.R
import com.labters.lottiealertdialoglibrary.ClickListener
import com.labters.lottiealertdialoglibrary.DialogTypes
import com.labters.lottiealertdialoglibrary.LottieAlertDialog
import retrofit2.Response
import java.io.Serializable
import java.net.HttpURLConnection

object Helper {
    fun showErrorDialog(
        context: Context,
        title: String = context.getString(R.string.error),
        desc: String,
        onClick: () -> Unit = {}
    ) {
        val dialog = LottieAlertDialog.Builder(context, DialogTypes.TYPE_ERROR)
            .setTitle(title)
            .setDescription(desc)
            .setPositiveText(context.getString(R.string.ok))
            .setPositiveButtonColor(Color.parseColor("#f44242"))
            .setPositiveTextColor(Color.parseColor("#ffffff"))
            .setPositiveListener(object : ClickListener {
                override fun onClick(dialog: LottieAlertDialog) {
                    dialog.dismiss()
                    onClick()
                }
            })
            .build()
        dialog.setCancelable(false)
        dialog.show()
    }

    fun <T> handleResponse(context: Context, response: Response<T>): ApiResult<T> {
        when {
            response.code() == HttpURLConnection.HTTP_CLIENT_TIMEOUT -> {
                return ApiResult.Error(
                    message = context.getString(R.string.connection_timeout)
                )
            }

            response.code() == HttpURLConnection.HTTP_NOT_FOUND -> {
                return ApiResult.Error(
                    message = context.getString(R.string.not_found)
                )
            }

            response.isSuccessful -> {
                val data = response.body()
                Log.d(
                    "Api",
                    "handleResponse: isSuccessful: ${data.toString()}"
                )
                return ApiResult.Success(data = data)
            }

            else -> {
                Log.d(
                    "Api",
                    "handleResponse: unknown " + response.errorBody().toString()
                )
                return ApiResult.Error(
                    message = context.getString(R.string.unknown_error)
                )
            }
        }
    }

    inline fun <reified T : java.io.Serializable> Bundle.serializable(key: String): T? = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializable(key, T::class.java)
        else -> @Suppress("DEPRECATION") getSerializable(key) as? T
    }

    inline fun <reified T : Serializable> Intent.serializable(key: String): T? = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializableExtra(key, T::class.java)
        else -> @Suppress("DEPRECATION") getSerializableExtra(key) as? T
    }
}