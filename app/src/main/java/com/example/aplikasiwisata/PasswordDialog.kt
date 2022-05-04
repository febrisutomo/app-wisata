package com.example.aplikasiwisata

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class PasswordDialog(val activity: Activity) {
    private lateinit var loadingDialog: LoadingDialog
    private val inflater = activity.layoutInflater
    @SuppressLint("InflateParams")
    fun showDialog() {
        val view = inflater.inflate(R.layout.dialog_password, null)
        val etPassword = view.findViewById<TextInputEditText>(R.id.etPassword)
        val etlPassword = view.findViewById<TextInputLayout>(R.id.etlPassword)
        loadingDialog = LoadingDialog(activity)
        with(AlertDialog.Builder(activity)) {
            setTitle("Ganti Password")
            setView(view)
            setPositiveButton("Ubah", null)
            setNegativeButton("Batal") { _, _ -> }
            create().apply {
                setOnShowListener {
                    getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {

                        val password = etPassword.text.toString()

                        if (password.isEmpty() || password.length < 6) {
                            etlPassword.error = "Password minimal 6 karakter!"
                        } else {
                            loadingDialog.startLoading()
                            val user = Firebase.auth.currentUser

                            user!!.updatePassword(password)
                                .addOnCompleteListener { task ->
                                    loadingDialog.stopLoading()
                                    dismiss()
                                    if (task.isSuccessful) {
                                        Toast.makeText(
                                            activity,
                                            "Password berhasil diubah!", Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    else{
                                        Toast.makeText(
                                            activity,
                                            "Password gagal diubah!", Toast.LENGTH_SHORT
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