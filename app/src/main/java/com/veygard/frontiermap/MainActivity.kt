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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey("20efb660-3a81-4715-89e4-5d366f2551a7");
        MapKitFactory.initialize(this);
        setContentView(R.layout.activity_main)
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