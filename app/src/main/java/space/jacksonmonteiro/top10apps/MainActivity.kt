package space.jacksonmonteiro.top10apps

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class FeedEntry {
    var name: String = ""
    var artist: String = ""
    var releaseDate: String = ""
    var summary: String = ""
    var imageURL: String = ""
}

private const val STATE_URL = "feedUrl"
private const val STATE_LIMIT = "feedLimit"

class MainActivity : AppCompatActivity() {

    private var feedUrl: String = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml"
    private var feedLimit = 10
    private lateinit var xmlListView : ListView

    private val feedViewModel: FeedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val feedAdapter = FeedAdapter(this, R.layout.list_record, EMPTY_FEED_LIST)
        xmlListView = findViewById(R.id.xmlListView)
        xmlListView.adapter = feedAdapter

        if (savedInstanceState != null) {
            feedUrl = savedInstanceState.getString(STATE_URL)!!
            feedLimit = savedInstanceState.getInt(STATE_LIMIT)
        }

        feedViewModel.feedEntries.observe(this) { feedEntries -> feedAdapter.setFeedList(feedEntries ?: EMPTY_FEED_LIST) }

        feedViewModel.downloadUrl(feedUrl.format(feedLimit))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.feeds_menu, menu)

        if (feedLimit == 10) {
            menu?.findItem(R.id.menu10)?.isChecked = true
        } else {
            menu?.findItem(R.id.menu25)?.isChecked = true
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuFree -> feedUrl =
                "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml"
            R.id.menuPaid -> feedUrl =
                "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/toppaidapplications/limit=%d/xml"
            R.id.menuSongs -> feedUrl =
                "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/limit=%d/xml"
            R.id.menu10, R.id.menu25 -> {
                if (!item.isChecked) {
                    item.isChecked = true
                    feedLimit = 35 - feedLimit
                    Log.d("onOptionsItemSelected", "${item.title} settings feedLimit to $feedLimit")
                } else {
                    Log.d("onOptionsItemSelected", "feedLimit unchanged")
                }
            }
            R.id.menuRefresh -> feedViewModel.invalidate()
            else -> return super.onOptionsItemSelected(item)
        }

        feedViewModel.downloadUrl(feedUrl.format(feedLimit))
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(STATE_URL, feedUrl)
        outState.putInt(STATE_LIMIT, feedLimit)
    }
}