package com.example.aplikasiwisata

import android.app.Activity
import android.app.AlertDialog

class LoadingDialog(val activity: Activity) {
    private  lateinit var dialog: AlertDialog
    fun startLoading(){
        val inflater = activity.layoutInflater
        val builder = AlertDialog.Builder(activity)
        builder.setView(inflater.inflate(R.layout.dialog_loading, null))
        builder.setCancelable(false)
        dialog = builder.create()
        dialog.show()
    }

    fun stopLoading(){
        dialog.dismiss()
    }
}