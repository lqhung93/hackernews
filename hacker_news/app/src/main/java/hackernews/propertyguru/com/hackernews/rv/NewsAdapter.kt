package hackernews.propertyguru.com.hackernews.rv

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.thefinestartist.finestwebview.FinestWebView
import hackernews.propertyguru.com.hackernews.R
import hackernews.propertyguru.com.hackernews.network.responses.GetStoryDetailResponse
import hackernews.propertyguru.com.hackernews.utils.C
import hackernews.propertyguru.com.hackernews.utils.Utils
import hackernews.propertyguru.com.hackernews.views.CommentActivity

/**
 * Created by hung on 5/8/19.
 */
class NewsAdapter(private val storyDetails: ArrayList<GetStoryDetailResponse>) : RecyclerView.Adapter<NewsAdapter.RowHolder>() {

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
        private val dateTv: TextView = v.findViewById(R.id.date_tv)

        fun bindData(storyDetail: GetStoryDetailResponse) {
            titleTv.text = storyDetail.title
            scoreTv.text = storyDetail.score
            sourceTv.text = Utils.getDomainName(storyDetail.url)
            commentTv.text = storyDetail.descendants
            dateTv.text = Utils.toRelative(storyDetail.time?.toLong() ?: 0)

            itemView.setOnClickListener {
                if (!TextUtils.isEmpty(storyDetail.url)) {
                    FinestWebView.Builder(itemView.context).show(storyDetail.url!!)
                }
            }

            val kids = storyDetail.kids
            if (kids != null && kids.isNotEmpty()) {
                commentBtn.setOnClickListener {
                    val intent = Intent(itemView.context, CommentActivity::class.java)
                    intent.putExtra(C.COMMENT_LIST, kids)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }
}
