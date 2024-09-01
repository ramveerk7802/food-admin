package com.example.foodadmin.Sign

import android.content.Intent
import android.health.connect.datatypes.ExerciseRoute.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.foodadmin.Activity.AdminProfileActivity
import com.example.foodadmin.DataModel.UserModel
import com.example.foodadmin.MainActivity
import com.example.foodadmin.OtherClass.PasswordEncodeManager
import com.example.foodadmin.R
import com.example.foodadmin.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignInActivity: AppCompatActivity() {

    private val binding:ActivitySignInBinding by lazy {
        ActivitySignInBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth
    private lateinit var database:DatabaseReference
    private  lateinit var email:String
    private lateinit var plainPassword :String





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //initialized the firebase and database

        auth=Firebase.auth
        database= Firebase.database.reference

        // get the value from edit text


        // handle the login button
        binding.loginButton.setOnClickListener {
            email = binding.emailEdt.text.toString().trim()
            plainPassword = binding.passwordEdt.text.toString().trim()

            if(email.isBlank() || plainPassword.isBlank()){
                Toast.makeText(this,"Please fill all the details !!",Toast.LENGTH_SHORT).show()

            } else if(plainPassword.length<8){
                Toast.makeText(this,"Password must be 8 character !!",Toast.LENGTH_SHORT).show()
            }
            else{
                val x= intent

                if(x?.getBooleanExtra("fromCreate",false)==true) {
                    val hashedPassword:String = x.getStringExtra("hashedPassword").orEmpty()
                    val passwordEncoder = PasswordEncodeManager()
                    if(passwordEncoder.matches(plainPassword,hashedPassword)){
                        signInWith(email,hashedPassword)
                    }
                    else{
                        Toast.makeText(this,"Enter the Wrong Password !!",Toast.LENGTH_SHORT).show()
                    }
                }
                else {
                    Log.i("signIn","Else sign in")
                    retrieveHashedPassword(email,plainPassword)

                }

            }
        }


        binding.dontText.setOnClickListener{
            startActivity(Intent(this@SignInActivity,SignUpActivity::class.java))
            finish()
        }
    }

    private fun signInWith(email: String, hashedPassword: String) {
        Log.i("signIn","Sign in called and value is : $email $hashedPassword")
        auth.signInWithEmailAndPassword(email, hashedPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.reload()?.addOnCompleteListener { reloadTask ->
                        if (reloadTask.isSuccessful) {
                            if (user.isEmailVerified) {
                                Log.i("signIn", "Email Verified")
                                // Proceed with checking the profile or any other logic
                                checkProfile(email, hashedPassword, user.uid)
                            } else {
                                auth.signOut()
                                Log.i("signIn", "Email not Verified")
                                Toast.makeText(this, "Please verify your email before signing in.", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Log.e("signIn", "Failed to reload user data.")
                            Toast.makeText(this, "An error occurred. Please try again.", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    auth.signOut()
                    Log.e("signIn", "Failed to sign in: ${task.exception?.message}")
                    Toast.makeText(this, "Register first then login. !!", Toast.LENGTH_SHORT).show()
                }
            }
    }


    private fun checkProfile(email: String, hashedPassword: String,userId: String) {
        Log.i("signIn","checkProfile Method Called")

        val userReference = database.child("User")
        userReference.child(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                            startActivity(Intent(this@SignInActivity,MainActivity::class.java))
                            finish()

                    }else{
                        Log.i("signIn","checkProfile Method Called and snapshot not Exist")
                        val intent1:Intent= intent
                        Toast.makeText(this@SignInActivity,"User does not exist !!",Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@SignInActivity,AdminProfileActivity::class.java).apply {
                            putExtra("email",email)
                            putExtra("hashedPassword",hashedPassword)
                            putExtra("ownerName",intent1.getStringExtra("ownerName").toString())
                            putExtra("location",intent1.getStringExtra("location").orEmpty())
                            putExtra("restaurantName",intent1.getStringExtra("restaurantName").orEmpty())
                            putExtra("fromComplete",true)
                        }
                        startActivity(intent)
                        finish()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    private fun retrieveHashedPassword(email: String, plainPassword: String) {
        database.child("User")
            .orderByChild("email")
            .equalTo(email)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        // User found, get hashed password
                        Log.i("signIn","User found, get hashed password")
                        for (userSnapshot in snapshot.children) {
                            val hashedPassword = userSnapshot.child("password").getValue(String::class.java)
                            if (hashedPassword != null) {
                                // Compare the provided plain password with the hashed password
                                if (PasswordEncodeManager().matches(plainPassword, hashedPassword)) {
                                    signInWith(email, hashedPassword = hashedPassword)
                                } else {
                                    Toast.makeText(this@SignInActivity, "Invalid password.", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    } else {
                        Toast.makeText(this@SignInActivity, "User not found. Please register.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("signIn", "Database error: ${error.message}")
                    Toast.makeText(this@SignInActivity, "Database error occurred.", Toast.LENGTH_SHORT).show()
                }
            })
    }


}