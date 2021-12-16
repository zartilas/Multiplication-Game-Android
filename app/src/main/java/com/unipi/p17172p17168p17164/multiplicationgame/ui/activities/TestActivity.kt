package com.unipi.p17172p17168p17164.multiplicationgame.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.MenuItem
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.content.res.AppCompatResources
import com.unipi.p17172.emarket.utils.SnackBarErrorClass
import com.unipi.p17172p17168p17164.multiplicationgame.R
import com.unipi.p17172p17168p17164.multiplicationgame.database.FirestoreHelper
import com.unipi.p17172p17168p17164.multiplicationgame.databinding.ActivityTestBinding
import com.unipi.p17172p17168p17164.multiplicationgame.models.UserLog
import com.unipi.p17172p17168p17164.multiplicationgame.utils.Constants
import com.unipi.p17172p17168p17164.multiplicationgame.utils.CustomDialog
import com.unipi.p17172p17168p17164.multiplicationgame.utils.Utils


class TestActivity : BaseActivity() {
    // ~~~~~~~~ VARIABLES ~~~~~~~~
    private lateinit var binding: ActivityTestBinding
    private var numFirst: Int = 0
    private var numSecond: Int = 0
    private var correctAnswers: Int = 0
    private var wrongAnswers: Int = 0
    private var correctResult: Int = -1

    private lateinit var state: String
    private var isFromLogs: Boolean = false

    private lateinit var timer: CountDownTimer
    private var timerState = TimerState.Running
    var millisInFuture: Long = Constants.DEFAULT_TEST_TIMER_DELAY //60 seconds
    var countDownInterval: Long = 1000 //1 second
    //Declare a variable to hold CountDownTimer remaining time
    private var timeRemaining: Long = Constants.DEFAULT_TEST_TIMER_DELAY

    enum class TimerState {
        Stopped, Paused, Running, Finished
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)

        init()
        setContentView(binding.root)
    }

    private fun init() {
        checkIntentExtras()
        initEquationNumbers()
        setupUI()
        startTimer()
    }

    private fun checkIntentExtras() {
        intent.apply {
            if (hasExtra(Constants.EXTRA_TIME_REMAINING))
                timeRemaining = extras!!.getLong(Constants.EXTRA_TIME_REMAINING)
            if (hasExtra(Constants.EXTRA_CORRECT_ANSWERS))
                correctAnswers = extras!!.getInt(Constants.EXTRA_CORRECT_ANSWERS)
            if (hasExtra(Constants.EXTRA_WRONG_ANSWERS))
                wrongAnswers = extras!!.getInt(Constants.EXTRA_WRONG_ANSWERS)
        }
    }

    private fun checkIfUserHasLogs() {

        showProgressDialog()
        pauseTimer()

        FirestoreHelper().doesUserHaveLogs(this)
    }


    private fun loadLogs() {

        if (Utils().generateRandomNumber(0, 10) > 5)
            FirestoreHelper().getRandomUserNegativeLogEntryGreater(this)
        else
            FirestoreHelper().getRandomUserNegativeLogEntryLess(this)
    }

    fun successLogFromFireStore(userLog: UserLog) {

        // Hide the progress dialog
        hideProgressDialog()
        startTimer()
        isFromLogs = true

        setEquation(userLog)
    }

    fun successUserHasLogs(isTrue: Boolean) {

        if (isTrue)
            loadLogs()
        else {
            hideProgressDialog()

            numFirst = Utils().generateRandomNumber(1, 10)
            numSecond = Utils().generateRandomNumber(1, 10)

            setEquation(null)
        }
    }

    private fun initEquationNumbers() {
        if (Utils().generateRandomNumber(0, 10) > 5) {
            checkIfUserHasLogs()
        }
        else {
            numFirst = Utils().generateRandomNumber(1, 10)
            numSecond = Utils().generateRandomNumber(1, 10)
            setEquation(null)
        }
    }

    private fun setEquation(userLog: UserLog?) {
        if (userLog != null) {
            numFirst = userLog.numFirst
            numSecond = userLog.numSecond
        }

        correctResult = numFirst * numSecond

        binding.apply {
            txtViewNumberFirst.text = numFirst.toString()
            txtViewNumberSecond.text = numSecond.toString()
            toolbar.textViewLabel.text = String.format(getString(R.string.txt_equation_format),
                numFirst,
                numSecond
            )
        }
    }

    private fun startTimer() {

        // Basically, if timer state is paused, we want to set the milliseconds to the
        // previous one before it was paused.
        if (timerState == TimerState.Paused) {
            millisInFuture = timeRemaining
        }
        else if (timerState == TimerState.Stopped)
            millisInFuture = Constants.DEFAULT_TABLE_TEST_TIMER_DELAY

        timerState = TimerState.Running

        //Initialize a new CountDownTimer instance
        timer = object : CountDownTimer(timeRemaining, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                //do something in every tick
                if (timerState == TimerState.Paused
                    || timerState == TimerState.Stopped) {
                    //If the user request to cancel or paused the
                    //CountDownTimer we will cancel the current instance
                    cancel()
                }
                else {
                    //Display the remaining seconds to app interface
                    //1 second = 1000 milliseconds
                    binding.apply {
                        val secondsRemaining = millisUntilFinished / 1000
                        val currentSeconds = millisInFuture / 1000
                        val minutes = secondsRemaining / 60
                        val seconds = secondsRemaining % 60

                        progressBarTimer.progress = secondsRemaining.toInt()

                        if (currentSeconds / 2 == secondsRemaining) {
                            progressBarTimer.progressDrawable = AppCompatResources.getDrawable(this@TestActivity, R.drawable.progress_bar_middle)
                            txtViewTimerValue.setTextColor(getColor(R.color.colorYellow))
                        }
                        else if (currentSeconds / 4 == secondsRemaining) {
                            progressBarTimer.progressDrawable = AppCompatResources.getDrawable(this@TestActivity, R.drawable.progress_bar_low)
                            txtViewTimerValue.setTextColor(getColor(R.color.colorRedLight))
                        }

                        txtViewTimerValue.text =
                            String.format(
                                getString(R.string.txt_timer_format),
                                minutes,
                                seconds
                            )
                    }
                    //Put count down timer remaining time in a variable
                    timeRemaining = millisUntilFinished
                }
            }
            override fun onFinish() {
                //Do something when count down finished
                stopTimer()
                binding.progressBarTimer.progress = 0
                timerState = TimerState.Finished
                CustomDialog().showTestResults(this@TestActivity, correctAnswers.toString(), wrongAnswers.toString())
                return
            }
        }
        timer.start()
    }

    fun checkAnswer(skip: Boolean) {
        if (skip)
            state = Constants.TYPE_SKIP
        else if (timerState == TimerState.Finished)
            state = Constants.TYPE_TIME_UP
        else {
            binding.apply {
                val userAnswer = txtInput.text.toString().trim { it <= ' ' }.toInt()
                if (userAnswer == correctResult) {
                    state = Constants.TYPE_SOLVED
                    CustomDialog().showCorrectAnswerDialog(this@TestActivity)
                    playPositiveSound(this@TestActivity)
                    correctAnswers++
                    if (isFromLogs)
                        FirestoreHelper().deleteLogEntry(this@TestActivity, numFirst, numSecond)
                }
                else {
                    state = Constants.TYPE_MISTAKE
                    CustomDialog().showWrongAnswerDialog(this@TestActivity)
                    playNegativeSound(this@TestActivity)
                    wrongAnswers++
                }
            }
        }
        stopTimer()
    }

    fun goToNextEquation() {
        val userLog = UserLog(
            FirestoreHelper().getCurrentUserID(),
            state,
            Utils().generateRandomNumber(1, 20000),
            numFirst,
            numSecond
        )
        FirestoreHelper().addLogEntry(this, userLog)

        // If next number of table is not bigger than the limit e.x. bigger than 10
        val intent = Intent(this@TestActivity, TestActivity::class.java)
        intent.putExtra(Constants.EXTRA_TIME_REMAINING, timeRemaining)
        intent.putExtra(Constants.EXTRA_CORRECT_ANSWERS, correctAnswers)
        intent.putExtra(Constants.EXTRA_WRONG_ANSWERS, wrongAnswers)
        finish()
        startActivity(intent)
    }

    private fun pauseTimer() {
        timerState = TimerState.Paused
        //startTimer()
    }

    private fun stopTimer() {
        timerState = TimerState.Stopped
        timer.cancel()
    }

    /*override fun onResume() {
        super.onResume()

        startTimer()
    }

    override fun onPause() {
        super.onPause()

        timerState = TimerState.Paused
        pauseTimer()
    }

    override fun onStop() {
        super.onStop()

        stopTimer()
    }*/

    override fun onStop() {
        super.onStop()

        stopTimer()
    }

    private fun validateFields(): Boolean {
        binding.apply {
            return when {
                TextUtils.isEmpty(txtInput.text.toString().trim { it <= ' ' }) -> {
                    SnackBarErrorClass
                        .make(root, getString(R.string.txt_error_empty_answer))
                        .show()
                    txtInputLayout.requestFocus()
                    txtInputLayout.error = getString(R.string.txt_error_empty_answer)
                    playNegativeSound(this@TestActivity)
                    btnNext.startAnimation(AnimationUtils.loadAnimation(
                        this@TestActivity, R.anim.anim_shake))
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
            // Let's set the max progress value to the actual starting timer.
            progressBarTimer.max = (millisInFuture / 1000).toInt()
            txtInput.setOnFocusChangeListener { v, hasFocus ->
                if (!hasFocus)
                    Utils().hideSoftKeyboard(this@TestActivity, v)
            }
            txtInput.addTextChangedListener(object: TextWatcher {
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    txtInputLayout.isErrorEnabled = false
                }
                // Not Needed
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun afterTextChanged(s: Editable?) {}
            })
        }

        // start/exit activity transitions
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left)
    }

    private fun setupClickListeners() {
        binding.apply {
            btnNext.setOnClickListener {
                playButtonPressSound(this@TestActivity)
                if (validateFields()) {
                    checkAnswer(false)
                }
            }
            btnClear.setOnClickListener {
                playButtonPressSound(this@TestActivity)
                txtInput.setText("")
            }
            imgViewArrowLeft.setOnClickListener{
                playButtonPressSound(this@TestActivity)
                // Focus the text input box
                txtInputLayout.requestFocus()
                // Hide soft keyboard
                val imm: InputMethodManager =
                    getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.toggleSoftInput(
                    InputMethodManager.SHOW_FORCED,
                    InputMethodManager.HIDE_IMPLICIT_ONLY
                )
            }
            fltBtnHelp.setOnClickListener {
                playButtonPressSound(this@TestActivity)
                CustomDialog().showTip(this@TestActivity,
                    getString(R.string.txt_help_title), getString(R.string.txt_help_test))
            }
        }
    }

    private fun setupActionBar() {
        binding.toolbar.apply {
            setSupportActionBar(root)
            // Set action bar title format to "Z x Y"
            textViewLabel.text = String.format(getString(R.string.txt_equation_format),
                numFirst,
                numSecond
            )
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
        exitDialog()
    }

    private fun exitDialog() {
        playButtonPressSound(this@TestActivity)
        CustomDialog().showExitConfirmation(this@TestActivity)
    }
}