package com.unipi.p17172p17168p17164.multiplicationgame.ui.activities

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.unipi.p17172p17168p17164.multiplicationgame.R
import com.unipi.p17172p17168p17164.multiplicationgame.models.User
import com.unipi.p17172p17168p17164.multiplicationgame.utils.Constants
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

/**
 * A base activity class is used to define the functions and members which we will use in all the activities.
 * It inherits the AppCompatActivity class so in other activity class we will replace the AppCompatActivity with BaseActivity.
 */
open class BaseActivity : AppCompatActivity() {

    // Create an executor that executes tasks in a background thread.
    private val backgroundExecutor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()

    // A global variable for double back press feature.
    private var doubleBackToExitPressedOnce = false

    /**
     * This is a progress dialog instance which we will initialize later on.
     */
    private lateinit var mProgressDialog: Dialog

    /**
     * This function is used to show the progress dialog with the title and message to user.
     */
    fun showProgressDialog() {
        mProgressDialog = Dialog(this)

        /*Set the screen content from a layout resource.
        The resource will be inflated, adding all top-level views to the screen.*/
        mProgressDialog.setContentView(R.layout.dialog_progress)

        mProgressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)

        //Start the dialog and display it on screen.
        mProgressDialog.show()
    }

    /**
     * This function is used to dismiss the progress dialog if it is visible to user.
     */
    fun hideProgressDialog() {
        mProgressDialog.dismiss()
    }

    fun goToMainActivity(context: Context) {
        val intent = Intent(context, MainMenuActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    fun goToMainActivityNoAnimation(context: Context) {
        val intent = Intent(context, MainMenuActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    fun goToSignInActivity(context: Context) {
        val intent = Intent(context, SignInActivity::class.java)
        startActivity(intent)
    }

    fun goToEditProfileActivity(context: Context, user: User) {
        val intent = Intent(context, EditProfileDetailsActivity::class.java)
        intent.putExtra(Constants.EXTRA_USER_DETAILS, user)
        startActivity(intent)
    }

    fun goToProfileDetailsActivity(context: Context, showEditSuccessSnackBar: Boolean) {
        val intent = Intent(context, ProfileDetailsActivity::class.java)
        intent.putExtra(Constants.EXTRA_SUCCESS_SNACKBAR, showEditSuccessSnackBar)
        intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    fun goToTestActivity(context: Context) {
        val intent = Intent(context, TestActivity::class.java)
        startActivity(intent)
    }

    fun goToSignInActivity(context: Context, showRegisteredSnackBar: Boolean, userEmail: String) {
        val intent = Intent(context, SignInActivity::class.java)
        intent.putExtra(Constants.EXTRA_REG_USERS_SNACKBAR, showRegisteredSnackBar)
        intent.putExtra(Constants.EXTRA_USER_EMAIL, userEmail)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    /**
     * A function to implement the double back press feature to exit the app.
     */
    fun doubleBackToExit() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true

        Toast.makeText(
            this,
            resources.getString(R.string.txt_please_click_back_again_to_exit),
            Toast.LENGTH_SHORT
        ).show()

        backgroundExecutor.schedule({
            doubleBackToExitPressedOnce = false
        }, 2000, TimeUnit.MILLISECONDS)
    }

    fun playButtonPressSound(activity: Activity) {
        val player: MediaPlayer = MediaPlayer.create(activity, R.raw.button_press_click)
        player.isLooping = false
        player.setVolume(Constants.VOLUME_MEDIUM, Constants.VOLUME_MEDIUM)
        player.start()
        player.setOnCompletionListener { player.release() }
    }

    fun playNegativeSound(activity: Activity) {
        val player: MediaPlayer = MediaPlayer.create(activity, R.raw.alert_negative_error)
        player.isLooping = false
        player.setVolume(Constants.VOLUME_MAX, Constants.VOLUME_MAX)
        player.start()
        player.setOnCompletionListener { player.release() }
    }

    fun playPositiveSound(activity: Activity) {
        val player: MediaPlayer = MediaPlayer.create(activity, R.raw.alert_success)
        player.isLooping = false
        player.setVolume(Constants.VOLUME_MEDIUM, Constants.VOLUME_MEDIUM)
        player.start()
        player.setOnCompletionListener { player.release() }
    }
}