/*
 * Created On : 12/5/18 2:56 PM
 * Author : Aqil Prakoso
 * Copyright (c) 2018 iRevStudio
 */

package com.irevstudio.timeapp.ui.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.irevstudio.timeapp.R
import com.irevstudio.timeapp.R.id.*
import com.irevstudio.timeapp.presenter.PengolahData
import com.irevstudio.timeapp.ui.fragment.HomeFragment
import com.irevstudio.timeapp.ui.fragment.ListFragment
import com.irevstudio.timeapp.ui.fragment.MenuFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        PengolahData.init()
        PengolahData.instance?.getAppData(
            PengolahData.instance!!.getTargetAppData(this,"com.whatsapp")
        )

        nav_bar.setOnNavigationItemSelectedListener {
                item ->
            var fragment: Fragment? = null
            when(item.itemId){


                bottomNav_list -> {
                    fragment = ListFragment()
                }
                bottomNav_menu -> {
                    fragment = MenuFragment()
                }
                bottomNav_home -> {
                    fragment = HomeFragment()
                }

            }
            loadFragment(fragment)
            return@setOnNavigationItemSelectedListener true
        }
        nav_bar.selectedItemId =  bottomNav_home


    }

    private fun loadFragment(fragment: Fragment?){

        if(fragment != null){
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .commit()

        }

    }

}
