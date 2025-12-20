package com.ctis487.marketsim.db

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ctis487.marketsim.db.MarketRoomDatabase
import com.ctis487.marketsim.model.Cart
import com.ctis487.marketsim.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartViewModel(application: Application): AndroidViewModel(application) {

    private val cartDAO: CartDAO
    private val userId: Int = 1

    private val cartItems = MutableLiveData<List<Cart>>()

    init {
        cartDAO= MarketRoomDatabase.getDatabase(application).CartDAO()
    }

    fun addToCart(product: Product, quantity: Int = 1) {
        viewModelScope.launch(Dispatchers.IO) {
            val cartItem = Cart(userId, product.pid, quantity)
            cartDAO.add(cartItem)
            val items = cartDAO.getCart(userId)
            withContext(Dispatchers.Main) {
                cartItems.value = items
            }
        }
    }

    fun removeFromCart(pid: Int, onResult: (Int) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val numberOfDeletedRecords = cartDAO.remove(userId, pid)
            val items = cartDAO.getCart(userId)
            withContext(Dispatchers.Main) {
                cartItems.value = items
                onResult(numberOfDeletedRecords)
            }
        }
    }



}