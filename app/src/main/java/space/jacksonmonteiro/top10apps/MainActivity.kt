package space.jacksonmonteiro.top10apps

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import java.io.IOException
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreate called")
        val downloadData = DownloadData()
        downloadData.execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml")
        Log.d(TAG, "onCreate done")
    }

    companion object {
        private class DownloadData : AsyncTask<String, Void, String>() {
            private val TAG = "DownloadData"

            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)
                Log.d(TAG, "OnPostExecute: parameter is $result")
            }

            override fun doInBackground(vararg params: String?): String {
                Log.d(TAG, "DoInBackgroudn starts with ${params[0]}")

                val rssFeed = downloadXML(params[0])
                if (rssFeed.isEmpty()) {
                    Log.e(TAG, "doInBackground: Error downloading RSS feed")
                }
                return rssFeed
            }

            private fun downloadXML(url: String?) : String {
                val xmlResult = StringBuilder()

                try {
                    val url = URL(url)
                    val connection : HttpURLConnection = url.openConnection() as HttpURLConnection
                    val response = connection.responseCode
                    Log.d(TAG, "downloadXML: Response is $response")

                    connection.inputStream.buffered().reader().use { xmlResult.append(it.readText()) }

                    Log.d(TAG, "Received ${xmlResult.length} bytes")
                    return xmlResult.toString()

                }  catch (e: Exception) {
                    val errorMessage : String   = when (e) {
                        is MalformedURLException -> "downloadXML: invalid url exception: ${e.message}"
                        is IOException -> "downloadXML: IOException: ${e.message}"
                        is SecurityException -> {
                            e.printStackTrace()
                            "downloadXML: SecurityException: ${e.message}"
                        }
                        else -> "downloadXML: unknown exception: ${e.message}"
                    }
                }

                return ""
            }
        }
    }

}