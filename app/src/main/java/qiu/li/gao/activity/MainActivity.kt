package qiu.li.gao.activity

import cctv5.cha.abc123.utils.xxTools
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.gestures.OnMoveListener
import qiu.li.gao.databinding.ActivityMainBinding
import qiu.li.gao.utils.*

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val positionChangedListener by lazy {
        _binding.mapView.getIndicatorListener()
    }

    private val onMoveListener = object : OnMoveListener {
        override fun onMove(detector: MoveGestureDetector): Boolean {
            return false
        }

        override fun onMoveBegin(detector: MoveGestureDetector) {

        }

        override fun onMoveEnd(detector: MoveGestureDetector) {

        }

    }

    override fun getViewBinding() = ActivityMainBinding.inflate(layoutInflater)
    override fun initialization() {
//        xxTools.get().xxCheck(this)
        _binding.mapView.getMapboxMap().setCamera(
            CameraOptions.Builder()
                .zoom(14.0)
                .build()
        )
        _binding.mapView.getMapboxMap().loadStyleUri(
            Style.MAPBOX_STREETS
        ) {
            _binding.mapView.initLocationComponent(positionChangedListener)
            _binding.mapView.addMoveListener(onMoveListener)
        }
    }

}