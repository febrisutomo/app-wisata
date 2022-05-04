package com.example.aplikasiwisata.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class UserProfile(
    var uid: String? = null,
    var name: String? = null,
    var photoUrl: String? = null,
    var phone: String? = null,
    var address: String? = null,
)
