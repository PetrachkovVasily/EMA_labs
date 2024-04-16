package com.example.checkbox_shop

import android.os.Bundle
import android.view.View
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity


private var catalogue: ListView? = null
private val arr_items: ArrayList<Item> = ArrayList()
private const val SIZE_OF_ARR = 25

class CatalogueActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalogue)

        initView();
        createMyListView();
    }

    private fun initView() {
        catalogue = findViewById<View>(R.id.catalogue) as ListView
    }

    private fun createMyListView() {
        fillData()
        val itemsAdapter = ItemsAdapter(this, arr_items)
        catalogue?.adapter = itemsAdapter
    }

    private fun fillData() {
        var i = 0
        while (i < SIZE_OF_ARR) {
            i++
            arr_items.add(Item(i, " Catalogue item No$i", false))
        }
    }
}