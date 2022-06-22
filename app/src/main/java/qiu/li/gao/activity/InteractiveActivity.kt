package qiu.li.gao.activity

import android.content.Intent
import androidx.recyclerview.widget.GridLayoutManager
import cctv5.cha.abc123.http.getData
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.squareup.picasso.Picasso
import me.lwb.bindingadapter.BindingAdapter
import qiu.li.gao.databinding.ActivityInteractiveBinding
import qiu.li.gao.databinding.ItemBinding
import qiu.li.gao.entity.DataEntity
import qiu.li.gao.utils.formatData
import qiu.li.gao.utils.formatImageUrl
import qiu.li.gao.utils.formatText
import qiu.li.gao.utils.showToast

class InteractiveActivity : BaseActivity<ActivityInteractiveBinding>() {
    override fun getViewBinding() = ActivityInteractiveBinding.inflate(layoutInflater)

    override fun initialization() {
        getData("https://www.google.com/streetview/feed/gallery/data.json",{
            val (list: ArrayList<DataEntity>, keys: ArrayList<String>) = formatData(it)
            val adapter = BindingAdapter(ItemBinding::inflate, list) { position, item ->
                Glide.with(this@InteractiveActivity)
                    .load(formatImageUrl(item.panoid))
                    .into(binding.itemImage)
                binding.itemText.text = formatText(item.title)
                binding.itemText.setOnClickListener {
                    val reqUrl =
                        "https://www.google.com/streetview/feed/gallery/collection/" + keys[position] + ".json"
                    val imageUrl = formatImageUrl(item.panoid)
                    startActivity(
                        Intent(
                            this@InteractiveActivity,
                            DetailsActivity::class.java
                        ).apply {
                            putExtra("reqUrl", reqUrl)
                            putExtra("imageUrl", imageUrl)
                        })
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