package qiu.li.gao.activity

import android.annotation.SuppressLint
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import com.mapbox.geojson.Point
import com.mapbox.maps.Style
import qiu.li.gao.R
import qiu.li.gao.databinding.ActivityStreetviewBinding
import qiu.li.gao.utils.currentLocation
import qiu.li.gao.utils.setCamera
import qiu.li.gao.utils.setCameraChangeListener

class StreetviewActivity : BaseActivity<ActivityStreetviewBinding>() {
    override fun getViewBinding()=ActivityStreetviewBinding.inflate(layoutInflater)

    @SuppressLint("ClickableViewAccessibility")
    override fun initialization() {
        _binding.map1.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN){
                true
            }
            true
        }
        _binding.map1.currentLocation()
        _binding.map1.getMapboxMap().loadStyleUri(Style.OUTDOORS)

        _binding.map2.currentLocation()
        _binding.map2.setCameraChangeListener { d, d2 ->
            _binding.map1.setCamera(Point.fromLngLat(d,d2))
        }
        _binding.map2.getMapboxMap().loadStyleUri(Style.SATELLITE_STREETS)
    }

}