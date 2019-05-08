package hackernews.propertyguru.com.hackernews.views

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import hackernews.propertyguru.com.hackernews.R
import hackernews.propertyguru.com.hackernews.network.responses.GetStoryDetailResponse
import hackernews.propertyguru.com.hackernews.network.responses.GetTopStoriesResponse
import hackernews.propertyguru.com.hackernews.rv.CustomRecyclerView
import hackernews.propertyguru.com.hackernews.rv.NewsAdapter
import hackernews.propertyguru.com.hackernews.utils.LogUtils
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : BaseActivity() {

    private val TAG = LogUtils.makeTag(MainActivity::class.java)

    private var newsRecyclerView: CustomRecyclerView? = null
    private var storyDetails: ArrayList<GetStoryDetailResponse> = arrayListOf()
    private var newsAdapter = NewsAdapter(storyDetails)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val linearLayoutManager = LinearLayoutManager(this)

        newsRecyclerView = findViewById(R.id.news_rv)
        newsRecyclerView?.layoutManager = linearLayoutManager

        newsRecyclerView?.setEmptyView(findViewById(R.id.empty_view))
        newsRecyclerView?.adapter = newsAdapter
    }

    override fun onStart() {
        super.onStart()
        pollingCenter.getTopStories()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onGettingTopStories(response: GetTopStoriesResponse) {
        response.ids.forEach {
            pollingCenter.getStoryDetail(it)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onGettingStoryDetail(response: GetStoryDetailResponse) {
        var index = storyDetails.indexOf(response)
        if (index != -1) {
            storyDetails[index] = response
        } else {
            storyDetails.add(response)
            index = storyDetails.indexOf(response)
        }

        newsAdapter.notifyItemChanged(index)
    }
}
