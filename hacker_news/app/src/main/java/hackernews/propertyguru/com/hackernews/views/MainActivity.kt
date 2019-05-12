package hackernews.propertyguru.com.hackernews.views

import android.os.Bundle
import android.os.Parcelable
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import hackernews.propertyguru.com.hackernews.R
import hackernews.propertyguru.com.hackernews.network.responses.GetStoryDetailResponse
import hackernews.propertyguru.com.hackernews.network.responses.GetTopStoriesResponse
import hackernews.propertyguru.com.hackernews.rv.NewsAdapter
import hackernews.propertyguru.com.hackernews.utils.C
import hackernews.propertyguru.com.hackernews.utils.LogUtils
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : BaseActivity() {

    private val TAG = LogUtils.makeTag(MainActivity::class.java)

    private var newsRefreshLayout: SwipeRefreshLayout? = null
    private var newsRecyclerView: CustomRecyclerView? = null
    private var newsAdapter = NewsAdapter()

    private var recyclerViewState: Parcelable? = null

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

        newsRecyclerView?.setEmptyView(findViewById(R.id.empty_view))
        newsRecyclerView?.adapter = newsAdapter

        invokeApis()
    }

    override fun onResume() {
        super.onResume()
        newsRecyclerView?.layoutManager?.onRestoreInstanceState(recyclerViewState)
        newsRecyclerView = null
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        recyclerViewState = savedInstanceState?.getParcelable(C.RECYCLER_VIEW_STATE) as Parcelable
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        recyclerViewState = newsRecyclerView?.layoutManager?.onSaveInstanceState()
        outState?.putParcelable(C.RECYCLER_VIEW_STATE, recyclerViewState)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onGettingTopStories(response: GetTopStoriesResponse) {
        response.ids.forEach {
            pollingCenter.getStoryDetail(it)
        }

        newsRefreshLayout?.isRefreshing = false
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onGettingStoryDetail(response: GetStoryDetailResponse) {
        LogUtils.d(TAG, "Story detail: $response")
        newsAdapter.add(response)
    }

    private fun invokeApis() {
        newsAdapter.clear()
        pollingCenter.getTopStories()
    }
}
