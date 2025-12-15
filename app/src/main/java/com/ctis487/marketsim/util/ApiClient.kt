package com.ctis487.retrofit

import com.ctis487.marketsim.util.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
//STEP1
object ApiClient {

    private var retrofit: Retrofit? = null

    fun getClient(): Retrofit {
        if (retrofit == null)
            retrofit = Retrofit.Builder()
                    .baseUrl(Constants.baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    //retrofit will understand as a converter GSON converter will be used
                    .build()

        return retrofit as Retrofit
    }
}