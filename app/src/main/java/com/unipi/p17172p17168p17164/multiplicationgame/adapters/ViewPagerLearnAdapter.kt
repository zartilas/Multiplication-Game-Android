package com.unipi.p17172p17168p17164.multiplicationgame.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.unipi.p17172p17168p17164.multiplicationgame.ui.fragments.LearnSectionFirstFragment
import com.unipi.p17172p17168p17164.multiplicationgame.ui.fragments.LearnSectionFourthFragment
import com.unipi.p17172p17168p17164.multiplicationgame.ui.fragments.LearnSectionSecondFragment
import com.unipi.p17172p17168p17164.multiplicationgame.ui.fragments.LearnSectionThirdFragment

class ViewPagerLearnAdapter(fm: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fm, lifecycle) {

    private val fragmentsList:ArrayList<Fragment> = arrayListOf(
        LearnSectionFirstFragment(),
        LearnSectionSecondFragment(),
        LearnSectionThirdFragment(),
        LearnSectionFourthFragment()
    )

    override fun getItemCount(): Int {
        return fragmentsList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentsList[position]
    }
}