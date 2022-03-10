package com.example.pinterest.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.pinterest.fragments_all.home.HomeFragment
import com.example.pinterest.fragments_all.message.ChatFragment
import com.example.pinterest.fragments_all.profile.ProfileFragment
import com.example.pinterest.fragments_all.search.SearchFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {

            0 -> {
                HomeFragment()
            }
            1 -> {
                SearchFragment()
            }
            2 -> {
                ChatFragment()
            }
            3 -> {
                ProfileFragment()
            }
            else -> {
                Fragment()
            }
        }
    }
}
