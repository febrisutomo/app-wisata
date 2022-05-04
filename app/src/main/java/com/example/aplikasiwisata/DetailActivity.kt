package com.example.aplikasiwisata

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import com.example.aplikasiwisata.databinding.ActivityDetailBinding
import com.example.aplikasiwisata.model.Place
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import java.net.URLEncoder

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var ref: DatabaseReference
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

//        dynamicToolbarColor()
        toolbarTextApperance()

        val place = intent.getParcelableExtra<Place>("place")

        if (place?.photoUrl !== null) {
            Picasso.with(binding.placePhoto.context)
                .load(place.photoUrl)
                .into(binding.placePhoto)
        }

        binding.placeAddress.text = place?.address
        binding.placeDetail.text = place?.detail
        binding.placeHour.text = place?.hour
        binding.placeDetail.text = place?.detail
        binding.placeTicket.text = place?.ticket
        binding.placeWebsite.text = place?.website
        binding.placePhone.text = place?.phone
        binding.toolbarLayout.title = place?.name

        if(place?.website == null){
            binding.placeWebsite.visibility = View.GONE
        }
        if (place?.phone == null){
            binding.placePhone.visibility = View.GONE
        }

        binding.btnMap.setOnClickListener {
           openMaps(place?.name)
        }

        auth = FirebaseAuth.getInstance()
        ref =
            FirebaseDatabase.getInstance("https://aplikasi-wisata-c4aee-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("places").child(place?.placeId!!).child("favorites")


        chekIsFavorite(place.placeId!!)

        binding.btnFavorite.setOnClickListener {
            toggleFavorite()
        }

        binding.btnShare.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "${place.name}\n\n${place.address}")
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

    }

    private fun toggleFavorite() {
        if (isFavorite) {
            ref.child(auth.currentUser!!.uid).removeValue().addOnSuccessListener {
                binding.btnFavorite.apply {
                    text = "Simpan"
                    setTextColor(ContextCompat.getColor(this@DetailActivity, R.color.teal_700))
                    iconTint =
                        ContextCompat.getColorStateList(this@DetailActivity, R.color.teal_700)
                    setBackgroundColor(Color.WHITE)
                }
                isFavorite = false
                Toast.makeText(
                    this@DetailActivity,
                    "Dihapus dari bookmark!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            ref.child(auth.currentUser!!.uid).setValue(true).addOnSuccessListener {
                binding.btnFavorite.apply {
                    text = "Disimpan"
                    setTextColor(Color.WHITE)
                    iconTint =
                        ContextCompat.getColorStateList(this@DetailActivity, R.color.white)
                    setBackgroundColor(
                        ContextCompat.getColor(
                            this@DetailActivity,
                            R.color.teal_700
                        )
                    )
                }

                isFavorite = true
                Toast.makeText(
                    this@DetailActivity,
                    "Disimpan di bookmark!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun openMaps(name: String?) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Peta Lokasi")
        builder.setMessage("Buka Aplikasi Google Maps?")

        builder.setPositiveButton("Ya") { _, _ ->
            val query: String = URLEncoder.encode(name, "utf-8")
            val gmmIntentUri =
                Uri.parse("geo:0,0?q=${query}")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }

        builder.setNegativeButton("Batal") { _, _ ->

        }
        builder.show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun chekIsFavorite(placeId: String) {

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    if (snapshot.hasChild(auth.currentUser!!.uid)) {
                        binding.btnFavorite.apply {
                            text = "Disimpan"
                            setTextColor(Color.WHITE)
                            iconTint =
                                ContextCompat.getColorStateList(this@DetailActivity, R.color.white)
                            setBackgroundColor(
                                ContextCompat.getColor(
                                    this@DetailActivity,
                                    R.color.teal_700
                                )
                            )
                        }
                        isFavorite = true
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun dynamicToolbarColor() {

        val bitmap = binding.placePhoto.background.toBitmap()

        Palette.from(bitmap).generate { palette ->
            binding.toolbarLayout.setContentScrimColor(palette!!.getMutedColor(R.attr.colorPrimary))
            binding.toolbarLayout.setStatusBarScrimColor(palette.getMutedColor(R.attr.colorPrimaryDark))
            window.statusBarColor = palette.getMutedColor(R.attr.colorPrimaryDark)
        }
    }


    private fun toolbarTextApperance() {
//        binding.toolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedAppbar)
        binding.toolbarLayout.setExpandedTitleTextAppearance(R.style.expandedAppbar)
    }
}
