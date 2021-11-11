package com.celar.celvisitas.interfaces

import com.celar.celvisitas.Tools.AppConfig
import com.celar.celvisitas.models.Visit
import com.celar.celvisitas.models.VisitAllowedOrReject
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.fastjson.FastJsonConverterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface RequestAllApi {

    @GET("user")
    fun getUser(): Call<VisitAllowedOrReject>

    @GET("visits/avalibles")
    fun getVisits(): Call<Visit>

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
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return  retrofit.create(VisitAPI::class.java)

        }


    }
}
