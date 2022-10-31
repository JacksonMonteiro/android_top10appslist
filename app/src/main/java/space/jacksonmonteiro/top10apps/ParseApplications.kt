package space.jacksonmonteiro.top10apps

import android.util.Log
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory

class ParseApplications {
    // Log TAG
    private val TAG = "ParseApplications"

    // Variables
    val applications = ArrayList<FeedEntry>();

    fun parse(xmlData: String) : Boolean {
        Log.d(TAG, "parse called with $xmlData")
        var status = true
        var inEntry = false
        var textValue = ""

        try {
            var factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val xpp = factory.newPullParser()
            xpp.setInput(xmlData.reader())
            var eventType = xpp.eventType
            var currentRecord = FeedEntry()
            while (eventType != XmlPullParser.END_DOCUMENT) {

            }
        } catch (e: Exception) {
            e.printStackTrace()
            status = false
        }

        return status
    }
}