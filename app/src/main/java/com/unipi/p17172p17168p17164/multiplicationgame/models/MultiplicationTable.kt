package com.unipi.p17172p17168p17164.multiplicationgame.models

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize

/**
 * A data model class with required fields.
 */
@Keep
@Parcelize
@IgnoreExtraProperties
data class MultiplicationTable(
    var tableId: String = "",
    val name: String = "",
    val desc: String = "",
    val number: Int = 0,
    val limit: Int = 0,
) : Parcelable