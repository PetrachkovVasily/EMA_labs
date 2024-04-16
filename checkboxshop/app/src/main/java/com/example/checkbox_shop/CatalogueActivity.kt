package com.example.checkbox_shop

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


private var catalogue: ListView? = null
private val arr_items: ArrayList<Item> = ArrayList()
private const val SIZE_OF_ARR = 25

class CatalogueActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalogue)

        initView();
        createCatalogueView();
    }

    private fun initView() {
        catalogue = findViewById<View>(R.id.catalogue) as ListView
    }

    private fun createCatalogueView() {
        fillData()
        val itemsAdapter = ItemsAdapter(this, arr_items)

        val layoutInflater: LayoutInflater = LayoutInflater.from(this);

        insertHeader(layoutInflater)
        insertFooter(layoutInflater)

        catalogue?.adapter = itemsAdapter
    }

    private fun insertHeader(layoutInflater: LayoutInflater) {
        val catalogueHeader = layoutInflater.inflate(R.layout.header, null)
        catalogue?.addHeaderView(catalogueHeader)
    }
    private fun insertFooter(layoutInflater: LayoutInflater) {
        val catalogueFooter = layoutInflater.inflate(R.layout.footer, null)
        catalogue?.addFooterView(catalogueFooter)
    }

    private fun fillData() {
        var i = 0
        while (i < SIZE_OF_ARR) {
            i++
            arr_items.add(Item(i, " Catalogue item No$i", false))
        }
    }
}