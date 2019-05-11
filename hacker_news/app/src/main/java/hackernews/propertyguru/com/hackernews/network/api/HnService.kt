package hackernews.propertyguru.com.hackernews.network.api

import hackernews.propertyguru.com.hackernews.network.responses.GetStoryDetailResponse
import hackernews.propertyguru.com.hackernews.network.responses.GetTopStoriesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by hung on 5/8/19.
 */
interface HnService {

    @GET("topstories.json")
    fun getTopStories(): Call<GetTopStoriesResponse>

    @GET("item/{id}.json")
    fun getStoryDetail(@Path("id") storyId: String): Call<GetStoryDetailResponse>
}