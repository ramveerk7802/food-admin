package com.example.foodadmin.Activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodadmin.Adaptor.AllItemAdaptor
import com.example.foodadmin.DataModel.ItemModel
import com.example.foodadmin.R
import com.example.foodadmin.databinding.ActivityAllItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AllItemActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityAllItemBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth
    private lateinit var databse: DatabaseReference
    private lateinit var adaptor: AllItemAdaptor
    private lateinit var itemList:MutableList<ItemModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        // initialized firebase and database
        auth = Firebase.auth
        databse = Firebase.database.reference

        itemList = mutableListOf<ItemModel>()
        adaptor = AllItemAdaptor(itemList)
        binding.itemRecycleView.layoutManager = LinearLayoutManager(this)
        binding.itemRecycleView.adapter = adaptor
        val menuReference= databse.child("AllMenu")

        fetchData(menuReference)


    }

    private fun fetchData(menuReference: DatabaseReference) {

        CoroutineScope(Dispatchers.IO).launch {
            try{
                val items = getMenuItem(menuReference)
                withContext(Dispatchers.Main){
                    itemList.clear()
                    itemList.addAll(items)
                    adaptor.notifyDataSetChanged()
                }

            }catch (e:Exception){
                Log.e("fetchData","${e.message}")
            }
        }

    }

    private suspend fun getMenuItem(menuReference: DatabaseReference): MutableList<ItemModel> =
        withContext(Dispatchers.IO){
            val snapshot = menuReference.get().await()
            val items = mutableListOf<ItemModel>()
            for(data in snapshot.children){
                val item = data.getValue(ItemModel::class.java)
                item?.let { items.add(it) }
            }
            items
        }


}