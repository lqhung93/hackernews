package hackernews.propertyguru.com.hackernews.rv

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import hackernews.propertyguru.com.hackernews.R
import hackernews.propertyguru.com.hackernews.network.responses.GetStoryDetailResponse

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

        fun bindData(storyDetail: GetStoryDetailResponse) {
            titleTv.text = storyDetail.title
        }
    }
}
