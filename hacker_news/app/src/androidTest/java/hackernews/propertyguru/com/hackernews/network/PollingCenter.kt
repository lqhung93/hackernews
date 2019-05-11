package hackernews.propertyguru.com.hackernews.network

import com.github.tomakehurst.wiremock.client.WireMock.*
import hackernews.propertyguru.com.hackernews.utils.AssetsReader
import hackernews.propertyguru.com.hackernews.utils.Constants
import java.net.HttpURLConnection

/**
 * Created by hung on 5/11/19.
 */
class PollingCenter {

    companion object {

        private var pollingCenter: PollingCenter? = null

        fun getPollingCenter(): PollingCenter {
            if (pollingCenter == null) {
                pollingCenter = PollingCenter()
            }

            return pollingCenter!!
        }
    }

    fun getTopStories() {
        val url = "topstories.json"
        BaseUrlChangingInterceptor.get().setInterceptor(Constants.LOCAL_HOST + url)
        val jsonBody = AssetsReader.asset("topstories.json")
        stubFor(get(urlPathMatching(url))
                .willReturn(aResponse()
                        .withStatus(HttpURLConnection.HTTP_OK)
                        .withBody(jsonBody)))
    }

    fun getStoryDetail(id: String) {
        val url = "item/$id.json"
        BaseUrlChangingInterceptor.get().setInterceptor(Constants.LOCAL_HOST + url)
        val jsonBody = AssetsReader.asset("$id.json")
        stubFor(get(urlPathMatching(url))
                .willReturn(aResponse()
                        .withStatus(HttpURLConnection.HTTP_OK)
                        .withBody(jsonBody)))
    }
}