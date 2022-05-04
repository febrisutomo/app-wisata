package com.example.aplikasiwisata

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.aplikasiwisata.databinding.FragmentProfileBinding
import com.example.aplikasiwisata.model.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    private lateinit var loadingDialog: LoadingDialog

    private lateinit var passwordDialog: PasswordDialog

    private lateinit var ref: DatabaseReference

    private lateinit var uid: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        loadingDialog = LoadingDialog(activity as AppCompatActivity)

        with(activity as AppCompatActivity) {
            setSupportActionBar(binding.toolbar)

            supportActionBar?.apply {
                title = "Profile Menu"

                setDisplayHomeAsUpEnabled(true)
                setDisplayShowHomeEnabled(true)

            }
        }

        val user = FirebaseAuth.getInstance().currentUser
        uid = user?.uid.toString()
        val email = user?.email

        val photoUrl =
            "https://sahabatperubahan.com/wp-content/uploads/2021/03/placeholder-profile-sq.jpg"

        getProfile()

        binding.etEmail.setText(email)

        binding.btnUpdate.setOnClickListener {
            val name = binding.etName.text.toString()
            val phone = binding.etPhone.text.toString()
            val address = binding.etAddress.text.toString()
            updateProfile(uid, name, photoUrl, phone, address)
        }

        passwordDialog = PasswordDialog(activity as AppCompatActivity)

        binding.btnChangePassword.setOnClickListener {
            passwordDialog.showDialog()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_profile, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                signOut()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun signOut() {
        val builder = AlertDialog.Builder(activity as AppCompatActivity)
        builder.setTitle("Logout")
        builder.setMessage("Apakah anda yakin ingin keluar?")

        builder.setPositiveButton("Ya") { _, _ ->
            Firebase.auth.signOut()
            Intent(activity, LoginActivity::class.java).also {
                startActivity(it)
            }
            (activity as AppCompatActivity).finish()
        }

        builder.setNegativeButton("Tidak") { _, _ ->

        }
        builder.show()
    }


    private fun getProfile() {
        ref =
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
                    binding.etName.setText(user?.name)
                    binding.etAddress.setText(user?.address)
                    binding.etPhone.setText(user?.phone)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


    private fun updateProfile(uid: String, name: String, photoUrl: String, phone: String, address: String) {

        val user = mapOf(
            "uid" to uid,
            "name" to name,
            "photoUrl" to photoUrl,
            "phone" to phone,
            "address" to address
        )

        loadingDialog.startLoading()
        ref =
            FirebaseDatabase.getInstance("https://aplikasi-wisata-c4aee-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("users")

        ref.child(uid).updateChildren(user).addOnSuccessListener {
            loadingDialog.stopLoading()
            Toast.makeText(
                activity, "Profile berhasil diupdate!",
                Toast.LENGTH_LONG
            ).show()
        }.addOnFailureListener {
            loadingDialog.stopLoading()
            Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
        }

    }


}