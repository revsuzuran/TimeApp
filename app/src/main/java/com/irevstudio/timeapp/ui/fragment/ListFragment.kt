/*
 * Created On : 12/5/18 2:46 PM
 * Author : Aqil Prakoso
 * Copyright (c) 2018 iRevStudio
 */

package com.irevstudio.timeapp.ui.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.irevstudio.timeapp.R

class ListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }


}
