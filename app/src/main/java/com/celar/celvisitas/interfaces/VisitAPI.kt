package com.celar.celvisitas.interfaces

import android.util.Log
import com.celar.celvisitas.Tools.AppConfig
import com.celar.celvisitas.models.Visit
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header

import com.google.gson.GsonBuilder
import retrofit2.converter.fastjson.FastJsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.Request


public interface VisitAPI {


    @GET("visits")
    fun getVisitsToken(@Header("Authorization") token: String): Call<Visit>


    @GET("visits")
    fun getVisits(): Call<Visit>

    companion object {


        var client = OkHttpClient.Builder().addInterceptor { chain ->
            val newRequest: Request = chain.request().newBuilder()
                .addHeader("Authorization", AppConfig.token)
                .build()
            chain.proceed(newRequest)
        }.build()

        fun create(): VisitAPI {
//            val gson = GsonBuilder()
//                .setLenient()
//                .create()

            val retrofit = Retrofit.Builder()
                .client(client)
                .baseUrl(AppConfig.REST_API)
                .addConverterFactory(FastJsonConverterFactory.create())
                .build()

            return  retrofit.create(VisitAPI::class.java)

        }


    }
}