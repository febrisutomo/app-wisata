package com.example.aplikasiwisata.model

import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize

@Parcelize
@IgnoreExtraProperties
data class Place(
    var placeId: String? = null,
    var name: String? = null,
    var photoUrl: String? = null,
    var detail: String? = null,
    var address: String? = null,
    var phone: String? = null,
    var website: String? = null,
    var hour: String? = null,
    var ticket: String? = null

    ) : Parcelable {

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "name" to name,
            "photoUrl" to photoUrl,
        )
    }
}