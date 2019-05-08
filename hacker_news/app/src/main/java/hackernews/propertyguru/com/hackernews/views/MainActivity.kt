package hackernews.propertyguru.com.hackernews.views

import android.os.Bundle
import hackernews.propertyguru.com.hackernews.R
import hackernews.propertyguru.com.hackernews.network.responses.GetTopStoriesResponse
import hackernews.propertyguru.com.hackernews.rv.CustomRecyclerView
import hackernews.propertyguru.com.hackernews.utils.LogUtils
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : BaseActivity() {

    private val TAG = LogUtils.makeTag(MainActivity::class.java)

    private var newRecyclerView: CustomRecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        newRecyclerView = findViewById(R.id.news_rv)
        newRecyclerView?.setEmptyView(findViewById(R.id.empty_view))

        pollingCenter.getTopStories()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onGettingTopStories(response: GetTopStoriesResponse) {
        response.ids.forEach {
            LogUtils.e(TAG, "it: $it")
        }
    }
}
