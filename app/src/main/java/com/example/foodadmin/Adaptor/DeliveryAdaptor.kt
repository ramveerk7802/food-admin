package com.example.foodadmin.Adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodadmin.DataModel.ItemModel
import com.example.foodadmin.databinding.DeliveryLayoutBinding

class DeliveryAdaptor(private val list :MutableList<ItemModel>) : RecyclerView.Adapter<DeliveryAdaptor.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val inflater = LayoutInflater.from(parent.context)
        val binding = DeliveryLayoutBinding.inflate(inflater,parent,false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int {
        return list.size
    }


    inner class ViewHolder (private val binding: DeliveryLayoutBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(){

        }
    }


}