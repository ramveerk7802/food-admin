package com.example.foodadmin

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.foodadmin.Activity.AddItemActivity
import com.example.foodadmin.Activity.AdminProfileActivity
import com.example.foodadmin.Activity.AllItemActivity
import com.example.foodadmin.Activity.CreateUserActivity
import com.example.foodadmin.Activity.PendingOrderActivity
import com.example.foodadmin.Sign.SignInActivity
import com.example.foodadmin.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

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

        if(auth.currentUser==null){
            startActivity(Intent(this,SignInActivity::class.java))
            finish()

        }
        binding.addItemButton.setOnClickListener {
            startActivity(Intent(this,AddItemActivity::class.java))
        }
        binding.allItemButton.setOnClickListener {
            startActivity(Intent(this,AllItemActivity::class.java))
        }
        binding.profileButton.setOnClickListener {
            startActivity(Intent(this,AdminProfileActivity::class.java))
        }
        binding.newUserButton.setOnClickListener {
            startActivity(Intent(this,CreateUserActivity::class.java))
        }
        binding.pendingOrderTextView.setOnClickListener {
            startActivity(Intent(this,PendingOrderActivity::class.java))
        }
    }
}