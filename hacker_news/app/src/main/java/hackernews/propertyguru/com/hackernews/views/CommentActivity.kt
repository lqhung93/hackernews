package hackernews.propertyguru.com.hackernews.views

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import hackernews.propertyguru.com.hackernews.R
import hackernews.propertyguru.com.hackernews.network.responses.GetStoryDetailResponse
import hackernews.propertyguru.com.hackernews.rv.CommentsAdapter
import hackernews.propertyguru.com.hackernews.rv.CustomRecyclerView
import hackernews.propertyguru.com.hackernews.utils.C
import hackernews.propertyguru.com.hackernews.utils.LogUtils
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

class CommentActivity : BaseActivity() {

    private val TAG = LogUtils.makeTag(CommentActivity::class.java)

    private var commentsRefreshLayout: SwipeRefreshLayout? = null
    private var commentsRecyclerView: CustomRecyclerView? = null
    private var storyDetails: ArrayList<GetStoryDetailResponse> = arrayListOf()
    private var commentsAdapter = CommentsAdapter(storyDetails)

    private var idsStack: Stack<String> = Stack()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)

        val linearLayoutManager = LinearLayoutManager(this)

        commentsRefreshLayout = findViewById(R.id.comments_swipe_view)
        commentsRefreshLayout?.setOnRefreshListener {
            invokeApis()
        }
        commentsRecyclerView = findViewById(R.id.comments_rv)
        commentsRecyclerView?.layoutManager = linearLayoutManager

        val itemDecor = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        commentsRecyclerView?.addItemDecoration(itemDecor)

        commentsRecyclerView?.setEmptyView(findViewById(R.id.empty_view))
        commentsRecyclerView?.adapter = commentsAdapter

        invokeApis()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onGettingStoryDetail(response: GetStoryDetailResponse) {
        LogUtils.e(TAG, "Comment detail: $response")

        if (TextUtils.isEmpty(response.type) || (response.type != "comment")) {
            getCommentDetail()
            return
        }

        if (!TextUtils.isEmpty(response.text)) {
            var index = storyDetails.indexOf(response)
            if (index != -1) {
                storyDetails[index] = response
            } else {
                storyDetails.add(response)
                index = storyDetails.indexOf(response)
            }

            commentsAdapter.notifyItemChanged(index)
        }

        val kids = response.kids
        if (kids != null && kids.isNotEmpty()) {
            idsStack.addAll(kids)
        }

        getCommentDetail()

        commentsRefreshLayout?.isRefreshing = false
    }

    private fun invokeApis() {
        storyDetails.clear()

        // Already check null and last activity
        idsStack.addAll(intent.getSerializableExtra(C.COMMENT_LIST) as ArrayList<String>)

        getCommentDetail()
    }

    private fun getCommentDetail() {
        try {
            pollingCenter.getStoryDetail(idsStack.pop())
        } catch (e: EmptyStackException) {

        }
    }
}
