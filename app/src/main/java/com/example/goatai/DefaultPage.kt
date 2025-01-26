package com.example.goatai

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth

class DefaultPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_default_page)

        val nameTextView = findViewById<TextView>(R.id.NameTextView)
        val user = FirebaseAuth.getInstance().currentUser
        if (user!= null){
            val name = user.displayName ?: "User"
            nameTextView.text = "Welcome, $name"
        }

        findViewById<MaterialButton>(R.id.btn_newChat).setOnClickListener{
            startActivity(Intent(this@DefaultPage, Chat::class.java))
        }

        findViewById<MaterialButton>(R.id.btn_previousChat).setOnClickListener{
            startActivity(Intent(this@DefaultPage, PreviousChats::class.java))
        }
    }
}