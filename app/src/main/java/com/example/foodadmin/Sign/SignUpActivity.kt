package com.example.foodadmin.Sign

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.foodadmin.Activity.AdminProfileActivity
import com.example.foodadmin.OtherClass.PasswordEncodeManager
import com.example.foodadmin.R
import com.example.foodadmin.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class SignUpActivity : AppCompatActivity() {


    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var location: String
    private lateinit var email: String
    private lateinit var plainPassword: String
    private lateinit var ownerName: String
    private lateinit var restaurantName: String


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

        // initialized firebase
        auth = Firebase.auth

        // initialized database
        database = Firebase.database.reference


        // Declare the variable


        binding.alreadyText.setOnClickListener {
            startActivity(Intent(this@SignUpActivity, SignInActivity::class.java))
            finish()
        }

        // set the location list
        val locationList = arrayOf(
            "Uttar Pradesh",
            "Delhi",
            "Haryana",
            "Punjab",
            "Chandigarh",
            "Gujarat",
            "Rajasthan",
            "Karnataka",
            "Tamil Nadu",
            "Kerala",
            "Goa"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, locationList)
        binding.autoCompleteTextView.setAdapter(adapter)
        binding.autoCompleteTextView.setOnTouchListener { view: View, motionEvent: MotionEvent ->
            binding.autoCompleteTextView.showDropDown()
            false
        }

        binding.createAccountButton.setOnClickListener {

            location = binding.autoCompleteTextView.text.toString()
            ownerName = binding.ownerNameEdt.text.toString().trim()
            restaurantName = binding.restaurantNameEdt.text.toString().trim()
            plainPassword = binding.passwordEdt.text.toString().trim()
            email = binding.emailEdt.text.toString().trim()

            if (location.equals("location")) {
                Toast.makeText(this, "Select the location", Toast.LENGTH_SHORT).show()
            } else if (ownerName.isBlank() || restaurantName.isBlank() || plainPassword.isBlank() || email.isBlank()) {
                Toast.makeText(this, "Please fill the all detail !!", Toast.LENGTH_SHORT).show()
           }else if(plainPassword.length<8){
                Toast.makeText(this,"Password below 8 character!!",Toast.LENGTH_SHORT).show()
            }
            else {
                val passwordEncode = PasswordEncodeManager()
                val hashedPassword = passwordEncode.encode(plainPassword)
                createAccount2(email,hashedPassword)
            }

        }

    }

    private fun createAccount2(email: String, hashedPassword: String) {

        auth.createUserWithEmailAndPassword(email,hashedPassword)
            .addOnCompleteListener { task->
                if(task.isSuccessful){
                    val user = auth.currentUser
                    user?.sendEmailVerification()?.addOnCompleteListener { it->
                            if(it.isSuccessful){
                                Toast.makeText(this,"Send verification Email, check your email !!",Toast.LENGTH_SHORT).show()
                                val intent = Intent(this,SignInActivity::class.java).apply {
                                    putExtra("ownerName",ownerName)
                                    putExtra("restaurantName",restaurantName)
                                    putExtra("location",location)
                                    putExtra("hashedPassword",hashedPassword)
                                    putExtra("fromCreate",true)
                                }
                                auth.signOut()
                                startActivity(intent)
                                finish()
                            }else{
                                Toast.makeText(this,"Failed to send verification email,try again later !!",Toast.LENGTH_SHORT).show()
                                auth.signOut()
                            }
                        }
                }
                else{
                    auth.signOut()
                    Toast.makeText(this,"Failed to create Account !!",Toast.LENGTH_SHORT).show()
                }
            }

    }

//    private fun createAccount(email: String, hashedPassword: String) {
//
//        auth.createUserWithEmailAndPassword(email,hashedPassword)
//            .addOnCompleteListener { taskId->
//                if(taskId.isSuccessful){
//                    val user :FirebaseUser?= auth.currentUser
//                    user?.sendEmailVerification()?.addOnCompleteListener { it->
//                        if(it.isSuccessful){
//
//
//                            val intent = Intent(this, SignInActivity::class.java).apply {
//                                putExtra("ownerName", ownerName)
//                                putExtra("restaurantName", restaurantName)
//                                putExtra("location", location)
//                                putExtra("hashedPassword", hashedPassword)
//                                putExtra("email", email)
//                            }
//                            startActivity(intent)
//                            Toast.makeText(this, "Verification email sent! Verify and log in.", Toast.LENGTH_SHORT).show()
//                            finish()
//                        }
//                        else{
//                            Toast.makeText(this, "Failed to send verification email.", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                }
//            }
//            .addOnFailureListener {
//                Toast.makeText(this, "Account creation failed. Try again later.", Toast.LENGTH_SHORT).show()
//            }
//    }


}