package com.example.aplikasiwisata

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aplikasiwisata.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    private val loading = LoadingDialog(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener(this)
        binding.btnRegister.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnRegister -> {
                val email = binding.etEmail.text.toString().trim()
                val password = binding.etPassword.text.toString().trim()

                if (email.isEmpty()) {
                    binding.etlEmail.error = "Email harus diisi!"
                    return
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.etlEmail.error = "Email tidak valid!"
                    return
                }

                if (password.isEmpty() || password.length < 6) {
                    binding.etlPassword.error = "Password minimal 6 karakter!"
                    return
                }

                registerUser(email, password)
            }
            R.id.btnLogin -> {
                Intent(this, LoginActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
    }

    private fun registerUser(email: String, password: String) {
        loading.startLoading()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {task ->
                loading.stopLoading()
                if (task.isSuccessful) {
                    auth.currentUser?.sendEmailVerification()
                    Intent(this, MainActivity::class.java).also {
                        it.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(it)
                    }

                } else {
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
    }
}