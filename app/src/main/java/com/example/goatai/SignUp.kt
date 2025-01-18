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

class SignUp : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)

        mAuth = FirebaseAuth.getInstance()

        var edtname = findViewById<TextInputLayout>(R.id.sp_name).editText as TextInputEditText
        var edtage = findViewById<TextInputLayout>(R.id.sp_age).editText as TextInputEditText
        var edtprofession = findViewById<TextInputLayout>(R.id.sp_profession).editText as TextInputEditText
        var edtemail = findViewById<TextInputLayout>(R.id.sp_email).editText as TextInputEditText
        var edtpassword = findViewById<TextInputLayout>(R.id.sp_password).editText as TextInputEditText

        val login = findViewById<MaterialButton>(R.id.sp_login).setOnClickListener(){
            startActivity(Intent(this@SignUp, Login::class.java))
        }
        var signUp = findViewById<MaterialButton>(R.id.sp_signup).setOnClickListener(){
            val name = edtname.text.toString().trim()
            val age = edtage.text.toString().trim()
            val profession = edtprofession.text.toString().trim()
            val email = edtemail.text.toString().trim()
            val password = edtpassword.text.toString().trim()

            if (name.isNotEmpty() || age.isNotEmpty() || profession.isNotEmpty() || email.isNotEmpty() || password.isNotEmpty()){
                signup(name, age, password, email, password)
            }
            else{
                Toast.makeText(this, "Please fill required feilds.", Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun signup(name: String, age:String, proffesion: String, email: String, password: String){
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){task->
                if(task.isSuccessful){
                    startActivity(Intent(this@SignUp, DefaultPage::class.java)
                    )
                }else{
                    Toast.makeText(this, "Error occurred while signing in", Toast.LENGTH_LONG).show()
                }
            }
    }

}