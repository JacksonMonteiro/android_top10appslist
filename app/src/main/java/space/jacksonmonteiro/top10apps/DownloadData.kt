package space.jacksonmonteiro.top10apps

import android.os.AsyncTask
import android.util.Log
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

private val TAG = "DownloadData"

class DownloadData(private val callBack: DownloaderCallBack) : AsyncTask<String, Void, String>() {

    interface DownloaderCallBack {
        fun onDataAvailable(data: List<FeedEntry>)
    }

    @Deprecated("Deprecated in Java")
    override fun onPostExecute(result: String?) {

        val parseApplications = ParseApplications()
        if (result != null) {
            parseApplications.parse(result)
        }

        callBack.onDataAvailable(parseApplications.applications)
    }

    @Deprecated("Deprecated in Java")
    override fun doInBackground(vararg params: String?): String {
        val rssFeed = downloadXML(params[0])
        if (rssFeed.isEmpty()) {
            Log.e(TAG, "doInBackground: Error downloading RSS feed")
        }
        return rssFeed
    }

    private fun downloadXML(url: String?): String {
        try {
            return URL(url).readText()
        } catch (e: MalformedURLException) {
            Log.d(TAG, "downloadXML: Invalid URL: $e")
        } catch (e: IOException) {
            Log.d(TAG, "downloadXML: Invalid Exception Reading Data: $e")
        } catch (e: SecurityException) {
            Log.d(TAG, "downloadXML: Security Exception, needs Permission? $e")
        }

        return ""
    }
}
