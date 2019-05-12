package hackernews.propertyguru.com.hackernews.network.responses

/**
 * Created by hung on 5/8/19.
 */
class GetTopStoriesResponse(val ids: ArrayList<String>) {

    override fun toString(): String {
        return "GetTopStoriesResponse(ids=$ids)"
    }
}
