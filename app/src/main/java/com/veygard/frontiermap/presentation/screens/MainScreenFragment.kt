package com.veygard.frontiermap.presentation.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.veygard.frontiermap.R
import com.veygard.frontiermap.databinding.FragmentMainScreenBinding
import com.veygard.frontiermap.presentation.viewModel.MainScreenViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView


class MainScreenFragment : Fragment(R.layout.fragment_main_screen) {
    lateinit var map: MapView
    private val binding: FragmentMainScreenBinding by viewBinding()
    private val viewModel: MainScreenViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_screen, container, false)
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
        viewModel.clusterPerimeter.observe(viewLifecycleOwner){ perim ->
            perim?.let {
                binding.perimeterTextView.apply {
                    text = requireContext().getString(R.string.cluster_perimeter_text, perim.toString())
                    visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getRussia(binding.mapView, binding.perimeterTextView)
        observeData()
        initMap()
    }

    private fun initMap() {
        map = binding.mapView
        map.apply {
            controller.setZoom(4.5)
            setMultiTouchControls(true)
            controller.setCenter(GeoPoint(55.558741, 37.378847 ))
        }
    }

}