package com.veygard.frontiermap

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.veygard.frontiermap.databinding.ActivityMainBinding
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView

class MainActivity : AppCompatActivity() {
    private lateinit var mapView: MapView
    private val TARGET_LOCATION = Point(59.945933, 30.320045)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey("20efb660-3a81-4715-89e4-5d366f2551a7");
        MapKitFactory.initialize(this);
        setContentView(R.layout.activity_main)
        mapView = findViewById(R.id.mapview)
        findViewById<Button>(R.id.button).setOnClickListener {
            move()
        }

    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }
    override fun onStop() {
        super.onStop()
        MapKitFactory.getInstance().onStop()
        mapView.onStop()
    }
    private fun move(){
        Log.e("click", "button clicked")
        mapView.map.move(
            CameraPosition(TARGET_LOCATION, 14.0f, 1.0f, 1.0f),
            Animation(Animation.Type.SMOOTH, 3f),
            null
        )
    }
}