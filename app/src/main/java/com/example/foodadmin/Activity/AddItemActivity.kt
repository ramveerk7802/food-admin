package com.example.foodadmin.Activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.foodadmin.DataModel.ItemModel
import com.example.foodadmin.MainActivity
import com.example.foodadmin.R
import com.example.foodadmin.databinding.ActivityAddItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import java.util.UUID

class AddItemActivity : AppCompatActivity() {
    private val binding : ActivityAddItemBinding by lazy{
        ActivityAddItemBinding.inflate(layoutInflater)
    }
    private lateinit var imagerUri: Uri
    private lateinit var auth: FirebaseAuth
    private lateinit var database:DatabaseReference
    private lateinit var storage : StorageReference



    private  val pickImage = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){ uri->
            if (uri!=null){
                Picasso.get().load(uri).into(binding.itemImage)
                imagerUri=uri
            }

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

        // initialized all instance of database
        auth = Firebase.auth
        database = Firebase.database.reference
        storage = Firebase.storage.reference




        binding.itemImage.setOnClickListener{
            pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        binding.addButton.setOnClickListener {
            val foodName = binding.itemName.text.toString().trim()
            val foodPrice = binding.itemPrice.text.toString().trim()
            val foodDesc = binding.shortDescEdtText.text.toString().trim()

            if(foodName.isBlank() || foodDesc.isBlank() || foodPrice.isBlank()){
                Toast.makeText(this,"Fill all the detail",Toast.LENGTH_SHORT).show()
            }else if(imagerUri.toString().isBlank()){
                Toast.makeText(this,"Select the food Image !!",Toast.LENGTH_SHORT).show()
            }else{
                    Log.i("addItem",imagerUri.toString())
                val price = foodPrice.toInt()
                uploadAndSaveData(foodName,price,foodDesc)
            }
        }
    }

    private fun uploadAndSaveData(foodName: String, price: Int, foodDesc: String) {
        val imageName = UUID.randomUUID().toString()
        val imageReference = storage.child("foodImage/$imageName.jpg")
        imageReference.putFile(imagerUri)
            .addOnSuccessListener {
                imageReference.downloadUrl.addOnSuccessListener { imageLocation->
                    val imageUrl = imageLocation.toString()
                    saveData(foodName,price,foodDesc,imageUrl)

                }
            }

    }

    private fun saveData(foodName: String, price: Int, foodDesc: String, imageUrl: String) {

        val menuReference = database.child("AllMenu")
        val key = menuReference.push().key
        val itemModel = ItemModel(foodName = foodName, foodPrice = price, foodDesc = foodDesc, foodImage = imageUrl, foodId = key!!)
        key.let{
            menuReference.child(key).setValue(itemModel)
                .addOnSuccessListener {
                    startActivity(Intent(this,MainActivity::class.java))
                    Toast.makeText(this,"Item added successfully",Toast.LENGTH_SHORT).show()
                    finish()
                }
        }
    }
}