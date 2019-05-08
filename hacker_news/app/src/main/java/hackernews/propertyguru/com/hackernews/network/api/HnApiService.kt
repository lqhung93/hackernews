package hackernews.propertyguru.com.hackernews.network.api

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import hackernews.propertyguru.com.hackernews.BuildConfig
import hackernews.propertyguru.com.hackernews.network.responses.GetTopStoriesDeserializer
import hackernews.propertyguru.com.hackernews.network.responses.GetTopStoriesResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DateFormat
import java.util.concurrent.TimeUnit

/**
 * Created by hung on 5/8/19.
 */
class HnApiService {

    companion object {
        private var hnApiService: HnService? = null
        private var retrofit: Retrofit? = null

        fun getService(): HnService {
            if (hnApiService == null) {
                hnApiService = getRetrofit().create(HnService::class.java)
            }

            return hnApiService!!
        }

        private fun getRetrofit(): Retrofit {
            if (retrofit == null) {
                val gson = GsonBuilder()
                        .registerTypeAdapter(GetTopStoriesResponse::class.java, GetTopStoriesDeserializer())
                        .enableComplexMapKeySerialization()
                        .serializeNulls()
                        .setDateFormat(DateFormat.LONG)
                        .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                        .setPrettyPrinting()
                        .setVersion(1.0)
                        .setLenient()
                        .create()

                val interceptor = HttpLoggingInterceptor()
                interceptor.level = HttpLoggingInterceptor.Level.BODY

                val client = OkHttpClient.Builder()
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .writeTimeout(30, TimeUnit.SECONDS)
                        .addInterceptor(interceptor)
                        .build()
                retrofit = Retrofit.Builder()
                        .baseUrl(BuildConfig.END_POINT)
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build()
            }

            return retrofit!!
        }
    }
}