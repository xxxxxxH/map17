package qiu.li.gao.activity

import android.content.Intent
import androidx.core.view.GravityCompat
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.gestures.OnMoveListener
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import qiu.li.gao.databinding.ActivityMainBinding
import qiu.li.gao.event.xxEvent
import qiu.li.gao.utils.addMoveListener
import qiu.li.gao.utils.getIndicatorListener
import qiu.li.gao.utils.initLocationComponent

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
        EventBus.getDefault().register(this)
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
        _binding.toolBar.getMenuBtn().setOnClickListener {
            _binding.drawerLayout.openDrawer(GravityCompat.START)
        }
        _binding.out.setOnClickListener {
            _binding.mapView.getMapboxMap().loadStyleUri(Style.OUTDOORS)
            _binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
        _binding.sate.setOnClickListener {
            _binding.mapView.getMapboxMap().loadStyleUri(Style.SATELLITE)
            _binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
        _binding.traf.setOnClickListener {
            _binding.mapView.getMapboxMap().loadStyleUri(Style.TRAFFIC_DAY)
            _binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
        _binding.light.setOnClickListener {
            _binding.mapView.getMapboxMap().loadStyleUri(Style.LIGHT)
            _binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
        _binding.streetview.setOnClickListener {
            startActivity(Intent(this, StreetviewActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(e: xxEvent){
        val msg = e.getMessage()
        when(msg[0]){

        }
    }

}