package edu.bluejack21_1.SunibTinder

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class PageAdapter(fm:FragmentManager) : FragmentPagerAdapter(fm){
    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        when(position){
            0 -> {
                return LoginFragment()
            }
            1 -> {
                return RegisterFragment()
            }
            else ->{
                return LoginFragment()
            }
        }

    }

    override fun getPageTitle(position: Int): CharSequence? {
        when(position){
            0 -> {
                return "Login Page"
            }
            1 -> {
                return "Register Page"
            }
            else -> {
                return "bimbing"
            }

        }
        return super.getPageTitle(position)
    }

}