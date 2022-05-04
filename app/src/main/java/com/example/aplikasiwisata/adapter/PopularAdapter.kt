package com.example.aplikasiwisata.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aplikasiwisata.DetailActivity
import com.example.aplikasiwisata.databinding.ItemPlaceGirdBinding
import com.example.aplikasiwisata.model.Place
import com.squareup.picasso.Picasso

class PopularAdapter(
    private val context: Context,
    private var placeList: ArrayList<Place>
) : RecyclerView.Adapter<PopularAdapter.PlaceViewHolder>() {

    inner class PlaceViewHolder(val binding: ItemPlaceGirdBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        return PlaceViewHolder(
            ItemPlaceGirdBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val newList = placeList[position]
        with(holder) {
            if (newList.photoUrl !== null) {
                Picasso.with(binding.placePhoto.context)
                    .load(newList.photoUrl)
                    .into(binding.placePhoto)
            }
            binding.placeDetail.text = newList.name
        }



        holder.binding.root.setOnClickListener {
            val placeId = newList.placeId
            val name = newList.name
            val photoUrl = newList.photoUrl
            val detail = newList.detail
            val address = newList.address
            val phone = newList.phone
            val hour = newList.hour
            val website = newList.website
            val ticket = newList.ticket

            val place = Place(placeId, name, photoUrl, detail, address, phone, website, hour, ticket)

            Intent(context, DetailActivity::class.java)
                .putExtra("place", place).also {
                    context.startActivity(it)
                }

        }
    }

    override fun getItemCount(): Int {
        return placeList.size
    }

}


