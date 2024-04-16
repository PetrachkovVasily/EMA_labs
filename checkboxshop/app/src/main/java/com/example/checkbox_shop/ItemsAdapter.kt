package com.example.checkbox_shop

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView

class ItemsAdapter (private val context: Context, private val arrGoodsAdapter: ArrayList<Item>) :
    BaseAdapter(), CompoundButton.OnCheckedChangeListener {

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return arrGoodsAdapter.size
    }

    override fun getItem(position: Int): Any {
        return arrGoodsAdapter[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
        var view = convertView
        if (view == null) {
            view = layoutInflater.inflate(R.layout.catalogue_item, viewGroup, false)
        }
        val goodTemp = arrGoodsAdapter[position]
        val tvGoodId = view!!.findViewById<TextView>(R.id.item_id)
        tvGoodId.text = goodTemp.id.toString()
        val tvGoodName = view.findViewById<TextView>(R.id.item_name)
        tvGoodName.text = goodTemp.name
        val cbGood = view.findViewById<CheckBox>(R.id.checkbox_add_to_cart)
        cbGood.isChecked = goodTemp.check
        cbGood.tag = position
        cbGood.setOnCheckedChangeListener(this)
        return view
    }

    override fun onCheckedChanged(compoundButton: CompoundButton?, isChecked: Boolean) {
        if (compoundButton != null && compoundButton.isShown) {
            val i = compoundButton.tag as Int
            arrGoodsAdapter[i].check = isChecked
            notifyDataSetChanged()
        }
    }
}


