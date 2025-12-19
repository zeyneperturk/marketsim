package com.ctis487.marketsim.db

import com.ctis487.marketsim.model.Product
import retrofit2.Call
import retrofit2.http.GET

interface ProductService {
    @GET("products")
    fun getProduct(): Call<List<Product>>
}