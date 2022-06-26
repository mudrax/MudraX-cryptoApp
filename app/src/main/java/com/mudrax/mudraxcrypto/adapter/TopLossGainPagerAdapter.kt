package com.mudrax.mudraxcrypto.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mudrax.mudraxcrypto.fragment.TopLossGainFragment

class TopLossGainPagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2 //since we have just 2 fragments
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = TopLossGainFragment()
        val bundle = Bundle()
        bundle.putInt("position" , position)
        fragment.arguments = bundle
        return fragment

    }
}