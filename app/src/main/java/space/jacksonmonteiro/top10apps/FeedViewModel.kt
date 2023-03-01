package space.jacksonmonteiro.top10apps

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

private const val TAG = "FeedViewModel"
val EMPTY_FEED_LIST: List<FeedEntry> = Collections.emptyList()

class FeedViewModel : ViewModel(), DownloadData.DownloaderCallBack {
    private var downloadData: DownloadData? = null
    private var feedCacheURL = "INVALIDATED"

    private val feed = MutableLiveData<List<FeedEntry>>()
    val feedEntries: LiveData<List<FeedEntry>>
        get() = feed

    init {
        feed.postValue(EMPTY_FEED_LIST)
    }

    override fun onDataAvailable(data: List<FeedEntry>) {
        Log.d(TAG, "onDataAvailable called")
        feed.value = data
        Log.d(TAG, "onDataAvailable ends")
    }

    fun downloadUrl(feedUrl: String) {
        Log.d(TAG, "downloadUrl: called with url $feedUrl")

        if (feedUrl != feedCacheURL) {
            downloadData = DownloadData(this)
            downloadData?.execute(feedUrl)
            feedCacheURL = feedUrl

            Log.d(TAG, "downloadUrl done")
        } else {
            Log.d(TAG, "downloadUrl = URL not changed")
        }
    }

    fun invalidate() {
        feedCacheURL = "INVALIDATE"
    }

    override fun onCleared() {
        Log.d(TAG, "onCleared: cancelling pendind downloads")
        downloadData?.cancel(true)
    }
}