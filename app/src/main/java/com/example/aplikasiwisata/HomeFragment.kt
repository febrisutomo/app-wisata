package com.example.aplikasiwisata

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.aplikasiwisata.adapter.PlaceAdapter
import com.example.aplikasiwisata.adapter.PopularAdapter
import com.example.aplikasiwisata.databinding.FragmentHomeBinding
import com.example.aplikasiwisata.model.Place
import com.example.aplikasiwisata.model.UserProfile
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var placeAdapter: PlaceAdapter
    private lateinit var popularAdapter: PopularAdapter

    //    private lateinit var db: FirebaseFirestore
    private lateinit var databaseReference: DatabaseReference
    private lateinit var placeList: ArrayList<Place>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        placeList = ArrayList()
        placeAdapter = PlaceAdapter(view.context, placeList)
        popularAdapter = PopularAdapter(view.context, placeList)
        binding.recyclerPlace.apply {
            layoutManager = LinearLayoutManager(view.context)
            setHasFixedSize(true)
        }
        binding.recyclerPopular.apply {
            layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }
        getTourismData()

        val user = Firebase.auth.currentUser

        if (user !== null) {
           getProfile(user.uid)
        }

    }

    private fun getProfile(uid: String) {

        val ref =
            FirebaseDatabase.getInstance("https://aplikasi-wisata-c4aee-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("users")

        ref.child(uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val user = snapshot.getValue(UserProfile::class.java)
                    if (user?.photoUrl != null) {
                        Glide.with(binding.userImg.context)
                            .load(user.photoUrl)
                            .into(binding.userImg)
                    }
                    binding.tvHalo.text = "Halo, " + user?.name
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun getTourismData() {

        databaseReference =
            FirebaseDatabase.getInstance("https://aplikasi-wisata-c4aee-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("places")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                placeList.clear()
                if (snapshot.exists()) {
                    for (placeSnapshot in snapshot.children) {
                        val place = placeSnapshot.getValue(Place::class.java)
                        placeList.add(place!!)
                    }
                    binding.recyclerPlace.adapter = placeAdapter
                    binding.recyclerPopular.adapter = popularAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    activity,
                    error.message, Toast.LENGTH_SHORT
                ).show()
            }

        })
    }


}