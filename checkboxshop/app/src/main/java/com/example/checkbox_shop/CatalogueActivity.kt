package com.example.checkbox_shop

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.checkbox_shop.adapters.CheckedItemsAdapter
import com.example.checkbox_shop.interfaces.OnCheckedChangeListener
import com.example.checkbox_shop.models.ItemData

private const val SIZE_OF_ARR = 25

class CatalogueActivity : AppCompatActivity(), OnCheckedChangeListener, OnClickListener {

    private lateinit var catalogue: ListView
    private var arrItems: ArrayList<ItemData> = ArrayList()
    private lateinit var itemsAdapter : CheckedItemsAdapter
    private lateinit var checkedCounter : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalogue)

        fillData(savedInstanceState)

        initView()
        createCatalogueView()

        checkedCounter = catalogue.findViewById(R.id.cart_count)

        catalogue.findViewById<Button>(R.id.to_cart).setOnClickListener(this)

        //Call onDataChanged after page init
        onDataChanged()
    }

    private fun initView() {
        catalogue = findViewById<View>(R.id.catalogue) as ListView
    }

    private fun createCatalogueView() {
        //Get and set adapter for catalogue listView
        itemsAdapter = CheckedItemsAdapter(this, arrItems, this)
        catalogue.adapter = itemsAdapter

        //Inflate footer and header with layoutInflater
        val layoutInflater: LayoutInflater = LayoutInflater.from(this)
        insertHeader(layoutInflater)
        insertFooter(layoutInflater)

    }

    private fun insertHeader(layoutInflater: LayoutInflater) {
        val catalogueHeader = layoutInflater.inflate(R.layout.header, catalogue, false)
        catalogue.addHeaderView(catalogueHeader)
    }
    private fun insertFooter(layoutInflater: LayoutInflater) {
        val catalogueFooter = layoutInflater.inflate(R.layout.footer, catalogue, false)
        catalogue.addFooterView(catalogueFooter, "Footer", false)
    }

    //Initialize catalogue items
    @Suppress("DEPRECATION")
    private fun fillData(saveItems : Bundle?) {

        val savedItems = saveItems?.getParcelableArrayList<ItemData>("items")
        if(savedItems != null)
        {
            arrItems = savedItems
            return
        }

        if(arrItems.size != 0) return
        var i = 0
        while (i < SIZE_OF_ARR) {
            i++
            arrItems.add(ItemData(i, " Catalogue item No$i", false))
        }
    }

    //Called when some item checked is changed
    override fun onDataChanged() {

        val checkedCount = itemsAdapter.getCheckedItems().size
        checkedCounter.text = checkedCount.toString()

    }

    //Move to cart activity
    override fun onClick(v: View?) {

        val intent = Intent(this, CartActivity::class.java)

        intent.putParcelableArrayListExtra("items", itemsAdapter.getCheckedItems())

        startActivity(intent)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList("items", itemsAdapter.getItemsList())
    }
}