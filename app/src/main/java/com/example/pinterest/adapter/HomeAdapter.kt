package com.example.pinterest.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.pinterest.fragments_all.home.AllFragment
import com.example.pinterest.fragments_all.home.NatureFragment
import com.example.pinterest.fragments_all.home.WallPaperFragment

class HomeAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                AllFragment()
            }
            1 -> {
                WallPaperFragment()
            }
            2 -> {
                NatureFragment()
            }
            else -> Fragment()
        }
    }
}