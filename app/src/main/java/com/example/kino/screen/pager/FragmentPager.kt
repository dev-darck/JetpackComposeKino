package com.example.kino.screen.pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.kino.OverviewFragment

class FragmentPager(fm: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fm, lifecycle) {

    var map: Map<Fragment, String>? = null

    override fun getItemCount(): Int = map?.size ?: 0

    override fun createFragment(position: Int): Fragment = OverviewFragment()

    override fun getItemViewType(position: Int): Int = super.getItemViewType(position)
}