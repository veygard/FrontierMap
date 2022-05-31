package com.veygard.frontiermap.presentation.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.veygard.frontiermap.R
import com.veygard.frontiermap.databinding.FragmentMainScreenBinding
import com.veygard.frontiermap.presentation.viewModel.MainScreenViewModel
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainScreenFragment : Fragment(R.layout.fragment_main_screen) {
    private val binding: FragmentMainScreenBinding by viewBinding()
    private val viewModel: MainScreenViewModel by viewModel()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mapview.map.move(
            CameraPosition(Point(59.945933, 30.320045), 14.0f, 1.0f, 1.0f),
            Animation(Animation.Type.SMOOTH, 3f),
            null
        )
        viewModel.getRussia()
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        binding.mapview.onStart()
    }
    override fun onStop() {
        super.onStop()
        MapKitFactory.getInstance().onStop()
        binding.mapview.onStop()
    }
}