package qiu.li.gao.utils

import com.mapbox.search.ResponseInfo
import com.mapbox.search.SearchMultipleSelectionCallback
import com.mapbox.search.SearchSelectionCallback
import com.mapbox.search.result.SearchResult
import com.mapbox.search.result.SearchSuggestion

interface SearchCallbackExt: SearchSelectionCallback, SearchMultipleSelectionCallback {

}