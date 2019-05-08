package hackernews.propertyguru.com.hackernews.network.responses

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

/**
 * Created by hung on 5/8/19.
 */
class GetTopStoriesDeserializer : JsonDeserializer<GetTopStoriesResponse> {

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): GetTopStoriesResponse {
        val ids = arrayListOf<String>()

        json?.asJsonArray?.forEach {
            ids.add(it.asString)
        }

        return GetTopStoriesResponse(ids)
    }
}