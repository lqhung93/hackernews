package hackernews.propertyguru.com.hackernews.rv

import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import com.thefinestartist.finestwebview.FinestWebView
import hackernews.propertyguru.com.hackernews.R
import hackernews.propertyguru.com.hackernews.network.responses.GetStoryDetailResponse
import hackernews.propertyguru.com.hackernews.utils.Utils

/**
 * Created by hung on 5/8/19.
 */
class NewsAdapter(private val storyDetails: List<GetStoryDetailResponse>) : RecyclerView.Adapter<NewsAdapter.RowHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowHolder {
        return RowHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_news, parent, false))
    }

    override fun getItemCount(): Int {
        return storyDetails.size
    }

    override fun onBindViewHolder(holder: RowHolder, position: Int) {
        holder.bindData(storyDetails[position])
    }

    class RowHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val titleTv: TextView = v.findViewById(R.id.title_tv)
        private val scoreTv: TextView = v.findViewById(R.id.score_tv)
        private val sourceTv: TextView = v.findViewById(R.id.source_tv)
        private val commentBtn: RelativeLayout = v.findViewById(R.id.comment_btn)
        private val commentTv: TextView = v.findViewById(R.id.comment_tv)

        fun bindData(storyDetail: GetStoryDetailResponse) {
            titleTv.text = storyDetail.title
            scoreTv.text = storyDetail.score
            sourceTv.text = Utils.getDomainName(storyDetail.url)
            commentTv.text = storyDetail.descendants

            itemView.setOnClickListener {
                if (!TextUtils.isEmpty(storyDetail.url)) {
                    FinestWebView.Builder(itemView.context).show(storyDetail.url!!)
                }
            }
        }
    }
}
