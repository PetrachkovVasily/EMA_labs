package com.example.checkbox_shop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ListView
import android.widget.TextView
import com.example.checkbox_shop.adapters.ItemsAdapter
import com.example.checkbox_shop.models.ItemData


class CartActivity : AppCompatActivity() {

    private var cartItems : ArrayList<ItemData> = arrayListOf()
    private lateinit var cart: ListView

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        //Retrieve items from parcel
        cartItems = intent.getParcelableArrayListExtra<ItemData>("items")!!
        cartItems.sortBy { it.id }

        initView()
        createCatalogueView()

        cart.findViewById<TextView>(R.id.items_counter).text = cartItems.size.toString()
    }

    private fun initView() {
        cart = findViewById<View>(R.id.cart) as ListView
    }

    private fun createCatalogueView() {
        //Get and set adapter for catalogue listView
        val itemsAdapter = ItemsAdapter(this, cartItems)
        cart.adapter = itemsAdapter

        //Inflate cart header with layoutInflater
        val layoutInflater: LayoutInflater = LayoutInflater.from(this)
        insertHeader(layoutInflater)


    }
    private fun insertHeader(layoutInflater: LayoutInflater) {
        val catalogueHeader = layoutInflater.inflate(R.layout.cart_header, cart, false)
        cart.addHeaderView(catalogueHeader, "Header", false)
    }
}