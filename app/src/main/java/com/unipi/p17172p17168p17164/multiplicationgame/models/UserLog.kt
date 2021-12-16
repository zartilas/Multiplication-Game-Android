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
data class UserLog(
    val userId: String = "",
    val type: String = "", // time_up, skip, mistake, solved
    val random: Int = 1,
    val numFirst: Int = 0,
    val numSecond: Int = 0,
    @ServerTimestamp
    val dateAdded: Date = Date(),
    var logId: String = "",
) : Parcelable