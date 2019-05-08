package hackernews.propertyguru.com.hackernews.rv

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import hackernews.propertyguru.com.hackernews.utils.LogUtils
import org.jetbrains.annotations.Nullable

/**
 * Created by hung on 5/8/19.
 */
class CustomRecyclerView : RecyclerView {

    private val TAG = LogUtils.makeTag(CustomRecyclerView::class.java)
    private var mEmptyView: View? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, @Nullable attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, @Nullable attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    private fun initEmptyView() {
        if (mEmptyView != null) {
            mEmptyView!!.visibility = if (adapter == null || adapter!!.itemCount == 0) VISIBLE else View.GONE
            this.visibility = if (adapter == null || adapter!!.itemCount == 0) View.GONE else VISIBLE
        }
    }

    private val observer: RecyclerView.AdapterDataObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            super.onChanged()
            initEmptyView()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            super.onItemRangeInserted(positionStart, itemCount)
            initEmptyView()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            super.onItemRangeRemoved(positionStart, itemCount)
            initEmptyView()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            super.onItemRangeChanged(positionStart, itemCount)
            initEmptyView()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            super.onItemRangeChanged(positionStart, itemCount, payload)
            initEmptyView()
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount)
            initEmptyView()
        }
    }

    override fun setAdapter(adapter: RecyclerView.Adapter<*>?) {
        val oldAdapter = getAdapter()
        super.setAdapter(adapter)
        oldAdapter?.unregisterAdapterDataObserver(observer)
        adapter?.registerAdapterDataObserver(observer)
    }

    fun setEmptyView(view: View) {
        this.mEmptyView = view
        initEmptyView()
    }
}