package com.example.aplikasiwisata

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aplikasiwisata.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    private val loading = LoadingDialog(this)
    private val forgetPassDialog = ForgetPassDialog(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener(this)
        binding.btnRegister.setOnClickListener(this)
        binding.btnForgotPassword.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnLogin -> {
                val email = binding.etEmail.text.toString().trim()
                val password = binding.etPassword.text.toString().trim()

                if (email.isEmpty()) {
                    binding.etlEmail.error = "Email harus diisi!"
                    return
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.etEmail.error = "Email tidak valid!"
                    return
                }

                if (password.isEmpty()) {
                    binding.etlPassword.error = "Password harus diisi!"
                    return
                }

                loginUser(email, password)
            }
            R.id.btnRegister-> {
                Intent(this, RegisterActivity::class.java).also {
                    startActivity(it)
                }
            }
            R.id.btnForgotPassword-> {
                forgetPassDialog.showDialog()
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        loading.startLoading()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                loading.stopLoading()
                if (task.isSuccessful) {
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