package qiu.li.gao.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.plugin.animation.flyTo
import com.mapbox.maps.plugin.annotation.AnnotationConfig
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.search.ResponseInfo
import com.mapbox.search.result.SearchResult
import com.mapbox.search.result.SearchSuggestion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import qiu.li.gao.R
import qiu.li.gao.entity.DataEntity
import qiu.li.gao.entity.NearbyEntity

fun MapView.initLocationComponent(listener: OnIndicatorPositionChangedListener) {
    val plugin = location
    plugin.updateSettings {
        this.enabled = true
    }
    plugin.addOnIndicatorPositionChangedListener(
        listener
    )
}

fun MapView.addMoveListener(listener: OnMoveListener) {
    this.gestures.addOnMoveListener(listener)
}

fun MapView.getIndicatorListener(): OnIndicatorPositionChangedListener {
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

fun MapView.moveMap(p: Point) {
    getMapboxMap().flyTo(cameraOptions {
        center(p)
        zoom(14.0)
    })
}

fun MapView.addMarker(p: Point) {
    val bitmap: Bitmap = BitmapFactory.decodeResource(this.context.resources, R.mipmap.red_marker)
    annotations.cleanup()
    val markerManager = annotations.createPointAnnotationManager(AnnotationConfig())
    val pointAnnotationOptions = PointAnnotationOptions()
        .withPoint(p)
        .withIconImage(bitmap)
    markerManager.create(pointAnnotationOptions)
}

fun searchCallback(
    callbackError: () -> Unit,
    callBackSuggestions: (List<SearchSuggestion>, ResponseInfo) -> Unit,
    callBackResult: (List<SearchSuggestion>, List<SearchResult>, ResponseInfo) -> Unit
): SearchCallbackExt {
    return object : SearchCallbackExt {
        override fun onResult(
            suggestions: List<SearchSuggestion>,
            results: List<SearchResult>,
            responseInfo: ResponseInfo
        ) {
            callBackResult(suggestions, results, responseInfo)
        }

        override fun onCategoryResult(
            suggestion: SearchSuggestion,
            results: List<SearchResult>,
            responseInfo: ResponseInfo
        ) {

        }

        override fun onResult(
            suggestion: SearchSuggestion,
            result: SearchResult,
            responseInfo: ResponseInfo
        ) {

        }

        override fun onError(e: Exception) {
            callbackError()
        }

        override fun onSuggestions(
            suggestions: List<SearchSuggestion>,
            responseInfo: ResponseInfo
        ) {
            callBackSuggestions(suggestions, responseInfo)
        }
    }
}


fun formatData(data: String): Pair<ArrayList<DataEntity>, ArrayList<String>> {
    val type = object : TypeToken<Map<String, DataEntity>>() {}.type
    val map: Map<String, DataEntity> = Gson().fromJson<Map<String, DataEntity>>(data, type)
    val list: ArrayList<DataEntity> = ArrayList<DataEntity>(map.values)
    val keys = ArrayList(map.keys)
    return Pair(list, keys)
}

fun formatImageUrl(key: String): String {
    return "https://geo0.ggpht.com/cbk?output=thumbnail&thumb=2&panoid=$key"
}

fun formatText(key: String): String {
    return if (TextUtils.isEmpty(key)) "" else key
}

fun AppCompatActivity.showToast(s: String) {
    Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
}

fun AppCompatActivity.getNearbyData(block: (ArrayList<NearbyEntity>) -> Unit) {
    lifecycleScope.launch(Dispatchers.IO) {
        val r = ArrayList<NearbyEntity>()
        val assetsManager = this@getNearbyData.resources.assets
        val files = assetsManager.list("img")
        if (files!!.isNotEmpty()) {
            files.forEach {
                val inputStream = assetsManager.open("img/$it")
                val bitmap = BitmapFactory.decodeStream(inputStream)
                val e = NearbyEntity(it, bitmap)
                r.add(e)
            }
        }
        withContext(Dispatchers.Main) {
            block(r)
        }
    }
}

//https://github.com/ve3344/binding-adapter
