package qiu.li.gao.activity

import androidx.recyclerview.widget.GridLayoutManager
import cctv5.cha.abc123.http.getData
import com.bumptech.glide.Glide
import com.mapbox.geojson.Point
import com.mapbox.maps.Style
import me.lwb.bindingadapter.BindingAdapter
import qiu.li.gao.databinding.ActivityDeatilsBinding
import qiu.li.gao.databinding.ItemBinding
import qiu.li.gao.entity.DataEntity
import qiu.li.gao.utils.*

class DetailsActivity : BaseActivity<ActivityDeatilsBinding>() {

    private val reqUrl by lazy { intent.getStringExtra("reqUrl") }

    private val lng by lazy { intent.getStringExtra("lng") }

    private val lat by lazy { intent.getStringExtra("lat") }

    override fun getViewBinding() = ActivityDeatilsBinding.inflate(layoutInflater)

    override fun initialization() {
        loadingDialog.show()
        _binding.map.moveMap(Point.fromLngLat(lng!!.toDouble(),  lat!!.toDouble()))
        _binding.map.getMapboxMap().loadStyleUri(Style.SATELLITE_STREETS)
        _binding.map.addMarker(Point.fromLngLat(lng!!.toDouble(),  lat!!.toDouble()))
        getData(reqUrl!!, {
            val (list: ArrayList<DataEntity>, keys: ArrayList<String>) = formatData(it)
            val adapter = BindingAdapter(ItemBinding::inflate, list) { position, item ->
                Glide.with(this@DetailsActivity).load(formatImageUrl(item.panoid))
                    .into(binding.itemImage)
                binding.itemText.text = formatText(item.title)
                binding.itemText.setOnClickListener {
                    _binding.map.moveMap(Point.fromLngLat(item.lng.toDouble(),  item.lat.toDouble()))
                    _binding.map.addMarker(Point.fromLngLat(item.lng.toDouble(),  item.lat.toDouble()))
                }
            }
            _binding.recycler.layoutManager = GridLayoutManager(this, 2)
            _binding.recycler.adapter = adapter
            loadingDialog.dismiss()
        }, {
            loadingDialog.dismiss()
            showToast("no data")
            finish()
        })
    }

}