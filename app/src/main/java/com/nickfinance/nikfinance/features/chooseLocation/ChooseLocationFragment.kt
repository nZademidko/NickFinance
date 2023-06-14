package com.nickfinance.nikfinance.features.chooseLocation

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.google.android.gms.location.LocationServices
import com.nickfinance.nikfinance.base.BaseFragment
import com.nickfinance.nikfinance.databinding.FragmentChooseLocationBinding
import com.yandex.mapkit.MapKit
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.ScreenPoint
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ChooseLocationFragment :
    BaseFragment<ChooseLocationViewModel, FragmentChooseLocationBinding>() {

    private lateinit var mapKit: MapKit
    private var isUserLocationLayerCreated: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.initialize(requireContext());
    }

    override fun onStart() {
        super.onStart()
        mapKit = MapKitFactory.getInstance()
        mapKit.onStart()
        if (!isUserLocationLayerCreated) {
            mapKit.createUserLocationLayer(vb.mapview.mapWindow).isVisible = true
            isUserLocationLayerCreated = true
        }
        vb.mapview.onStart()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        vb.pbAction.setOnClickListener {
            val centerX: Int = vb.mapview.width / 2
            val centerY: Int = vb.mapview.height / 2

            val targetLocation: Point? = vb.mapview.mapWindow.screenToWorld(
                ScreenPoint(
                    centerX.toFloat(),
                    centerY.toFloat()
                )
            )

            vm.onLocationClicked(targetLocation!!)
        }
    }

    override fun onStop() {
        vb.mapview.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    @SuppressLint("MissingPermission")
    @Suppress("PermissionMissing")
    override fun initCustomObservers() {

        vm.locationState.observe { location ->
            if (location == null) {
                LocationServices.getFusedLocationProviderClient(requireActivity()).lastLocation.addOnSuccessListener {
                    vb.mapview.map.move(
                        CameraPosition(
                            Point(it.latitude, it.longitude),
                            18.0f,
                            0.0f,
                            0.0f
                        )
                    )
                }
            } else {
                val latitude = location.split(';')[0].toDouble()
                val longitude = location.split(';')[1].toDouble()
                vb.mapview.map.move(
                    CameraPosition(
                        Point(latitude, longitude),
                        18.0f,
                        0.0f,
                        0.0f
                    )
                )
            }
        }
    }
}