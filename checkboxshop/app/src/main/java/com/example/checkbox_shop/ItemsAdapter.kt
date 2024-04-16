package com.example.checkbox_shop

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView

class ItemsAdapter (context: Context, private val arrItemsAdapter: ArrayList<Item>) :
    BaseAdapter() {

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return arrItemsAdapter.size
    }

    override fun getItem(position: Int): Any {
        return arrItemsAdapter[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
        var view = convertView

        if (view == null) {
            view = layoutInflater.inflate(R.layout.catalogue_item, viewGroup, false)
        }


        return  createItem(position, view!!)
    }

    @SuppressLint("ResourceType")
    private fun createItem(position: Int, view: View):View {
        val itemTemp = arrItemsAdapter[position]

        val itemId = view.findViewById<TextView>(R.id.item_id)
        itemId.text = itemTemp.id.toString()

        val itemName = view.findViewById<TextView>(R.id.item_name)
        itemName.text = itemTemp.name

        val itemIsChecked = view.findViewById<CheckBox>(R.id.checkbox_add_to_cart)
        itemIsChecked.isChecked = itemTemp.check
        itemIsChecked.tag = position

//        itemIsChecked.setOnCheckedChangeListener { buttonView, isChecked ->
//            val position = buttonView.tag as Int
//            arrItemsAdapter[position].check = isChecked
//            val footerCounter:TextView = view.findViewById(R.layout.catalogue_item)
//            updateCartCount(footerCounter)
//        }

        return view
    }

    private fun updateCartCount(footerCounter: TextView) {
        val countChecked = arrItemsAdapter.count { it.check }
        footerCounter.text = "Товаров в корзине: $countChecked"
    }

    fun onCheckedChanged(compoundButton: CompoundButton?, isChecked: Boolean) {
        if (compoundButton != null && compoundButton.isShown) {
            val i = compoundButton.tag as Int
            arrItemsAdapter[i].check = isChecked
            notifyDataSetChanged()
        }
    }
}


