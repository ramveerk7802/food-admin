package com.example.foodadmin.Adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodadmin.DataModel.ItemModel
import com.example.foodadmin.databinding.ItemLayoutBinding
import com.squareup.picasso.Picasso

class AllItemAdaptor(private val list:MutableList<ItemModel>) : RecyclerView.Adapter<AllItemAdaptor.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLayoutBinding.inflate(inflater,parent,false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return list.size
    }


    inner class ViewHolder(private val binding: ItemLayoutBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(position: Int){
            binding.apply {
                val obj = list[position]
                itemNameText.text=obj.foodName
                Picasso.get().load(obj.foodImage).into(itemImage)
                val money= "₹ "+obj.foodPrice.toString()
                price.text =money
            }
            
        }

    }
}