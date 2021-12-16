package com.unipi.p17172p17168p17164.multiplicationgame.utils

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import com.unipi.p17172p17168p17164.multiplicationgame.R
import com.unipi.p17172p17168p17164.multiplicationgame.databinding.*
import com.unipi.p17172p17168p17164.multiplicationgame.ui.activities.TableResultActivity
import com.unipi.p17172p17168p17164.multiplicationgame.ui.activities.TestActivity
import com.unipi.p17172p17168p17164.multiplicationgame.ui.activities.UserLogsListActivity
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class CustomDialog {

    fun showTip(activity: Activity, title: String, body: String) {
        val dialog = Dialog(activity)
        val binding = DialogTipBinding.inflate(LayoutInflater.from(activity))

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCanceledOnTouchOutside(true)
        dialog.window?.setWindowAnimations(R.style.DialogAnimation)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

        dialog.setContentView(binding.root)

        binding.apply {
            textViewHeader.text = title
            textViewBody.text = body
            btnDismiss.setOnClickListener {
                dialog.dismiss()
                when (activity) {
                    is TableResultActivity -> {
                        if (!activity.isFinishing())
                            activity.playButtonPressSound(activity)
                    }
                }
            }
        }
        dialog.show()
    }

    fun showExitConfirmation(activity: Activity) {
        val dialog = Dialog(activity)
        val binding = DialogExitBinding.inflate(LayoutInflater.from(activity))

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCanceledOnTouchOutside(true)
        dialog.window?.setWindowAnimations(R.style.DialogAnimation)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dialog.window?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
        )

        dialog.setContentView(binding.root)

        binding.apply {
            btnDismiss.setOnClickListener { dialog.dismiss() }
            btnYes.setOnClickListener {
                dialog.dismiss()
                when (activity) {
                    is TableResultActivity -> {
                        if (!activity.isFinishing()) {
                            activity.playButtonPressSound(activity)
                            activity.finish()
                            activity.overridePendingTransition(R.anim.anim_slide_in_right,
                                R.anim.anim_slide_out_right)
                        }
                    }
                    is TestActivity -> {
                        if (!activity.isFinishing()) {
                            activity.playButtonPressSound(activity)
                            activity.finish()
                            activity.overridePendingTransition(R.anim.anim_slide_in_right,
                                R.anim.anim_slide_out_right)
                        }
                    }
                }
            }
        }

        dialog.show()
    }

    fun showSkipConfirmation(activity: Activity) {
        val dialog = Dialog(activity)
        val binding = DialogSkipBinding.inflate(LayoutInflater.from(activity))

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.window?.setWindowAnimations(R.style.DialogAnimation)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

        dialog.setContentView(binding.root)

        binding.apply {
            btnDismiss.setOnClickListener { dialog.dismiss() }
            btnYes.setOnClickListener {
                when (activity) {
                    is TableResultActivity -> {
                        activity.playButtonPressSound(activity)
                        activity.checkAnswer(true)
                        activity.goToNextEquation()
                    }
                    is TestActivity -> {
                        activity.playButtonPressSound(activity)
                        activity.checkAnswer(true)
                        activity.goToNextEquation()
                    }
                }
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    fun showTestResults(activity: Activity, correctAnswers: String, wrongAnswers: String) {
        val dialog = Dialog(activity)
        val binding = DialogTestResultsBinding.inflate(LayoutInflater.from(activity))

        // Dialog Properties
        dialog.run {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCanceledOnTouchOutside(false)
            window?.apply {
                // Add some show/hide dialog animations.
                setWindowAnimations(R.style.DialogAnimation)
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                )
            }

            setContentView(binding.root)

            // Set dialog title and body and also add click event listeners.
            binding.apply {
                textViewCorrectAnswersValue.text = correctAnswers
                textViewWrongAnswersValue.text = wrongAnswers
                btnDismiss.setOnClickListener {
                    dialog.dismiss()
                    when (activity) {
                        is TableResultActivity -> {
                            if (!activity.isFinishing()) {
                                activity.finish()
                                activity.overridePendingTransition(R.anim.anim_slide_in_right,
                                    R.anim.anim_slide_out_right)
                            }

                        }
                        is TestActivity -> {
                            if (!activity.isFinishing()) {
                                activity.finish()
                                activity.overridePendingTransition(R.anim.anim_slide_in_right,
                                    R.anim.anim_slide_out_right)
                            }
                        }
                    }
                }
            }

            // Show dialog
            show()
        }
    }


    fun showTimeOut(activity: Activity) {
        val dialog = Dialog(activity)
        val binding = DialogTimeOutBinding.inflate(LayoutInflater.from(activity))

        // Dialog Properties
        dialog.run {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCanceledOnTouchOutside(false)
            window?.apply {
                // Add some show/hide dialog animations.
                setWindowAnimations(R.style.DialogAnimation)
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                // Remove the dim/shadow background behind the dialog.
                clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                )
            }

            setContentView(binding.root)

            // Set click event listeners.
            binding.apply {
                btnDismiss.setOnClickListener {
                    when (activity) {
                        is TableResultActivity -> {
                            if (!activity.isFinishing()) {
                                activity.playButtonPressSound(activity)
                                dismiss()
                            }

                        }
                        is TestActivity -> {
                            if (!activity.isFinishing()) {
                                activity.playButtonPressSound(activity)
                                dismiss()
                            }

                        }
                    }
                }
            }

            // Show dialog
            show()

            // Create an executor that executes tasks in a background thread.
            val backgroundExecutor: ScheduledExecutorService =
                Executors.newSingleThreadScheduledExecutor()

            // Execute a task in the background thread after X seconds.
            backgroundExecutor.schedule({
                dismiss()
            }, 10, TimeUnit.SECONDS)

            // When the dialog is dismiss go to next equation.
            setOnDismissListener {
                when (activity) {
                    is TableResultActivity -> {
                        if (!activity.isFinishing()) {
                            activity.checkAnswer(false)
                            activity.goToNextEquation()
                        }

                    }
                    is TestActivity -> activity.finish()
                }
            }
        }
    }

    fun showFilterDialog(activity: Activity) {
        val dialog = Dialog(activity)
        val binding = DialogFilterBinding.inflate(LayoutInflater.from(activity))

        // Dialog Properties
        dialog.run {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCanceledOnTouchOutside(true)
            setCancelable(true)
            window?.apply {
                // Add some show/hide dialog animations.
                setWindowAnimations(R.style.DialogAnimation)
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }

            setContentView(binding.root)

            // Set click event listeners.
            binding.apply {
                btnSort.setOnClickListener {
                    when (activity) {
                        is UserLogsListActivity -> {
                            activity.playButtonPressSound(activity)
                            radioGroup.checkedRadioButtonId.apply {
                                when (this) {
                                    0 -> activity.loadUserLogs(Constants.FIELD_DATE_ADDED)
                                    else -> activity.loadUserLogs(Constants.FIELD_TYPE)
                                }
                            }
                        }
                    }
                    dismiss()
                }
                btnDismiss.setOnClickListener { dismiss() }
            }

            // Show dialog
            show()
        }
    }

    fun showWrongAnswerDialog(activity: Activity) {
        val dialog = Dialog(activity)
        val binding = DialogWrongAnswerBinding.inflate(LayoutInflater.from(activity))

        // Dialog Properties
        dialog.run {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCanceledOnTouchOutside(false)
            window?.apply {
                // Add some show/hide dialog animations.
                setWindowAnimations(R.style.DialogAnimation)
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                )
            }

            setContentView(binding.root)

            // Set click event listeners.
            binding.apply {
                btnDismiss.setOnClickListener {
                    when (activity) {
                        is TableResultActivity -> {
                            activity.playButtonPressSound(activity)
                        }
                        is TestActivity -> {
                            activity.playButtonPressSound(activity)
                        }
                    }
                    dismiss()
                }
            }

            // Create an executor that executes tasks in a background thread.
            val backgroundExecutor: ScheduledExecutorService =
                Executors.newSingleThreadScheduledExecutor()

            // Execute a task in the background thread after X seconds.
            backgroundExecutor.schedule({
                dismiss()
            }, 5, TimeUnit.SECONDS)

            // When the dialog is dismiss go to next equation.
            setOnDismissListener {
                when (activity) {
                    is TableResultActivity -> activity.goToNextEquation()
                    is TestActivity -> activity.goToNextEquation()
                }
            }

            // Show dialog
            show()
        }
    }

    fun showCorrectAnswerDialog(activity: Activity) {
        val dialog = Dialog(activity)
        val binding = DialogCorrectAnswerBinding.inflate(LayoutInflater.from(activity))

        // Dialog Properties
        dialog.run {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCanceledOnTouchOutside(false)
            window?.apply {
                // Add some show/hide dialog animations.
                setWindowAnimations(R.style.DialogAnimation)
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                )
            }

            setContentView(binding.root)

            // Set click event listeners.
            binding.apply {
                btnDismiss.setOnClickListener {
                    when (activity) {
                        is TableResultActivity -> activity.playButtonPressSound(activity)
                        is TestActivity -> activity.playButtonPressSound(activity)
                    }
                    dismiss()
                }
            }

            // Create an executor that executes tasks in a background thread.
            val backgroundExecutor: ScheduledExecutorService =
                Executors.newSingleThreadScheduledExecutor()

            // Execute a task in the background thread after X seconds.
            backgroundExecutor.schedule({
                dismiss()
            }, 5, TimeUnit.SECONDS)

            // When the dialog is dismiss go to next equation.
            setOnDismissListener {
                when (activity) {
                    is TableResultActivity -> activity.goToNextEquation()
                    is TestActivity -> activity.goToNextEquation()
                }
            }

            // Show dialog
            show()
        }
    }
}
