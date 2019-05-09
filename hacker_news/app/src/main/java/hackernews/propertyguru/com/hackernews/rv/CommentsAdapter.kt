package hackernews.propertyguru.com.hackernews.rv

import android.os.Build
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.text.Spanned
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import hackernews.propertyguru.com.hackernews.R
import hackernews.propertyguru.com.hackernews.network.responses.GetStoryDetailResponse
import java.util.*

/**
 * Created by hung on 5/8/19.
 */
class CommentsAdapter(private val storyDetails: ArrayList<GetStoryDetailResponse>) : RecyclerView.Adapter<CommentsAdapter.RowHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowHolder {
        return RowHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_comment, parent, false))
    }

    override fun getItemCount(): Int {
        return storyDetails.size
    }

    override fun onBindViewHolder(holder: RowHolder, position: Int) {
        holder.bindData(storyDetails[position])
    }

    class RowHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val byTv: TextView = v.findViewById(R.id.by_tv)
        private val dateTv: TextView = v.findViewById(R.id.date_tv)
        private val textTv: TextView = v.findViewById(R.id.text_tv)

        fun bindData(storyDetail: GetStoryDetailResponse) {
            byTv.text = storyDetail.by
            dateTv.text = prettyTime(storyDetail.time ?: "0")
            textTv.text = parseHtml(storyDetail.text ?: "")
        }

        private fun prettyTime(time: String): String {
            return DateUtils.getRelativeTimeSpanString(time.toLong()).toString()
        }

        private fun parseHtml(html: String): Spanned {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)
            } else {
                Html.fromHtml(html)
            }
        }
    }
}
