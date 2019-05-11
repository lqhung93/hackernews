package hackernews.propertyguru.com.hackernews.network

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * Created by hung on 5/11/19.
 */
class BaseUrlChangingInterceptor private constructor() : Interceptor {

    private var httpUrl: HttpUrl? = null


    fun setInterceptor(url: String) {
        httpUrl = HttpUrl.parse(url)
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var original = chain.request()

        if (httpUrl != null) {
            original = original.newBuilder()
                    .url(httpUrl)
                    .build()
        }
        return chain.proceed(original)
    }

    companion object {
        private var sInterceptor: BaseUrlChangingInterceptor? = null

        fun get(): BaseUrlChangingInterceptor {
            if (sInterceptor == null) {
                sInterceptor = BaseUrlChangingInterceptor()
            }
            return sInterceptor as BaseUrlChangingInterceptor
        }
    }
}