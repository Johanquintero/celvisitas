package com.celar.celvisitas.interfaces

import com.celar.celvisitas.Tools.AppConfig
import com.celar.celvisitas.models.Login
import com.celar.celvisitas.models.Visit
import com.celar.celvisitas.models.VisitAllowedOrReject
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

public interface LoginAPI {
    @GET("casas")
    fun getCasas() : Call<List<Login>>

    @POST("login")
    fun logIN(@Body requestBody: RequestBody): Call<Login>

    companion object {

        fun create(): LoginAPI {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(AppConfig.REST_API)
                .build()
            return retrofit.create(LoginAPI::class.java)

        }


    }



}
