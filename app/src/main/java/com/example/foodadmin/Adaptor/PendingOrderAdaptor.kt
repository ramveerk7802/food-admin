package com.example.foodadmin.Adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodadmin.OtherClass.OrderDetail
import com.example.foodadmin.R
import com.example.foodadmin.databinding.PendingLayoutBinding
import com.squareup.picasso.Picasso

class PendingOrderAdaptor(private val list:MutableList<OrderDetail>) :RecyclerView.Adapter<PendingOrderAdaptor.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PendingLayoutBinding.inflate(inflater,parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }


    inner class ViewHolder(private val binding: PendingLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int) {
            if(position!=RecyclerView.NO_POSITION){
                val n = list.size
                binding.apply {
                    customerName.text= list[n-1-position].name!!
                    Picasso.get().load(list[n-1-position].itemImage!![0]).into(itemImage)
                    amount1.text = list[n-1-position].totalAmt.toString()
                    if(!list[n-1-position].orderAccepted){
                        orderAcceptButton.text = "Declined"
                    }else{
                        orderAcceptButton.text="Accept"
                    }
                    orderAcceptButton.setOnClickListener{
                        if(!list[n-1-position].orderAccepted){
                            orderAcceptButton.text= "Accept"
                            list[n-1-position].orderAccepted=true
                        }else{
                            orderAcceptButton.text ="Declined"
                            list[n-1-position].orderAccepted=false
                        }
                    }
                    root.setOnClickListener{

                    }
                }
            }
        }


    }


}