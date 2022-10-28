package space.jacksonmonteiro.top10apps

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.nio.Buffer

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

                    val reader = BufferedReader(InputStreamReader(connection.inputStream))

                    val inputBuffer = CharArray(500)
                    var charsReader = 0
                    while (charsReader <= 0) {
                        charsReader = reader.read(inputBuffer)
                        if (charsReader > 0) {
                            xmlResult.append(inputBuffer, 0, charsReader)
                        }
                    }
                    reader.close()
                    Log.d(TAG, "Received ${xmlResult.length} bytes")
                    return xmlResult.toString()

                } catch (e: MalformedURLException) {
                    Log.e(TAG, "downloadXML: invalid url exception: ${e.message}")
                } catch (e: IOException) {
                    Log.e(TAG, "downloadXML: IOException: ${e.message}")
                } catch (e: Exception) {
                    Log.e(TAG, "downloadXML: Unknown error: ${e.message}")
                }

                return ""
            }
        }
    }

}