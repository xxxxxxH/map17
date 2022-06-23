package qiu.li.gao.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mapbox.search.MapboxSearchSdk
import com.mapbox.search.ResponseInfo
import com.mapbox.search.SearchOptions
import com.mapbox.search.result.SearchResult
import com.mapbox.search.result.SearchSuggestion
import com.mapbox.search.ui.view.SearchBottomSheetView
import qiu.li.gao.R
import qiu.li.gao.databinding.ActivityRouterBinding
import qiu.li.gao.utils.showToast

class RouterActivity : BaseActivity<ActivityRouterBinding>() {

    override fun getViewBinding() = ActivityRouterBinding.inflate(layoutInflater)

    override fun initialization() {
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        _binding.sheet.initializeSearch(savedInstanceState, SearchBottomSheetView.Configuration())
        _binding.sheet.addOnCategoryClickListener{
            loadingDialog.show()
            searchEngine.search(it.geocodingCanonicalName, SearchOptions(), callback)
        }
        _binding.sheet.addOnFavoriteClickListener{
            loadingDialog.show()
            startActivity(Intent(this, ResultActivity::class.java).apply {
                putExtra("lat", it.coordinate.latitude())
                putExtra("lng", it.coordinate.longitude())
            })
        }
        _binding.sheet.addOnHistoryClickListener{
            loadingDialog.show()
            it.coordinate?.let { point ->
                startActivity(Intent(this, ResultActivity::class.java).apply {
                    putExtra("lat", point.latitude())
                    putExtra("lng", point.longitude())
                })
            } ?: kotlin.run {
                searchEngine.search(
                    it.name,
                    SearchOptions(),
                    callback
                )
            }
        }
        _binding.sheet.addOnSearchResultClickListener{searchResult, responseInfo ->
            loadingDialog.show()
            searchResult.coordinate?.let {
                startActivity(Intent(this, ResultActivity::class.java).apply {
                    putExtra("lat", it.latitude())
                    putExtra("lng", it.longitude())
                })
            } ?: kotlin.run {
                searchEngine.search(
                    searchResult.name,
                    SearchOptions(),
                    callback
                )
            }
        }
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

    override fun onError() {
        super.onError()
        loadingDialog.dismiss()
    }
}