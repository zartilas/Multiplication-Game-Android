package com.unipi.p17172p17168p17164.multiplicationgame.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.unipi.p17172p17168p17164.multiplicationgame.databinding.FragmentLearnSection2Binding


class LearnSectionSecondFragment : Fragment() {

    // ~~~~~~~VARIABLES~~~~~~~
    private var _binding: FragmentLearnSection2Binding? = null  // Scoped to the lifecycle of the fragment's view (between onCreateView and onDestroyView)
    private val binding get() = _binding!!
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLearnSection2Binding.inflate(inflater, container, false)

        init()

        return binding.root
    }

    private fun init() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}