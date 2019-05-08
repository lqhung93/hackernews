package hackernews.propertyguru.com.hackernews.network.responses

import com.google.gson.annotations.SerializedName

/**
 * Created by hung on 5/8/19.
 */
class GetStoryDetailResponse(@SerializedName(value = "by") val by: String?,
                             @SerializedName(value = "descendants") val descendants: String?,
                             @SerializedName(value = "id") val id: String?,
                             @SerializedName(value = "kids") val kids: List<String>?,
                             @SerializedName(value = "score") val score: String?,
                             @SerializedName(value = "time") val time: String?,
                             @SerializedName(value = "title") val title: String?,
                             @SerializedName(value = "type") val type: String?,
                             @SerializedName(value = "url") val url: String?,
                             @SerializedName(value = "parent") val parent: String?,
                             @SerializedName(value = "text") val text: String?)
