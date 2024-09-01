package com.example.foodadmin.Adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodadmin.databinding.PendingLayoutBinding

class PendingOrderAdaptor(private val list:MutableList<String>) :RecyclerView.Adapter<PendingOrderAdaptor.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PendingLayoutBinding.inflate(inflater,parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }


    inner class ViewHolder(private val binding: PendingLayoutBinding):RecyclerView.ViewHolder(binding.root){


        fun bind(){
            TODO("Not yet implement")
        }
    }


}