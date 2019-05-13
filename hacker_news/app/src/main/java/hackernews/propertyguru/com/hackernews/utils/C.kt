package hackernews.propertyguru.com.hackernews.utils

import com.google.common.annotations.VisibleForTesting
import hackernews.propertyguru.com.hackernews.BuildConfig

/**
 * Created by hung on 5/8/19.
 */
class C {

    companion object {
        val LOCK = Any()
        const val COMMENT_LIST = "COMMENT_LIST"
        const val RECYCLER_VIEW_STATE = "RECYCLER_VIEW_STATE"

        var END_POINT = BuildConfig.END_POINT
            @VisibleForTesting
            set(value) {
                field = value
            }
    }
}