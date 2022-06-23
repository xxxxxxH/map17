package qiu.li.gao.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.mapbox.search.MapboxSearchSdk
import com.mapbox.search.ResponseInfo
import com.mapbox.search.result.SearchResult
import com.mapbox.search.result.SearchSuggestion
import qiu.li.gao.utils.searchCallback
import qiu.li.gao.widget.LoadingDialog

abstract class BaseActivity<T : ViewBinding> : AppCompatActivity() {
    lateinit var _binding: T
    private val binding get() = _binding


    val searchEngine by lazy {
        MapboxSearchSdk.getSearchEngine()
    }

    val loadingDialog by lazy {
        LoadingDialog(this)
    }

    var callback = searchCallback({
        onError()
    }, { suggestions, responseInfo ->
        suggestions(suggestions, responseInfo)
    }, { suggestions, results, responseInfo ->
        result(suggestions, results, responseInfo)
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = getViewBinding()
        setContentView(binding.root)
        initialization()
        initData(savedInstanceState)
    }

    protected abstract fun getViewBinding(): T

    abstract fun initialization()

    open fun initData(savedInstanceState: Bundle?) {}

    open fun onError() {}

    open fun suggestions(
        suggestions: List<SearchSuggestion>,
        responseInfo: ResponseInfo
    ) {
    }

    open fun result(
        suggestions: List<SearchSuggestion>,
        results: List<SearchResult>,
        responseInfo: ResponseInfo
    ) {
    }
}