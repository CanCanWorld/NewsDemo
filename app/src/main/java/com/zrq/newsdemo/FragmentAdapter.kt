package com.zrq.newsdemo

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


class FragmentAdapter(
    fm: FragmentManager,
    private val mFragment: List<Fragment>,
    private val mTitles: List<String>
) :
    FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return mFragment[position]
    }

    override fun getCount(): Int {
        return mFragment.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return mTitles[position]
    }
}
