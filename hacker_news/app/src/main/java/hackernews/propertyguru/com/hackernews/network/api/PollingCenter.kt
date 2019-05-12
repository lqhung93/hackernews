package hackernews.propertyguru.com.hackernews.network.api

import hackernews.propertyguru.com.hackernews.network.responses.ErrorData
import hackernews.propertyguru.com.hackernews.network.responses.GetStoryDetailResponse
import hackernews.propertyguru.com.hackernews.network.responses.GetTopStoriesResponse
import hackernews.propertyguru.com.hackernews.utils.C
import hackernews.propertyguru.com.hackernews.utils.LogUtils
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection

/**
 * Created by hung on 5/8/19.
 */
class PollingCenter private constructor() {

    private val TAG = LogUtils.makeTag(PollingCenter::class.java)

    private val hnService = HnApiService.getService()

    companion object {

        private var pollingCenter: PollingCenter? = null

        fun getPollingCenter(): PollingCenter {
            if (pollingCenter == null) {
                pollingCenter = PollingCenter()
            }

            return pollingCenter!!
        }
    }

    internal interface IExecuteAction {
        fun onSuccess(call: Call<Any>?, response: Response<Any>?)
    }

    fun getTopStories() {
        synchronized(C.LOCK) {
            val getTopStoriesResponseCall = hnService.getTopStories() as Call<Any>
            execute(getTopStoriesResponseCall, object : IExecuteAction {
                override fun onSuccess(call: Call<Any>?, response: Response<Any>?) {
                    if (response?.body() != null) {
                        EventBus.getDefault().post(response?.body() as GetTopStoriesResponse)
                    }
                }
            })
        }
    }

    fun getStoryDetail(storyId: String) {
        synchronized(C.LOCK) {
            val getTopStoriesResponseCall = hnService.getStoryDetail(storyId) as Call<Any>
            execute(getTopStoriesResponseCall, object : IExecuteAction {
                override fun onSuccess(call: Call<Any>?, response: Response<Any>?) {
                    if (response?.body() != null) {
                        EventBus.getDefault().post(response?.body() as GetStoryDetailResponse)
                    }
                }
            })
        }
    }

    private fun execute(call: Call<Any>, callback: IExecuteAction) {
        call.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>?, response: Response<Any>?) {
                if (response?.code() == HttpURLConnection.HTTP_OK) {
                    callback.onSuccess(call, response)
                } else {
                    EventBus.getDefault().post(ErrorData(response))
                }
            }

            override fun onFailure(call: Call<Any>?, t: Throwable?) {
                EventBus.getDefault().post(ErrorData(t))
            }
        })
    }
}