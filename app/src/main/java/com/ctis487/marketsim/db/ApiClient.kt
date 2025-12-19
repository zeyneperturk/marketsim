package com.ctis487.marketsim.db

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private var retrofit: Retrofit? = null

    fun getClient(): Retrofit {
        if (retrofit == null)
            retrofit = Retrofit.Builder()
                .baseUrl("http://56.228.81.131:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                //retrofit will understand as a converter GSON converter will be used
                .build()

        return retrofit as Retrofit
    }
}