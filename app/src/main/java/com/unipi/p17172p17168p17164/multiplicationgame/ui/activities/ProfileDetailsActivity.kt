package com.unipi.p17172p17168p17164.multiplicationgame.ui.activities

import android.os.Bundle
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.unipi.p17172p17168p17164.multiplicationgame.R
import com.unipi.p17172p17168p17164.multiplicationgame.database.FirestoreHelper
import com.unipi.p17172p17168p17164.multiplicationgame.databinding.ActivityProfileDetailsBinding
import com.unipi.p17172p17168p17164.multiplicationgame.models.User
import com.unipi.p17172p17168p17164.multiplicationgame.utils.Constants
import com.unipi.p17172p17168p17164.multiplicationgame.utils.SnackBarSuccessClass
import java.util.*


class ProfileDetailsActivity : BaseActivity() {

    private lateinit var binding: ActivityProfileDetailsBinding
    private lateinit var modelUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileDetailsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        init()
    }

    private fun init() {
        if (intent.extras?.getBoolean(Constants.EXTRA_SUCCESS_SNACKBAR) == true) {
            SnackBarSuccessClass
                .make(binding.root,
                    getString(R.string.txt_success_done),
                    getString(R.string.txt_success_update_user))
                .show()
            overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_right)
        }

        loadProfileDetails()
        setupUI()
    }

    private fun loadProfileDetails() {

        showProgressDialog()

        FirestoreHelper().getUserDetails(this)
    }

    fun successProfileDetailsFromFirestore(user: User) {

        // Hide the progress dialog
        hideProgressDialog()

        modelUser = user

        binding.apply {
            // Populate the user details in the input texts.
            textViewFullName.text = modelUser.fullName
            textViewNameValue.text = modelUser.fullName
            textViewEmailValue.text = modelUser.email
            textViewDateRegisteredValue.text = Constants.DATE_FORMAT.format(modelUser.dateRegistered)
        }

    }

    private fun setupUI() {
        setupActionBar()
        binding.btnLogout.setOnClickListener{
            playButtonPressSound(this)
            FirebaseAuth.getInstance().signOut()
            goToSignInActivity(this)
        }
    }

    private fun setupActionBar() {
        binding.toolbar.apply {
            setSupportActionBar(root)
            textViewActionBarLabel.text = getString(R.string.txt_profile)
            imgBtnEdit.setOnClickListener {
                playButtonPressSound(this@ProfileDetailsActivity)
                goToEditProfileActivity(this@ProfileDetailsActivity, modelUser)
            }
        }

        val actionBar = supportActionBar
        actionBar?.let {
            it.setDisplayShowCustomEnabled(true)
            it.setDisplayHomeAsUpEnabled(true) // Enable back button
            it.setHomeAsUpIndicator(R.drawable.ic_chevron_left_24dp) // Set custom back button icon
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                playButtonPressSound(this)
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        finish()
        goToMainActivityNoAnimation(this)
    }
}