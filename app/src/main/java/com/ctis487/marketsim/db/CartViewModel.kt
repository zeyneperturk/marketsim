package com.ctis487.marketsim.db

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ctis487.marketsim.model.CartItem
import com.ctis487.marketsim.db.MarketRoomDatabase
import com.ctis487.marketsim.model.Cart
import com.ctis487.marketsim.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartViewModel(application: Application): AndroidViewModel(application) {

    lateinit var cartDAO: CartDAO
    lateinit var productDAO: ProductDAO
    private val userId: Int = 1
    private val cartItemsWithProducts = MutableLiveData<List<CartItem>>()
    private val totalCost = MutableLiveData<Double>()
    private val originalTotal = MutableLiveData<Double>()
    private var selectedCoupon: com.ctis487.marketsim.model.Coupon? = null

    init {
        val database = MarketRoomDatabase.getDatabase(application)
        cartDAO = database.CartDAO()
        productDAO = database.ProductDAO()
    }

    fun addToCart(product: Product, quantity: Int = 1) {
        viewModelScope.launch(Dispatchers.IO) {
            productDAO.insertAll(listOf(product))
            val existingCartItem = cartDAO.getCartItem(userId, product.pid)
            
            var finalQuantity = quantity
            if (existingCartItem != null) {
                finalQuantity = existingCartItem.quantity + quantity
            }
            
            val cartItem = Cart(userId, product.pid, finalQuantity)
            cartDAO.add(cartItem)
            updateCartItems()
        }
    }

    fun removeFromCart(pid: Int, onResult: (Int) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val numberOfDeletedRecords = cartDAO.remove(userId, pid)
            withContext(Dispatchers.Main) {
                onResult(numberOfDeletedRecords)
            }
            updateCartItems()
        }
    }

    fun setQuantity(product: Product, quantity: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            productDAO.insertAll(listOf(product))
            val cartItem = Cart(userId, product.pid, quantity)
            cartDAO.add(cartItem)
            updateCartItems()
        }
    }

    fun decreaseQuantity(product: Product, currentQuantity: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            if (currentQuantity > 1) {
                val newQuantity = currentQuantity - 1
                setQuantity(product, newQuantity)
            } else {
                cartDAO.remove(userId, product.pid)
                updateCartItems()
            }
        }
    }

    fun getCartItemsWithProducts(): LiveData<List<CartItem>> {
        viewModelScope.launch(Dispatchers.IO) {
            updateCartItems()
        }
        return cartItemsWithProducts
    }

    fun getTotalCost(): LiveData<Double> {
        viewModelScope.launch(Dispatchers.IO) {
            updateCartItems()
        }
        return totalCost
    }

    fun getOriginalTotal(): LiveData<Double> {
        viewModelScope.launch(Dispatchers.IO) {
            updateCartItems()
        }
        return originalTotal
    }

    private fun updateCartItems() {
        viewModelScope.launch(Dispatchers.IO) {
            val carts = cartDAO.getCart(userId)
            val cartItemsList = mutableListOf<CartItem>()
            
            for (cart in carts) {
                val product = productDAO.getById(cart.pid)
                if (product != null) {
                    cartItemsList.add(CartItem(cart, product))
                }
            }
            
            var subtotal = 0.0
            for (cartItem in cartItemsList) {
                subtotal += cartItem.product.price * cartItem.cart.quantity
            }
            
            var finalTotal = subtotal
            if (selectedCoupon != null) {
                val discountAmount = subtotal * (selectedCoupon!!.discount / 100.0)
                finalTotal = subtotal - discountAmount
            }
            
            withContext(Dispatchers.Main) {
                cartItemsWithProducts.value = cartItemsList
                originalTotal.value = subtotal
                totalCost.value = finalTotal
            }
        }
    }
    
    fun applyCoupon(coupon: com.ctis487.marketsim.model.Coupon?) {
        selectedCoupon = coupon
        updateCartItems()
    }
    
    fun getSelectedCoupon(): com.ctis487.marketsim.model.Coupon? {
        return selectedCoupon
    }

}