package com.veygard.frontiermap.presentation.screens

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import by.kirich1409.viewbindingdelegate.viewBinding
import com.veygard.frontiermap.R
import com.veygard.frontiermap.databinding.FragmentMainScreenBinding
import com.veygard.frontiermap.presentation.viewModel.MainScreenViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.osmdroid.bonuspack.kml.KmlDocument
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.FolderOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polygon
import org.osmdroid.views.overlay.compass.CompassOverlay
import java.io.File
import java.io.IOException
import java.io.InputStream


class MainScreenFragment : Fragment(R.layout.fragment_main_screen) {
    lateinit var map: MapView
    private val binding: FragmentMainScreenBinding by viewBinding()
    private val viewModel: MainScreenViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view=  inflater.inflate(R.layout.fragment_main_screen, container, false)

        return view
    }

    private fun observeData() {
        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            when (loading) {
                true -> {
                    binding.lottie.apply {
                        speed = 1.3f
                        repeatCount = 99
                        playAnimation()
                    }
                }
                false -> {
                    binding.lottie.cancelAnimation()
                    binding.lottie.visibility = View.GONE
                    binding.mapView.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getRussia(binding.mapView)
        observeData()
        initMap()
    }

    private fun initMap() {
        map = binding.mapView
        map.controller.setZoom(4.5)
        map.setMultiTouchControls(true)
        val point = GeoPoint(55.558741, 37.378847 )
        map.controller.setCenter(point)
    }

    override fun onStart() {
        super.onStart()
    }
    override fun onStop() {
        super.onStop()
    }

}