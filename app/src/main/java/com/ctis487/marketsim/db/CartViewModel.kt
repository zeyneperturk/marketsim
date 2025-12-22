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

    private val cartDAO: CartDAO
    private val productDAO: ProductDAO
    private val userId: Int = 1

    private val cartItems = MutableLiveData<List<Cart>>()
    private val cartItemsWithProducts = MutableLiveData<List<CartItem>>()
    private val totalCost = MutableLiveData<Double>()

    init {
        val database = MarketRoomDatabase.getDatabase(application)
        cartDAO = database.CartDAO()
        productDAO = database.ProductDAO()
    }

    fun addToCart(product: Product, quantity: Int = 1) {
        viewModelScope.launch(Dispatchers.IO) {
            // Save product to database first (in case it's not already there)
            productDAO.insertAll(listOf(product))
            
            // Check if item already exists in cart
            val existingCartItem = cartDAO.getCartItem(userId, product.pid)
            
            val finalQuantity = if (existingCartItem != null) {
                // If item exists, increment the quantity
                existingCartItem.quantity + quantity
            } else {
                // If item doesn't exist, use the specified quantity
                quantity
            }
            
            val cartItem = Cart(userId, product.pid, finalQuantity)
            cartDAO.add(cartItem)
            val items = cartDAO.getCart(userId)
            withContext(Dispatchers.Main) {
                cartItems.value = items
            }
            // Also update cart items with products
            updateCartItemsWithProducts()
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
            // Also update cart items with products
            updateCartItemsWithProducts()
        }
    }

    fun setQuantity(product: Product, quantity: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            // Save product to database first (in case it's not already there)
            productDAO.insertAll(listOf(product))
            
            // Set the exact quantity (don't increment)
            val cartItem = Cart(userId, product.pid, quantity)
            cartDAO.add(cartItem)
            val items = cartDAO.getCart(userId)
            withContext(Dispatchers.Main) {
                cartItems.value = items
            }
            // Also update cart items with products
            updateCartItemsWithProducts()
        }
    }

    fun decreaseQuantity(product: Product, currentQuantity: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            if (currentQuantity > 1) {
                // Decrease quantity by 1 - use setQuantity to set exact value
                val newQuantity = currentQuantity - 1
                setQuantity(product, newQuantity)
            } else {
                // Remove item if quantity is 1
                cartDAO.remove(userId, product.pid)
                val items = cartDAO.getCart(userId)
                withContext(Dispatchers.Main) {
                    cartItems.value = items
                }
                updateCartItemsWithProducts()
            }
        }
    }

    fun getCartItemsWithProducts(): LiveData<List<CartItem>> {
        viewModelScope.launch(Dispatchers.IO) {
            updateCartItemsWithProducts()
        }
        return cartItemsWithProducts
    }

    private suspend fun updateCartItemsWithProducts() {
        val carts = cartDAO.getCart(userId)
        val cartItemsList = carts.mapNotNull { cart ->
            val product = productDAO.getById(cart.pid)
            product?.let { CartItem(cart, it) }
        }
        withContext(Dispatchers.Main) {
            cartItemsWithProducts.value = cartItemsList
            // Calculate total cost whenever cart items update
            calculateTotalCost(cartItemsList)
        }
    }

    private fun calculateTotalCost(cartItems: List<CartItem>) {
        val total = cartItems.sumOf { cartItem ->
            cartItem.product.price * cartItem.cart.quantity
        }
        totalCost.value = total
    }

    fun getTotalCost(): LiveData<Double> {
        // Initialize total cost if not already calculated
        viewModelScope.launch(Dispatchers.IO) {
            updateCartItemsWithProducts()
        }
        return totalCost
    }

}