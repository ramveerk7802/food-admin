package com.example.foodadmin.Activity

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodadmin.Adaptor.PendingOrderAdaptor
import com.example.foodadmin.OtherClass.OrderDetail
import com.example.foodadmin.R
import com.example.foodadmin.databinding.ActivityPendingOrderBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.childEvents
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PendingOrderActivity : AppCompatActivity() {
    private val binding:ActivityPendingOrderBinding by lazy {
        ActivityPendingOrderBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth
    private lateinit var database:DatabaseReference
    private  val itemImage:MutableList<String> = mutableListOf()
    private  var itemQuantity:MutableList<Int> = mutableListOf()
    private  val customerName:MutableList<String> = mutableListOf()
    private var totalAmountList:MutableList<Int> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // initialize firebase
        auth = Firebase.auth
        database = Firebase.database.reference



        lifecycleScope.launch (Dispatchers.IO) {
            fetChDataAndSetupRecycle()
        }

    }



    private fun setUpRecycleView(listOfOrder: MutableList<OrderDetail>) {
        val adaptor = PendingOrderAdaptor(listOfOrder)
        binding.pendingRecycleView.layoutManager = LinearLayoutManager(this)
        binding.pendingRecycleView.adapter=adaptor
    }

    private suspend fun fetChDataAndSetupRecycle() {
        val listOfOrder = mutableListOf<OrderDetail>()
        val job = lifecycleScope.launch {
            val snapshot = database.child("OrderDetail").get().await()
            for(x in snapshot.children){
                val item = x.getValue(OrderDetail::class.java)
                item?.let {
                    listOfOrder.add(it)
                }
            }

        }
        job.join()
        Log.d("Pending",listOfOrder.size.toString())
        lifecycleScope.launch (Dispatchers.Main){
            binding.progressBar.visibility=View.GONE
            binding.divider.visibility=View.VISIBLE
            setUpRecycleView(listOfOrder)
        }


    }
}