package com.unipi.p17172p17168p17164.multiplicationgame.database

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.unipi.p17172p17168p17164.multiplicationgame.models.MultiplicationTable
import com.unipi.p17172p17168p17164.multiplicationgame.models.User
import com.unipi.p17172p17168p17164.multiplicationgame.models.UserLog
import com.unipi.p17172p17168p17164.multiplicationgame.ui.activities.*
import com.unipi.p17172p17168p17164.multiplicationgame.utils.Constants


class FirestoreHelper {

    // Access a Cloud Firestore instance.
    private val dbFirestore = FirebaseFirestore.getInstance()

    /**
     * A function to get the user id of current logged user.
     */
    fun getCurrentUserID(): String {
        // An Instance of currentUser using FirebaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser

        // A variable to assign the currentUserId if it is not null or else it will be blank.
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }

        return currentUserID
    }

    /**
     * A function to get the logged user details from from FireStore Database.
     */
    fun getUserDetails(activity: Activity) {

        // Here we pass the collection name from which we wants the data.
        dbFirestore.collection(Constants.COLLECTION_USERS)
            // The document id to get the Fields of user.
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->

                Log.d(activity.javaClass.simpleName, document.toString())

                // Here we have received the document snapshot which is converted into the User Data model object.
                val user = document.toObject(User::class.java)!!

                when (activity) {
                    // When activity is the sign in one
                    is SignInActivity -> {
                        val sharedPreferences =
                            activity.getSharedPreferences(
                                Constants.SHARED_PREFERENCES_PREFIX,
                                Context.MODE_PRIVATE
                            )

                        // Create an instance of the editor which is help us to edit the SharedPreference.
                        val editor: SharedPreferences.Editor = sharedPreferences.edit()
                        editor.putString(
                            Constants.LOGGED_IN_EMAIL,
                            user.email
                        )
                        editor.apply()
                        // Call a function of base activity for transferring the result to it.
                        activity.userLoggedInSuccess()
                    }
                    // When activity is the profile details one
                    is ProfileDetailsActivity -> {
                        activity.successProfileDetailsFromFirestore(user)
                    }
                }
            }
            .addOnFailureListener { e ->
                // Hide the progress dialog if there is any error. And print the error in log.
                when (activity) {
                    is SignInActivity -> {
                        activity.hideProgressDialog()
                    }
                    is ProfileDetailsActivity -> {
                        activity.hideProgressDialog()
                    }
                }

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while getting user details.",
                    e
                )
            }
    }

    /**
     * A function to make an entry of the registered user in the FireStore database.
     */
    fun registerUser(activity: SignUpActivity, userInfo: User) {

        // The "users" is collection name. If the collection is already created then it will not create the same one again.
        dbFirestore.collection(Constants.COLLECTION_USERS)
            // Document ID for users fields. Here the document it is the User ID.
            .document(userInfo.userId)
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge later on instead of replacing the fields.
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {

                // Here call a function of base activity for transferring the result to it.
                activity.userRegistrationSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while registering the user.",
                    e
                )
            }
    }

    /**
     * A function to get the tables list from cloud firestore.
     *
     * @param activity The fragment is passed as parameter as the function is called from fragment and need to the success result.
     */
    fun getTablesList(activity: Activity) {
        // The collection name for PRODUCTS
        dbFirestore.collection(Constants.COLLECTION_TABLES)
            .orderBy(Constants.FIELD_NUMBER, Query.Direction.ASCENDING)
            .get() // Will get the documents snapshots.
            .addOnSuccessListener { document ->

                // Here we get the list of boards in the form of documents.
                Log.d("Tables List", document.documents.toString())

                // Here we have created a new instance for Products ArrayList.
                val tablesList: ArrayList<MultiplicationTable> = ArrayList()

                // A for loop as per the list of documents to convert them into Products ArrayList.
                for (i in document.documents) {

                    val table = i.toObject(MultiplicationTable::class.java)
                    table!!.tableId = i.id

                    tablesList.add(table)
                }
                when (activity) {
                    is TablesListActivity -> {
                        activity.successTablesList(tablesList)
                    }
                    else -> {}
                }
            }
            .addOnFailureListener { e ->
                Log.e("Get Tables List", "Error while getting tables list.", e)
            }
    }

    fun doesUserHaveLogs(activity: Activity) {
        dbFirestore.collection(Constants.COLLECTION_USER_LOGS)
            .whereEqualTo(Constants.FIELD_USER_ID, getCurrentUserID())
            .whereNotEqualTo(Constants.FIELD_TYPE, Constants.TYPE_SOLVED)
            .get()
            .addOnSuccessListener {
                when (activity) {
                    is TestActivity -> {
                        if (it.size() > 0)
                            activity.successUserHasLogs(true)
                        else
                            activity.successUserHasLogs(false)
                    }
                }
            }
            .addOnFailureListener { e ->
                when (activity) {
                    is TestActivity -> {
                        activity.hideProgressDialog()
                    }
                }
                Log.e("", "", e)
            }
    }

    /**
     * A function to get the user logs list from cloud firestore.
     *
     * @param activity The fragment is passed as parameter as the function is called from fragment and need to the success result.
     */
    fun getUserLogEntries(activity: Activity, sortBy: String) {
        // The collection name for User Logs
        dbFirestore.collection(Constants.COLLECTION_USER_LOGS)
            .whereEqualTo(Constants.FIELD_USER_ID, getCurrentUserID())
            .orderBy(sortBy, Query.Direction.ASCENDING)
            .get() // Will get the documents snapshots.
            .addOnSuccessListener { document ->

                // Here we get the list of boards in the form of documents.
                Log.d("User Logs List", document.documents.toString())

                // Here we have created a new instance for user logs ArrayList.
                val userLogsList: ArrayList<UserLog> = ArrayList()

                // A for loop as per the list of documents to convert them into user logs ArrayList.
                for (i in document.documents) {

                    val userLog = i.toObject(UserLog::class.java)
                    userLog!!.logId = i.id

                    userLogsList.add(userLog)
                }
                when (activity) {
                    is UserLogsListActivity -> {
                        activity.successUserLogsFromFireStore(userLogsList)
                    }
                    else -> {}
                }
            }
            .addOnFailureListener { e ->
                when (activity) {
                    is UserLogsListActivity -> {
                        activity.hideLogs()
                    }
                    else -> {}
                }
                Log.e("Get User Logs List", "Error while getting user logs list.", e)
            }
    }

    fun getRandomUserNegativeLogEntryGreater(activity: Activity) {
        dbFirestore.collection(Constants.COLLECTION_USER_LOGS)
            .whereEqualTo(Constants.FIELD_USER_ID, getCurrentUserID())
            .whereNotEqualTo(Constants.FIELD_TYPE, Constants.TYPE_SOLVED)
            .orderBy(Constants.FIELD_TYPE, Query.Direction.ASCENDING)
            .orderBy(Constants.FIELD_RANDOM, Query.Direction.ASCENDING)
            .limit(1)
            .get() // Will get the documents snapshots.
            .addOnSuccessListener { document ->

                val log = document.documents[0].toObject(UserLog::class.java)!!

                when (activity) {
                    is TestActivity -> {
                        activity.successLogFromFireStore(log)
                    }
                    else -> {}
                }
            }
            .addOnFailureListener { e ->
                when (activity) {
                    is TestActivity -> {
                        activity.hideProgressDialog()
                    }
                    else -> {}
                }
                Log.e("Get User Log", "Error while getting user log.", e)
            }
    }

    fun getRandomUserNegativeLogEntryLess(activity: Activity) {
        dbFirestore.collection(Constants.COLLECTION_USER_LOGS)
            .whereEqualTo(Constants.FIELD_USER_ID, getCurrentUserID())
            .whereNotEqualTo(Constants.FIELD_TYPE, Constants.TYPE_SOLVED)
            .orderBy(Constants.FIELD_TYPE, Query.Direction.DESCENDING)
            .orderBy(Constants.FIELD_RANDOM, Query.Direction.DESCENDING)
            .limit(1)
            .get() // Will get the documents snapshots.
            .addOnSuccessListener { document ->

                val log = document.documents[0].toObject(UserLog::class.java)!!

                when (activity) {
                    is TestActivity -> {
                        activity.successLogFromFireStore(log)
                    }
                    else -> {}
                }
            }
            .addOnFailureListener { e ->
                when (activity) {
                    is TestActivity -> {
                        activity.hideProgressDialog()
                    }
                    else -> {}
                }
                Log.e("Get User Log", "Error while getting user log.", e)
            }
    }

    fun deleteLogEntry(activity: Activity, numFirst: Int, numSecond: Int) {

        // Collection name address.
        dbFirestore.collection(Constants.COLLECTION_USER_LOGS)
            .whereEqualTo(Constants.FIELD_USER_ID, getCurrentUserID())
            .whereEqualTo(Constants.FIELD_NUM_FIRST, numFirst)
            .whereEqualTo(Constants.FIELD_NUM_SECOND, numSecond)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful)
                    for (document in it.result!!) {
                        dbFirestore.collection(Constants.COLLECTION_USER_LOGS)
                            .document(document.id)
                            .delete()
                    }
            }
            .addOnFailureListener { e ->
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while adding the log entry.",
                    e
                )
            }
    }

    fun addLogEntry(activity: Activity, userLog: UserLog) {

        // Collection name address.
        dbFirestore.collection(Constants.COLLECTION_USER_LOGS)
            .document()
            // Here the userLog are Field and the SetOption is set to merge. It is for if we wants to merge
            .set(userLog, SetOptions.merge())
            .addOnFailureListener { e ->
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while adding the log entry.",
                    e
                )
            }
    }

    fun updateUserProfileData(activity: Activity, userHashMap: HashMap<String, Any>) {
        // Collection Name
        dbFirestore.collection(Constants.COLLECTION_USERS)
            // Document ID against which the data to be updated. Here the document id is the current logged in user id.
            .document(getCurrentUserID())
            // A HashMap of fields which are to be updated.
            .update(userHashMap)
            .addOnSuccessListener {

                // Notify the success result.
                when (activity) {
                    is EditProfileDetailsActivity -> {
                        // Call a function of base activity for transferring the result to it.
                        activity.userProfileUpdateSuccess()
                    }
                }
            }
            .addOnFailureListener { e ->
                when (activity) {
                    is EditProfileDetailsActivity -> {
                        // Hide the progress dialog if there is any error. And print the error in log.
                        activity.hideProgressDialog()
                    }
                }

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updating the user details.",
                    e
                )
            }
    }

}