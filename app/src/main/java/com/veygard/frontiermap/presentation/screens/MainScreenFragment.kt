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
import org.osmdroid.bonuspack.kml.KmlDocument
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.compass.CompassOverlay
import java.io.File


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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getRussia(binding.mapView)
        map = binding.mapView

        map.controller.setZoom(10.2)
        map.setMultiTouchControls(true)


        val compassOverlay = CompassOverlay(requireContext(), map)
        compassOverlay.enableCompass()
        map.overlays.add(compassOverlay)

        val point = GeoPoint(42.845404364, 132.44898522200018)

        val startMarker = Marker(map)
        startMarker.position = point
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
        map.overlays.add(startMarker)

        map.controller.setCenter(point)

        val kmlDocument = KmlDocument()
        val localFile: File = kmlDocument.getDefaultPathForAndroid(requireContext(),"rus.json")
        kmlDocument.saveAsGeoJSON(localFile)
        val overlay = kmlDocument.mKmlRoot.buildOverlay(map, null, null, kmlDocument)
        map.overlays.add(overlay)
        map.invalidate()

        val bb = kmlDocument.mKmlRoot.boundingBox
        map.zoomToBoundingBox(bb, true)
        map.controller.setCenter(bb.center);
    }

    override fun onStart() {
        super.onStart()
    }
    override fun onStop() {
        super.onStop()
    }



}