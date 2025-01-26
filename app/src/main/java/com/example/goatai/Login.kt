package com.example.goatai

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var frontcard: CardView
    private lateinit var backcard: CardView
    private var isFrontVisible = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()
        var edtemail = findViewById<TextInputLayout>(R.id.lg_email).editText as TextInputEditText
        var edtpassword = findViewById<TextInputLayout>(R.id.lg_password).editText as TextInputEditText

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

        frontcard = findViewById(R.id.id_card)
        backcard = findViewById(R.id.id_card_signup)

        frontcard.setOnClickListener{flipcard()}
        backcard.setOnClickListener{flipcard()}
    }

    private fun flipcard(){
        val scale = applicationContext.resources.displayMetrics.density
        frontcard.cameraDistance = 8000 * scale
        backcard.cameraDistance = 8000 * scale

        val flipOut: AnimatorSet = AnimatorInflater.loadAnimator(this, R.animator.card_flip_out) as AnimatorSet
        val flipIn: AnimatorSet = AnimatorInflater.loadAnimator(this, R.animator.card_flip_in) as AnimatorSet

        if (isFrontVisible){
            flipOut.setTarget(frontcard)
            flipIn.setTarget(backcard)
            flipOut.start()
            flipIn.start()
            isFrontVisible = false
        }else{
            flipOut.setTarget(backcard)
            flipIn.setTarget(frontcard)
            flipOut.start()
            flipIn.start()
            isFrontVisible = true
        }

    }

}