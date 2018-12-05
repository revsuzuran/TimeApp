/*
 * Created On : 12/5/18 2:56 PM
 * Author : Aqil Prakoso
 * Copyright (c) 2018 iRevStudio
 */

package com.irevstudio.timeapp.ui.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.ListFragment
import com.irevstudio.timeapp.R

import com.irevstudio.timeapp.R.id.*
import com.irevstudio.timeapp.ui.fragment.HomeFragment
import com.irevstudio.timeapp.ui.fragment.MenuFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nav_bar.setOnNavigationItemSelectedListener {
                item ->
            var fragment: Fragment? = null
            when(item.itemId){

                bottomNav_menu -> {
                    fragment = MenuFragment()
                }
                bottomNav_home -> {
                    fragment = HomeFragment()
                }
                bottomNav_list -> {
                    fragment = ListFragment()
                }

            }
            loadFragment(fragment)
            return@setOnNavigationItemSelectedListener(true)
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
