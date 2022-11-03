package space.jacksonmonteiro.top10apps

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class FeedAdapter(context: Context, private val resource: Int, private val applications: List<FeedEntry>) :
    ArrayAdapter<FeedEntry>(context, resource) {
    private val TAG = "FeedAdapter"
    private val inflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        Log.d(TAG, "getCount() called")
        return applications.size
    }

    override fun getView(position: Int, contentView: View?, parent: ViewGroup): View {
        Log.d(TAG, "getView() called")

        val view: View
        if (contentView == null) {
            Log.d(TAG, "getView called with null contentView")
            view = inflater.inflate(resource, parent, false)
        } else {
            Log.d(TAG, "getView provided a contentView")
            view = contentView
        }

        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvArtist: TextView = view.findViewById(R.id.tvArtist)
        val tvSummary: TextView = view.findViewById(R.id.tvSummary)

        val currentApp = applications[position]
        tvName.text = currentApp.name
        tvArtist.text = currentApp.artist
        tvSummary.text = currentApp.summary

        return view
    }
}