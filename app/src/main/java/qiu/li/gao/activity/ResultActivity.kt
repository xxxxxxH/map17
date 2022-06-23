package qiu.li.gao.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mapbox.geojson.Point
import com.mapbox.maps.Style
import qiu.li.gao.R
import qiu.li.gao.databinding.ActivityResultBinding
import qiu.li.gao.utils.addMarker
import qiu.li.gao.utils.moveMap

class ResultActivity : BaseActivity<ActivityResultBinding>() {
    private val lat by lazy {
        intent.getDoubleExtra("lat", 0.0)
    }
    private val lng by lazy {
        intent.getDoubleExtra("lng", 0.0)
    }
    override fun getViewBinding() = ActivityResultBinding.inflate(layoutInflater)

    override fun initialization() {
        _binding.map.moveMap(Point.fromLngLat(lng,  lat))
        _binding.map.getMapboxMap().loadStyleUri(Style.SATELLITE_STREETS)
        _binding.map.addMarker(Point.fromLngLat(lng,  lat))
    }

}