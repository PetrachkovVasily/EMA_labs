package com.example.checkbox_shop.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.checkbox_shop.models.ItemData
import com.example.checkbox_shop.R

open class ItemsAdapter(context: Context, protected val items: ArrayList<ItemData>) : BaseAdapter() {

    protected val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    //Count of items in dataset
    override fun getCount(): Int {
        return items.size
    }

    //Get item from item position in dataset
    override fun getItem(position: Int): ItemData {
        return items[position]
    }

    //Get item id from item position in dataset
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView

        if (view == null) {
            //convertView - old view to reuse (can be hidden by something like scroll.
            //If it's null - creating new View
            view = layoutInflater.inflate(R.layout.cart_item, parent, false)
        }

        return  fillItem(position, view!!)
    }

    //Fill view for item with its data
    @SuppressLint("ResourceType")
    private fun fillItem(position: Int, view: View) : View {
        val itemTemp = items[position]

        val itemId = view.findViewById<TextView>(R.id.item_id)
        itemId.text = itemTemp.id.toString()

        val itemName = view.findViewById<TextView>(R.id.item_name)
        itemName.text = itemTemp.name

        return view
    }

    fun getItemsList() : ArrayList<ItemData>
    {
        return items
    }
}