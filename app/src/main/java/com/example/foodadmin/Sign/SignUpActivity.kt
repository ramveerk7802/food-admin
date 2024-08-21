package com.example.foodadmin.Sign

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.foodadmin.R
import com.example.foodadmin.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.alreadyText.setOnClickListener {
            startActivity(Intent(this@SignUpActivity,SignInActivity::class.java))
            finish()
        }

        // set the location list
        val locationList = arrayOf("Uttar Pradesh","Delhi","Haryana","Punjab","Chandigarh","Gujarat","Rajasthan","Karnataka","Tamil Nadu","Kerala","Goa")
        val adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,locationList)
        binding.autoCompleteTextView.setAdapter(adapter)
        binding.autoCompleteTextView.setOnTouchListener { view: View, motionEvent: MotionEvent ->
            binding.autoCompleteTextView.showDropDown()
            false
        }



    }
}