package com.celar.celvisitas.interfaces

import android.util.Log
import com.celar.celvisitas.Session.SessionManager
import com.celar.celvisitas.Tools.AppConfig
import com.celar.celvisitas.models.Login
import com.celar.celvisitas.models.Visit
import com.celar.celvisitas.models.VisitAllowedOrReject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import com.google.gson.GsonBuilder
import retrofit2.converter.fastjson.FastJsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import retrofit2.http.*


public interface VisitAPI {

    var sessionManager: SessionManager

    @GET("visits/avalibles")
    fun getVisits(): Call<Visit>

    @GET("user")
    fun getUser(): Call<VisitAllowedOrReject>

    @PUT("visits/{id}")
    fun allowOrReject(@Path("id") requestId:String,@Body requestBody: RequestBody ): Call<VisitAllowedOrReject>

    @PUT("visits/out/{id}")
    fun outVisit(@Path("id") requestId:String ): Call<VisitAllowedOrReject>

    @PUT("user/notification")
    fun notificationPutId(@Body requestBody: RequestBody ): Call<VisitAllowedOrReject>

    companion object {
        var client = OkHttpClient.Builder().addInterceptor { chain ->
            val newRequest: Request = chain.request().newBuilder()
                .addHeader("Authorization", AppConfig.token)
                .build()
            chain.proceed(newRequest)
        }.build()

        fun create(): VisitAPI {
            val retrofit = Retrofit.Builder()
                .client(client)
                .baseUrl(AppConfig.REST_API)
                .addConverterFactory(FastJsonConverterFactory.create())
                .build()

            return  retrofit.create(VisitAPI::class.java)

        }


    }
}