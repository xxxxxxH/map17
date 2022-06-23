package qiu.li.gao.activity

import android.content.Intent
import androidx.recyclerview.widget.GridLayoutManager
import com.mapbox.search.ResponseInfo
import com.mapbox.search.SearchOptions
import com.mapbox.search.result.SearchResult
import com.mapbox.search.result.SearchSuggestion
import me.lwb.bindingadapter.BindingAdapter
import qiu.li.gao.databinding.ActivityNearbyBinding
import qiu.li.gao.databinding.ItemBinding
import qiu.li.gao.utils.getNearbyData
import qiu.li.gao.utils.showToast

class NearbyActivity : BaseActivity<ActivityNearbyBinding>() {
    override fun getViewBinding() = ActivityNearbyBinding.inflate(layoutInflater)

    override fun initialization() {
        getNearbyData {
            val adapter = BindingAdapter(ItemBinding::inflate, it) { position, item ->
                binding.itemImage.setImageBitmap(item.img)
                binding.itemText.text = item.name.substring(0, item.name.length - 4)
                binding.itemText.setOnClickListener {
                    loadingDialog.show()
                    searchEngine.search(
                        item.name.substring(0, item.name.length - 4),
                        SearchOptions(),
                        callback
                    )
                }
            }
            _binding.recycler.layoutManager = GridLayoutManager(this, 2)
            _binding.recycler.adapter = adapter
        }
    }

    override fun onError() {
        super.onError()
        loadingDialog.dismiss()
    }

    override fun suggestions(suggestions: List<SearchSuggestion>, responseInfo: ResponseInfo) {
        super.suggestions(suggestions, responseInfo)
        suggestions.firstOrNull()?.let {
            searchEngine.select(suggestions, callback)
        } ?: kotlin.run {
            loadingDialog.dismiss()
            showToast("No suggestions found")
        }
    }

    override fun result(suggestions: List<SearchSuggestion>, results: List<SearchResult>, responseInfo: ResponseInfo) {
        super.result(suggestions, results, responseInfo)
        loadingDialog.dismiss()
        results.firstOrNull()?.coordinate?.let {
            startActivity(Intent(this, ResultActivity::class.java).apply {
                putExtra("lat", it.latitude())
                putExtra("lng", it.longitude())
            })
        } ?: kotlin.run {
            showToast("No suggestions found")
        }
    }
}