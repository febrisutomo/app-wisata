package com.example.aplikasiwisata

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val user = FirebaseAuth.getInstance().currentUser

        Handler(Looper.getMainLooper()).postDelayed({
            if (user !== null) {
                Intent(this, MainActivity::class.java).also {
                    startActivity(it)
                }
            } else {
                Intent(this, LoginActivity::class.java).also {
                    startActivity(it)
                }
            }
            finish()
        }, 3000)
    }

}