package com.veygard.frontiermap.presentation.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.veygard.frontiermap.R
import com.veygard.frontiermap.databinding.FragmentMainScreenBinding
import com.veygard.frontiermap.presentation.viewModel.MainScreenViewModel
import com.veygard.frontiermap.presentation.viewModel.MainScreenVmState
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
        viewModel.state.observe(viewLifecycleOwner){ result ->
            when(result){
                MainScreenVmState.Error -> {
                    this.findNavController().navigate(R.id.action_mainScreenFragmen_to_errorScreenFragment)
                }
                MainScreenVmState.Loading -> {
                    binding.lottie.apply {
                        speed = 1.3f
                        repeatCount = 99
                        playAnimation()
                    }
                }
                MainScreenVmState.StopLoading -> {
                    binding.lottie.cancelAnimation()
                    binding.lottie.visibility = View.GONE
                    binding.mapView.visibility = View.VISIBLE
                }
                is MainScreenVmState.Success -> {
                    result.clusterPerimeter?.let { km->
                        binding.perimeterTextView.apply {
                            text = requireContext().getString(R.string.cluster_perimeter_text, km.toString())
                            visibility = View.VISIBLE
                        }
                    }
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
        map.apply {
            controller.setZoom(4.5)
            setMultiTouchControls(true)
            controller.setCenter(GeoPoint(55.558741, 37.378847 ))
            isVerticalMapRepetitionEnabled = false
            setScrollableAreaLimitLatitude(MapView.getTileSystem().maxLatitude, MapView.getTileSystem().minLatitude, 0)
            isHorizontalMapRepetitionEnabled = true
//            setScrollableAreaLimitLongitude(MapView.getTileSystem().minLongitude, MapView.getTileSystem().maxLongitude, 0)
        }
    }

}