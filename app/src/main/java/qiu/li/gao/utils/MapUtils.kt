package qiu.li.gao.utils

import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location

fun MapView.initLocationComponent(listener: OnIndicatorPositionChangedListener){
    val plugin = location
    plugin.updateSettings {
        this.enabled = true
    }
    plugin.addOnIndicatorPositionChangedListener(
        listener
    )
}

fun MapView.addMoveListener(listener: OnMoveListener){
    this.gestures.addOnMoveListener(listener)
}

fun MapView.getIndicatorListener():OnIndicatorPositionChangedListener{
    return OnIndicatorPositionChangedListener {
        this.getMapboxMap().setCamera(CameraOptions.Builder().center(it).build())
        this.gestures.focalPoint = this.getMapboxMap().pixelForCoordinate(it)
    }
}
