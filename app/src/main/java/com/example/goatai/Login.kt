package com.example.goatai

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()
        var edtemail = findViewById<TextInputLayout>(R.id.lg_email).editText as TextInputEditText
        var edtpassword = findViewById<TextInputLayout>(R.id.lg_password).editText as TextInputEditText

        val signup = findViewById<MaterialButton>(R.id.lg_signup).setOnClickListener(){
            startActivity(Intent(this@Login, SignUp::class.java))
        }
        val login = findViewById<MaterialButton>(R.id.lg_login).setOnClickListener(){
            val email = edtemail.text.toString().trim()
            val password = edtpassword.text.toString().trim()

            if (email.isNotEmpty() || password.isNotEmpty()){
                login(email, password)
            }
            else{
                Toast.makeText(this, "Required fields", Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun login(email: String, password: String){
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){task ->
                if (task.isSuccessful){
                    startActivity(Intent(this@Login, DefaultPage::class.java))
                }else{
                    Toast.makeText(this, "Failed logging in", Toast.LENGTH_LONG).show()
                }
            }

    }
}