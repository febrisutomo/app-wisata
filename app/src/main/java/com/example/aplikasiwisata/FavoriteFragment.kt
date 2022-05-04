package com.example.aplikasiwisata

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplikasiwisata.adapter.PlaceAdapter
import com.example.aplikasiwisata.databinding.FragmentFavoriteBinding
import com.example.aplikasiwisata.model.Place
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var placeAdapter: PlaceAdapter

    private lateinit var myRef: DatabaseReference
    private lateinit var placeList: ArrayList<Place>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(activity as AppCompatActivity) {
            setSupportActionBar(binding.toolbar)

            supportActionBar?.apply {
                title = "Bookmark"

                setDisplayHomeAsUpEnabled(true)
                setDisplayShowHomeEnabled(true)

            }
        }

        placeList = ArrayList()

        placeAdapter = PlaceAdapter(view.context, placeList)

        binding.recyclerPlace.apply {
            layoutManager = LinearLayoutManager(view.context)
            setHasFixedSize(true)
        }

        auth = FirebaseAuth.getInstance()

        getTourismData()


    }


    private fun getTourismData() {
        val uid = auth.currentUser?.uid
        myRef =
            FirebaseDatabase.getInstance("https://aplikasi-wisata-c4aee-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("places")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                placeList.clear()
                if (snapshot.exists()) {
                        for (placeSnapshot in snapshot.children) {
                            if (placeSnapshot.child("favorites").hasChild(uid!!)){
                                val place = placeSnapshot.getValue(Place::class.java)
                                placeList.add(place!!)
                            }
                        }
                        binding.recyclerPlace.adapter = placeAdapter
//                    }
                }

                if (placeList.isEmpty()){
                    binding.emptyState.visibility = View.VISIBLE
                }
                else{
                    binding.emptyState.visibility = View.INVISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


    }


}