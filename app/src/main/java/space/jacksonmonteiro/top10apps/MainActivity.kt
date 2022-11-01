package space.jacksonmonteiro.top10apps

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import java.net.URL
import kotlin.properties.Delegates

class FeedEntry {
    var name: String = ""
    var artist: String = ""
    var releaseDate: String = ""
    var summary: String = ""
    var imageURL: String = ""

    override fun toString(): String {
        return """
            name = $name,
            artist = $artist,
            releaseDate = $releaseDate,
            imageURL = $imageURL
        """.trimIndent()
    }
}

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreate called")

        val xmlListView : ListView = findViewById(R.id.xmlListView)
        val downloadData = DownloadData(this, xmlListView)
        downloadData.execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml")
        Log.d(TAG, "onCreate done")
    }

    companion object {
        private class DownloadData(context: Context, listview: ListView) : AsyncTask<String, Void, String>() {
            private val TAG = "DownloadData"

            var propContext: Context by Delegates.notNull()
            var propListView: ListView by Delegates.notNull()

            init {
                propContext = context
                propListView = listview
            }

            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)
                //Log.d(TAG, "OnPostExecute: parameter is $result")

                val parseApplications = ParseApplications()
                if (result != null) {
                    parseApplications.parse(result)
                }

                val arrayAdapter = ArrayAdapter<FeedEntry>(propContext, R.layout.list_item, parseApplications.applications)
                propListView.adapter = arrayAdapter
            }

            override fun doInBackground(vararg params: String?): String {
                Log.d(TAG, "DoInBackgroudn starts with ${params[0]}")

                val rssFeed = downloadXML(params[0])
                if (rssFeed.isEmpty()) {
                    Log.e(TAG, "doInBackground: Error downloading RSS feed")
                }
                return rssFeed
            }

            private fun downloadXML(url: String?): String {
                return URL(url).readText()
            }
        }
    }

}