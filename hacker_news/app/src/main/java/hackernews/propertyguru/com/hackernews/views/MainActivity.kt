package hackernews.propertyguru.com.hackernews.views

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import hackernews.propertyguru.com.hackernews.R
import hackernews.propertyguru.com.hackernews.network.responses.GetStoryDetailResponse
import hackernews.propertyguru.com.hackernews.network.responses.GetTopStoriesResponse
import hackernews.propertyguru.com.hackernews.rv.NewsAdapter
import hackernews.propertyguru.com.hackernews.utils.LogUtils
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : BaseActivity() {

    private val TAG = LogUtils.makeTag(MainActivity::class.java)

    private var newsRefreshLayout: SwipeRefreshLayout? = null
    private var newsRecyclerView: RecyclerView? = null
    private var storyDetails: ArrayList<GetStoryDetailResponse> = arrayListOf()
    private var newsAdapter = NewsAdapter(storyDetails)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val linearLayoutManager = LinearLayoutManager(this)

        newsRefreshLayout = findViewById(R.id.news_swipe_view)
        newsRefreshLayout?.setOnRefreshListener {
            invokeApis()
        }

        newsRecyclerView = findViewById(R.id.news_rv)
        newsRecyclerView?.layoutManager = linearLayoutManager

        val itemDecor = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        newsRecyclerView?.addItemDecoration(itemDecor)

//        newsRecyclerView?.setEmptyView(findViewById(R.id.empty_view))
        newsRecyclerView?.adapter = newsAdapter
    }

    override fun onStart() {
        super.onStart()
        invokeApis()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onGettingTopStories(response: GetTopStoriesResponse) {
        val ids = response.ids

        ids.forEach {
            pollingCenter.getStoryDetail(it)

            if (it == ids.last()) {
                newsRefreshLayout?.isRefreshing = false
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onGettingStoryDetail(response: GetStoryDetailResponse) {
        LogUtils.e(TAG, "Res: $response")

        var index = storyDetails.indexOf(response)
        if (index != -1) {
            storyDetails[index] = response
        } else {
            storyDetails.add(response)
            index = storyDetails.indexOf(response)
        }

        newsAdapter.notifyItemChanged(index)
    }

    private fun invokeApis() {
        storyDetails.clear()
        pollingCenter.getTopStories()
    }
}
