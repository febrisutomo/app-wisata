package com.example.aplikasiwisata

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.util.Patterns
import android.view.LayoutInflater
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ForgetPassDialog(val activity: Activity) {

    fun showDialog() {
        val view = LayoutInflater.from(activity).inflate(R.layout.dialog_forget_password, null)
        val etEmail = view.findViewById<TextInputEditText>(R.id.etEmail)
        val etlEmail = view.findViewById<TextInputLayout>(R.id.etlEmail)
        with(AlertDialog.Builder(activity)) {
            setTitle("Lupa Password")
            setView(view)
            setPositiveButton("Kirim", null)
            setNegativeButton("Batal") { _, _ -> }
            create().apply {
                setOnShowListener {
                    getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {

                        val email = etEmail.text.toString()

                        if (email.isEmpty()) {
                            etlEmail.error = "Email harus diisi"
                        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                            etlEmail.error = "Email tidak valid!"
                        } else {
                            dismiss()
                            Firebase.auth.sendPasswordResetEmail(email)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(
                                            activity,
                                            "Email reset password berhasil dikirimkan!", Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            activity,
                                            "Email tidak terdaftar!", Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                        }
                    }
                }
            }
        }.show()
    }

}