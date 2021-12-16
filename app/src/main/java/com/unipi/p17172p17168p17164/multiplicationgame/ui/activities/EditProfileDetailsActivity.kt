package com.unipi.p17172p17168p17164.multiplicationgame.ui.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.animation.AnimationUtils
import com.unipi.p17172.emarket.utils.SnackBarErrorClass
import com.unipi.p17172p17168p17164.multiplicationgame.R
import com.unipi.p17172p17168p17164.multiplicationgame.database.FirestoreHelper
import com.unipi.p17172p17168p17164.multiplicationgame.databinding.ActivityEditProfileDetailsBinding
import com.unipi.p17172p17168p17164.multiplicationgame.models.User
import com.unipi.p17172p17168p17164.multiplicationgame.utils.Constants
import com.unipi.p17172p17168p17164.multiplicationgame.utils.SnackBarSuccessClass
import com.unipi.p17172p17168p17164.multiplicationgame.utils.Utils


class EditProfileDetailsActivity : BaseActivity() {
    // ~~~~~~~~ VARIABLES ~~~~~~~~
    private lateinit var binding: ActivityEditProfileDetailsBinding
    private lateinit var modelUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        checkIntentExtras()
        setupUI()
    }

    private fun checkIntentExtras() {
        intent.apply {
            modelUser = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }
    }

    /**
     * A function to update user profile details to the firestore.
     */
    private fun updateUserProfileDetails() {

        showProgressDialog()

        val userHashMap = HashMap<String, Any>()

        binding.apply {
            val fullName = inputTxtFullName.text.toString()

            if (fullName != modelUser.fullName)
                userHashMap[Constants.FIELD_FULL_NAME] = fullName
        }

        // call the registerUser function of FireStore class to make an entry in the database.
        FirestoreHelper().updateUserProfileData(
            this,
            userHashMap
        )
    }

    /**
     * A function to notify the success result and proceed further accordingly after updating the user details.
     */
    fun userProfileUpdateSuccess() {

        // Hide the progress dialog
        hideProgressDialog()

        finish()
        goToProfileDetailsActivity(this, true)
    }

    private fun validateFields(): Boolean {
        binding.apply {
            return when {
                TextUtils.isEmpty(inputTxtFullName.text.toString().trim { it <= ' ' }) -> {
                    SnackBarErrorClass
                        .make(root, getString(R.string.txt_error_empty_full_name))
                        .show()
                    inputTxtLayoutFullName.requestFocus()
                    inputTxtLayoutFullName.error = getString(R.string.txt_error_empty_full_name)
                    btnSave.startAnimation(AnimationUtils.loadAnimation(
                        this@EditProfileDetailsActivity, R.anim.anim_shake))
                    false
                }
                else -> true
            }
        }
    }

    private fun setupUI() {
        setupActionBar()
        setupClickListeners()

        binding.apply {
            inputTxtFullName.run {
                setText(modelUser.fullName)
                setOnFocusChangeListener { v, hasFocus ->
                    if (!hasFocus)
                        Utils().hideSoftKeyboard(this@EditProfileDetailsActivity, v)
                }
                addTextChangedListener(object: TextWatcher {
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        inputTxtLayoutFullName.isErrorEnabled = false
                    }
                    // Not Needed
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                    override fun afterTextChanged(s: Editable?) {}
                })
            }
        }

        // start/exit activity transitions
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left)
    }

    private fun setupClickListeners() {
        binding.apply {
            btnSave.setOnClickListener {
                playButtonPressSound(this@EditProfileDetailsActivity)
                if (validateFields()) {
                    updateUserProfileDetails()
                }
            }
        }
    }

    private fun setupActionBar() {
        binding.toolbar.apply {
            setSupportActionBar(root)
            textViewLabel.text = getString(R.string.txt_edit_profile)
        }

        val actionBar = supportActionBar
        actionBar?.let {
            it.setDisplayShowCustomEnabled(true)
            it.setDisplayHomeAsUpEnabled(true) // Enable back button
            it.setHomeAsUpIndicator(R.drawable.ic_chevron_left_24dp) // Set custom back button icon
        }
    }
}