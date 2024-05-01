package com.example.checkbox_shop.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import com.example.checkbox_shop.models.Item
import com.example.checkbox_shop.interfaces.OnCheckedChangeListener
import com.example.checkbox_shop.R

class CheckedItemsAdapter(
    context: Context,
    items: ArrayList<Item>,
    private val checkedChangedListener: OnCheckedChangeListener
) : ItemsAdapter(context, items), CompoundButton.OnCheckedChangeListener {

    private var checkedItems : ArrayList<Item>

    init {
        checkedItems = ArrayList( items.filter { it.check } )
    }

    //Get UI element for item in certain position in dataset
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView

        if (view == null) {
            //convertView - old view to reuse (can be hidden by something like scroll.
            //If it's null - creating new View
            view = layoutInflater.inflate(R.layout.catalogue_item, parent, false)
        }

        return  fillItem(position, view!!)
    }

    //Return all checked items of dataset
    fun getCheckedItems() : ArrayList<Item>
    {
        return checkedItems
    }

    //Fill view for item with its data
    @SuppressLint("ResourceType")
    private fun fillItem(position: Int, view: View) : View {
        val itemTemp = items[position]

        val itemId = view.findViewById<TextView>(R.id.item_id)
        itemId.text = itemTemp.id.toString()

        val itemName = view.findViewById<TextView>(R.id.item_name)
        itemName.text = itemTemp.name

        val itemIsChecked = view.findViewById<CheckBox>(R.id.checkbox_add_to_cart)
        itemIsChecked.isChecked = itemTemp.check
        itemIsChecked.tag = position

        itemIsChecked.setOnCheckedChangeListener(this)

        return view
    }

    //Method called when state of compound button (checkbox) changed
    override fun onCheckedChanged(compoundButton: CompoundButton?, isChecked: Boolean) {
        if (compoundButton!!.isShown) {

            //retrieve item id from button
            val i = compoundButton.tag as Int
            items[i].check = isChecked

            //Notify observers that checkbox state changed
            notifyDataSetChanged()

            //Update checked items
            if(isChecked)
            {
                checkedItems.add(items[i])
            }
            else
            {
                checkedItems.remove(items[i])
            }

            checkedChangedListener.onDataChanged()
        }
    }
}


