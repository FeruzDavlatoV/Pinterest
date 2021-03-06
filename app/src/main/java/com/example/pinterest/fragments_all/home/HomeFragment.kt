package com.example.pinterest.fragments_all.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.pinterest.R
import com.example.pinterest.adapter.HomeAdapter
import com.google.android.material.tabs.TabLayout


class HomeFragment : Fragment() {

    private lateinit var viewPager2: ViewPager2
    private lateinit var homeAdapter: HomeAdapter
    private lateinit var tabLayout: TabLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        initViews(view)
        return view
    }
    private fun initViews(view: View) {
        viewPager2 = view.findViewById(R.id.viewPager)
        tabLayout = view.findViewById(R.id.tabLayout)
        homeAdapter = HomeAdapter(childFragmentManager, lifecycle)
        viewPager2.adapter = homeAdapter

        tabLayout.addTab(tabLayout.newTab().setText("All"))
        tabLayout.addTab(tabLayout.newTab().setText("Wallpapers"))
        tabLayout.addTab(tabLayout.newTab().setText("Nature"))

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager2.currentItem = tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })
    }
}