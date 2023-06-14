package com.nickfinance.nikfinance.features.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.nickfinance.nikfinance.R
import com.nickfinance.nikfinance.base.BaseFragment
import com.nickfinance.nikfinance.base.imageLoader.ColorState
import com.nickfinance.nikfinance.base.imageLoader.Type
import com.nickfinance.nikfinance.databinding.FragmentMapBinding
import com.nickfinance.nikfinance.databinding.ItemMapIconBinding
import com.nickfinance.nikfinance.domain.models.ExpenseWithTheme
import com.nickfinance.nikfinance.utils.extensions.loader
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObject
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.runtime.ui_view.ViewProvider
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MapFragment : BaseFragment<MapViewModel, FragmentMapBinding>(), MapObjectTapListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.initialize(requireContext());
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        vb.mapview.onStart()
    }

    override fun onStop() {
        vb.mapview.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun initCustomObservers() {
        vm.state.observe { list ->
            list.forEachIndexed { index, item ->
                if (item.expense.location != null) {
                    val latitude = item.expense.location.split(';')[0].toDouble()
                    val longitude = item.expense.location.split(';')[1].toDouble()

                    if (index == 0) {
                        vb.mapview.map.move(CameraPosition(Point(latitude, longitude), 18f, 0f, 0f))
                        vm.onMarkerClicked(item)
                    }

                    val marker = vb.mapview.map.mapObjects.addPlacemark(
                        Point(
                            latitude,
                            longitude
                        )
                    )
                    val customMarkerView = getCustomMarker(data = item)


                    marker.setView(ViewProvider(customMarkerView))
                    marker.userData = item
                    marker.addTapListener(this)
                }
            }
        }

        vm.openDialogAction.observe { data ->
            DetailedExpenseInfoDialogFragment.openDialog(this, data)
        }
    }

    private fun getCustomMarker(data: ExpenseWithTheme): View {
        return LayoutInflater.from(context).inflate(R.layout.item_map_icon, null).apply {
            findViewById<ImageView>(R.id.ivIcon).loader(
                Type.FromRes(
                    resId = data.theme.iconId,
                    colorState = ColorState.FromIntColor(data.theme.color)
                )
            )
        }
    }

    override fun onMapObjectTap(p0: MapObject, p1: Point): Boolean {
        val obj = p0.userData as ExpenseWithTheme
        val latitude = obj.expense.location!!.split(';')[0]
        val longitude = obj.expense.location.split(';')[1]
        vb.mapview.map.move(
            CameraPosition(
                Point(latitude.toDouble(), longitude.toDouble()),
                18f,
                0f,
                0f
            )
        )
        vm.onMarkerClicked(obj)
        return true
    }
}