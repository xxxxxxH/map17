package qiu.li.gao.utils

import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import qiu.li.gao.R
import qiu.li.gao.entity.DataEntity

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

fun MapView.currentLocation() {
    val listener = object : OnIndicatorPositionChangedListener {
        override fun onIndicatorPositionChanged(point: Point) {
            setTag(R.id.appViewMyLocationId, point)
            getMapboxMap().setCamera(CameraOptions.Builder().center(point).build())
            gestures.focalPoint = getMapboxMap().pixelForCoordinate(point)
            location.removeOnIndicatorPositionChangedListener(this)
        }
    }
    location.addOnIndicatorPositionChangedListener(listener)
}

fun MapView.setCameraChangeListener(block: (Double, Double) -> Unit) {
    getMapboxMap().addOnCameraChangeListener {
        getMapboxMap().cameraState.center.let {
            block(it.longitude(), it.latitude())
        }
    }
}

fun MapView.setCamera(center: Point) {
    getMapboxMap().setCamera(
        cameraOptions {
            center(center)
            zoom(14.0)
        }
    )
}


fun formatData(data: String):Pair<ArrayList<DataEntity>,ArrayList<String>> {
    val type = object : TypeToken<Map<String, DataEntity>>() {}.type
    val map: Map<String, DataEntity> = Gson().fromJson<Map<String, DataEntity>>(data, type)
    val list: ArrayList<DataEntity> = ArrayList<DataEntity>(map.values)
    val keys = ArrayList(map.keys)
    return Pair(list, keys)
}

fun formatImageUrl(key: String): String {
    return "https://geo0.ggpht.com/cbk?output=thumbnail&thumb=2&panoid=$key"
}

fun formatText(key :String):String{
    return if (TextUtils.isEmpty(key)) "" else key
}

fun AppCompatActivity.showToast(s:String){
    Toast.makeText(this,s,Toast.LENGTH_SHORT).show()
}

//https://github.com/ve3344/binding-adapter
