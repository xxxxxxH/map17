package qiu.li.gao.activity

import androidx.recyclerview.widget.GridLayoutManager
import cctv5.cha.abc123.http.getData
import com.bumptech.glide.Glide
import me.lwb.bindingadapter.BindingAdapter
import qiu.li.gao.databinding.ActivityDeatilsBinding
import qiu.li.gao.databinding.ItemBinding
import qiu.li.gao.entity.DataEntity
import qiu.li.gao.utils.formatData
import qiu.li.gao.utils.formatImageUrl
import qiu.li.gao.utils.formatText
import qiu.li.gao.utils.showToast

class DetailsActivity : BaseActivity<ActivityDeatilsBinding>() {

    private val reqUrl by lazy { intent.getStringExtra("reqUrl") }

    private val imageUrl by lazy { intent.getStringExtra("imageUrl") }

    override fun getViewBinding() = ActivityDeatilsBinding.inflate(layoutInflater)

    override fun initialization() {
        Glide.with(this).load(imageUrl).into(_binding.image)
        getData(reqUrl!!, {
            val (list: ArrayList<DataEntity>, keys: ArrayList<String>) = formatData(it)
            val adapter = BindingAdapter(ItemBinding::inflate, list) { position, item ->
                Glide.with(this@DetailsActivity).load(formatImageUrl(item.panoid))
                    .into(binding.itemImage)
                binding.itemText.text = formatText(item.title)
                binding.itemText.setOnClickListener {
                    Glide.with(this@DetailsActivity).load(formatImageUrl(item.panoid))
                        .into(_binding.image)
                }
            }
            _binding.recycler.layoutManager = GridLayoutManager(this, 2)
            _binding.recycler.adapter = adapter
        }, {
            showToast("no data")
            finish()
        })
    }

}