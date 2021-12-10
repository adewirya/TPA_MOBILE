package edu.bluejack21_1.SunibTinder

import android.content.Context
import android.content.res.Resources
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.google.common.io.Resources.getResource

class PageAdapter(fm:FragmentManager, c : Context) : FragmentPagerAdapter(fm){
    override fun getCount(): Int {
        return 2
    }

    var ctx = c
    lateinit var sharedPref : SharedPrefConfig

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> {
                LoginFragment()
            }
            1 -> {
                RegisterFragment()
            }
            else ->{
                LoginFragment()
            }
        }

    }
    override fun getPageTitle(position: Int): CharSequence? {

            when(position){
                0 -> {
                    return ctx.resources.getString(R.string.login)
                }
                1 -> {
                    return ctx.resources.getString(R.string.register)
                }

            }

        return super.getPageTitle(position)
    }

}