package com.unipi.p17172p17168p17164.multiplicationgame.ui.activities

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import com.unipi.p17172p17168p17164.multiplicationgame.database.FirestoreHelper
import com.unipi.p17172p17168p17164.multiplicationgame.databinding.ActivitySplashBinding
import com.unipi.p17172p17168p17164.multiplicationgame.utils.Constants
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class SplashActivity : BaseActivity() {
    // ~~~~~~~~ VARIABLES ~~~~~~~~
    private lateinit var binding: ActivitySplashBinding
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        hideSystemUI() // Hides the status bar and title from android UI.
        moveToNextActivity() // Moves to next activity in a specific amount of time after loading.
    }

    private fun hideSystemUI() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
    }

    private fun moveToNextActivity() {
        // Create an executor that executes tasks in a background thread.
        val backgroundExecutor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()

        // Execute a task in the background thread after some seconds.
        backgroundExecutor.schedule({
            if (FirestoreHelper().getCurrentUserID() == "") {
                finish()
                goToSignInActivity(this@SplashActivity)
            }
            else {
                finish()
                goToMainActivityNoAnimation(this@SplashActivity)
            }
        }, Constants.SPLASH_SCREEN_DELAY, TimeUnit.MILLISECONDS)
    }
}