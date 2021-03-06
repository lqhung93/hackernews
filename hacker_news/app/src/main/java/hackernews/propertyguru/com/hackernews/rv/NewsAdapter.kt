package hackernews.propertyguru.com.hackernews.rv

import android.content.Intent
import android.support.v7.util.SortedList
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
class NewsAdapter : RecyclerView.Adapter<NewsAdapter.RowHolder>() {

    private var storyDetails = SortedList<GetStoryDetailResponse>(GetStoryDetailResponse::class.java, object : SortedList.Callback<GetStoryDetailResponse>() {

        override fun compare(res0: GetStoryDetailResponse?, res1: GetStoryDetailResponse?): Int {
            val tmp0 = res0?.time?.toLong() ?: 0
            val tmp1 = res1?.time?.toLong() ?: 0
            // Decrease order
            return tmp1.compareTo(tmp0)
        }

        override fun onChanged(position: Int, count: Int) {
            notifyItemRangeChanged(position, count)
        }

        override fun areContentsTheSame(res0: GetStoryDetailResponse?, res1: GetStoryDetailResponse?): Boolean {
            return res0?.title.equals(res1?.time) || res0?.text.equals(res1?.text)
        }

        override fun areItemsTheSame(res0: GetStoryDetailResponse?, res1: GetStoryDetailResponse?): Boolean {
            return res0?.id.equals(res1?.id)
        }

        override fun onInserted(position: Int, count: Int) {
            notifyItemRangeInserted(position, count)
        }

        override fun onRemoved(position: Int, count: Int) {
            notifyItemRangeRemoved(position, count)
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            notifyItemMoved(fromPosition, toPosition)
        }
    })

    fun add(detail: GetStoryDetailResponse) {
        storyDetails.beginBatchedUpdates()
        storyDetails.add(detail)
        storyDetails.endBatchedUpdates()
    }

    fun addAll(details: List<GetStoryDetailResponse>) {
        storyDetails.beginBatchedUpdates()
        for (i in details.indices) {
            storyDetails.add(details[i])
        }
        storyDetails.endBatchedUpdates()
    }

    fun clear() {
        storyDetails.beginBatchedUpdates()
        //remove items at end, to avoid unnecessary array shifting
        while (storyDetails.size() > 0) {
            storyDetails.removeItemAt(storyDetails.size() - 1)
        }
        storyDetails.endBatchedUpdates()
    }

    fun get(position: Int): GetStoryDetailResponse {
        return storyDetails.get(position)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowHolder {
        return RowHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_news, parent, false))
    }

    override fun getItemCount(): Int {
        return storyDetails.size()
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
            dateTv.text = Utils.toDateTime((storyDetail.time?.toLong() ?: 0) * 1000)

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
                    itemView.context.startActivity(intent)
                }
            }
        }
    }
}
