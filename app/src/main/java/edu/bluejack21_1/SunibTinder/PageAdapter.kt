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
                return R.string.login.toString()
            }
            1 -> {
                return R.string.register.toString()
            }

        }
        return super.getPageTitle(position)
    }

}