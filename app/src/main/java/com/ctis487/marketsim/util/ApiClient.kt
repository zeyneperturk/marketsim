package com.ctis487.marketsim.db

import com.ctis487.marketsim.util.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private var retrofit: Retrofit? = null

    fun getClient(): Retrofit {
        if (retrofit == null)
            retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                //retrofit will understand as a converter GSON converter will be used
                .build()

        return retrofit as Retrofit
    }
}