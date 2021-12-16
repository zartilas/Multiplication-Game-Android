package com.unipi.p17172p17168p17164.multiplicationgame.ui.activities

import android.content.Intent
import android.os.Bundle
import com.unipi.p17172p17168p17164.multiplicationgame.R
import com.unipi.p17172p17168p17164.multiplicationgame.databinding.ActivityMainMenuBinding
import com.unipi.p17172p17168p17164.multiplicationgame.utils.CustomDialog

class MainMenuActivity : BaseActivity() {
    // ~~~~~~~~ VARIABLES ~~~~~~~~
    private lateinit var binding: ActivityMainMenuBinding
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        setupUI()
    }

    private fun setupUI() {
        binding.apply {
            btnLearn.setOnClickListener {
                playButtonPressSound(this@MainMenuActivity)
                val intent = Intent(this@MainMenuActivity, LearnActivity::class.java)
                startActivity(intent)
            }
            btnStartTest.setOnClickListener {
                playButtonPressSound(this@MainMenuActivity)
                goToTestActivity(this@MainMenuActivity)
            }
            btnTables.setOnClickListener {
                playButtonPressSound(this@MainMenuActivity)
                val intent = Intent(this@MainMenuActivity, TablesListActivity::class.java)
                startActivity(intent)
            }
            btnLogs.setOnClickListener {
                playButtonPressSound(this@MainMenuActivity)
                val intent = Intent(this@MainMenuActivity, UserLogsListActivity::class.java)
                startActivity(intent)
            }
            imgBtnProfile.setOnClickListener {
                playButtonPressSound(this@MainMenuActivity)
                val intent = Intent(this@MainMenuActivity, ProfileDetailsActivity::class.java)
                startActivity(intent)
            }
            imgBtnHelp.setOnClickListener {
                playButtonPressSound(this@MainMenuActivity)
                CustomDialog().showTip(this@MainMenuActivity, getString(R.string.txt_help_title), getString(R.string.txt_main_help))
            }
        }
    }

    override fun onBackPressed() {
        doubleBackToExit()
    }
}