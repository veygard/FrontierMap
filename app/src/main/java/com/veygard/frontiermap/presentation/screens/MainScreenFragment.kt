package com.veygard.frontiermap.presentation.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.veygard.frontiermap.R
import com.veygard.frontiermap.databinding.FragmentMainScreenBinding
import com.yandex.mapkit.MapKitFactory

class MainScreenFragment : Fragment(R.layout.fragment_main_screen) {
    private val binding: FragmentMainScreenBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_screen, container, false)
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
    }
    override fun onStop() {
        super.onStop()
        MapKitFactory.getInstance().onStop()
    }
}