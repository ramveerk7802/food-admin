package com.example.foodadmin

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.foodadmin.Activity.AddItemActivity
import com.example.foodadmin.Activity.AdminProfileActivity
import com.example.foodadmin.Activity.AllItemActivity
import com.example.foodadmin.Activity.CreateUserActivity
import com.example.foodadmin.Activity.PendingOrderActivity
import com.example.foodadmin.OtherClass.OrderDetail
import com.example.foodadmin.Sign.SignInActivity
import com.example.foodadmin.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database:DatabaseReference
    private lateinit var dialog: Dialog

    private  val binding:ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()
        database = Firebase.database.reference

        if(auth.currentUser==null){
            startActivity(Intent(this,SignInActivity::class.java))
            finish()

        }

        // setup the dialog box
        dialog = Dialog(this)
        dialog.setContentView(R.layout.processing_box_layout)
        dialog.setCancelable(false)
        dialog.show()




        lifecycleScope.launch (Dispatchers.IO){
            var n = fetChDataAndSetupRecycle()

            lifecycleScope.launch (Dispatchers.Main){
                binding.pendingOrderQuantity.text = n.toString()
                dialog.dismiss()

                binding.addItemButton.setOnClickListener {
                    startActivity(Intent(this@MainActivity,AddItemActivity::class.java))
                }
                binding.allItemButton.setOnClickListener {
                    startActivity(Intent(this@MainActivity,AllItemActivity::class.java))
                }
                binding.profileButton.setOnClickListener {
                    startActivity(Intent(this@MainActivity,AdminProfileActivity::class.java))
                }
                binding.newUserButton.setOnClickListener {
                    startActivity(Intent(this@MainActivity,CreateUserActivity::class.java))
                }
                binding.pendingOrderTextView.setOnClickListener {
                    val intent = Intent(this@MainActivity,PendingOrderActivity::class.java)
                    startActivity(intent)
                }

            }
        }


    }


    private suspend fun fetChDataAndSetupRecycle() :Int{
        var n=0;
        val job = lifecycleScope.launch {
            val snapshot = database.child("OrderDetail").get().await()
            for(x in snapshot.children){
                val item = x.getValue(OrderDetail::class.java)
                item?.let {
                    n++
                }
            }

        }
        job.join()
//        Log.d("Pending",listOfOrder.size.toString())
    return n




    }
}