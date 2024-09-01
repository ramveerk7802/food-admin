package com.example.foodadmin.Activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.foodadmin.DataModel.UserModel
import com.example.foodadmin.MainActivity
import com.example.foodadmin.OtherClass.PhoneNumberChecker
import com.example.foodadmin.R
import com.example.foodadmin.Sign.SignInActivity
import com.example.foodadmin.databinding.ActivityAdminProfileBinding
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AdminProfileActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityAdminProfileBinding.inflate(layoutInflater)
    }
    private lateinit var auth:FirebaseAuth
    private lateinit var database:DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // initialized the database
        auth=Firebase.auth
        database=Firebase.database.reference

        //
        binding.adminName.isEnabled=false
        binding.adminAddress.isEnabled=false
        binding.adminEmail.isEnabled=false
        binding.adminPhone.isEnabled=false
        binding.adminPasswordLayout.visibility = GONE
        binding.editText.visibility= INVISIBLE
        binding.saveButton.visibility= GONE
        binding.restaurantName.isEnabled=false

        // fetch Admin user data
        val profileReference = database.child("AdminUser")
        profileReference.child(auth.currentUser?.uid!!)
            .addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val model = snapshot.getValue(UserModel::class.java)
                    binding.adminEmail.setText(model?.email)
                    binding.adminName.setText(model?.ownerName)
                    binding.adminPhone.setText(model?.phone)
                    binding.adminAddress.setText(model?.address)
                    binding.restaurantName.setText(model?.restaurantName)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })




        binding.editButton.setOnClickListener{
            binding.adminName.isEnabled=true
            binding.adminAddress.isEnabled=true
            binding.adminPhone.isEnabled=true
            binding.editText.visibility=VISIBLE
            binding.editButton.visibility=INVISIBLE
            binding.saveButton.visibility=VISIBLE
            binding.restaurantNameLayout.visibility= GONE
        }

        // handle from registration of new Admin
        val intent = intent
        if(intent!=null && intent.getBooleanExtra("fromComplete",false)){
            binding.editText.visibility= INVISIBLE
            binding.editButton.visibility= INVISIBLE
            binding.adminEmail.isEnabled=false
            binding.adminPassword.isEnabled=false
            binding.saveButton.visibility=VISIBLE
            binding.adminAddress.isEnabled=true
            binding.adminName.isEnabled=false
            binding.adminPhone.isEnabled=true
            binding.restaurantNameLayout.visibility= GONE
            binding.adminPassword.isEnabled=false
            binding.adminPasswordLayout.endIconMode= TextInputLayout.END_ICON_NONE


            val ownerName = intent.getStringExtra("ownerName")
            val location = intent.getStringExtra("location")
            val hashedPassword = intent.getStringExtra("hashedPassword")
            val restaurantName = intent.getStringExtra("restaurantName")
            val email = intent.getStringExtra("email")
            binding.adminEmail.setText(email)
            binding.adminPassword.setText(hashedPassword)
            binding.adminName.setText(ownerName)

            // handle the address button
            binding.saveButton.setOnClickListener {
                val mobileNoChecker = PhoneNumberChecker()
                val address = binding.adminAddress.text.toString().trim()
                val phoneNo = binding.adminPhone.text.toString().trim()

                if(address.isBlank() || phoneNo.isBlank()){
                    Toast.makeText(this,"All detail mandatory !!",Toast.LENGTH_SHORT).show()
                }else if(!mobileNoChecker.phoneNumberValidation(phoneNo)){
                    Toast.makeText(this,"Enter the valid Phone Number !!",Toast.LENGTH_SHORT).show()
                } else{
                    val userId = auth.currentUser?.uid
                    val model = UserModel(ownerName,address,location,restaurantName,email,phoneNo,hashedPassword,userId)
                    val userReference = database.child("AdminUser").child(userId!!)
                    userReference.setValue(model)
                        .addOnSuccessListener {
                            startActivity(Intent(this,MainActivity::class.java))
                            finish()
                        }
                        .addOnFailureListener{
                            Toast.makeText(this,"Failed to creation Account Try again after some time !!",Toast.LENGTH_SHORT).show()
                        }
                }
            }





        }

    }

}