package com.unipi.p17172p17168p17164.multiplicationgame.models

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.parcelize.Parcelize
import java.util.*

/**
 * A data model class with required fields.
 */
@Keep
@Parcelize
@IgnoreExtraProperties
data class User(
    val userId: String = "",
    val fullName: String = "",
    val email: String = "",
    @ServerTimestamp
    val dateRegistered: Date = Date(),
    val admin: Boolean = false
) : Parcelable