package com.example.goatai

import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.view.animation.Animation
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.animation.AnimationUtils
import com.google.firebase.auth.FirebaseAuth

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val splashLogo = findViewById<ImageView>(R.id.logo)

        val animation = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.fade_in)
        splashLogo.startAnimation(animation)

        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                startActivity(Intent(this@Splash, Login::class.java))
                finish()
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })
    }
}