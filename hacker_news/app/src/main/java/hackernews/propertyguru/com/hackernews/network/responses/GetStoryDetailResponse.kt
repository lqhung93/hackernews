package hackernews.propertyguru.com.hackernews.network.responses

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by hung on 5/8/19.
 */
class GetStoryDetailResponse(@SerializedName(value = "by") val by: String?,
                             @SerializedName(value = "descendants") val descendants: String?,
                             @SerializedName(value = "id") val id: String?,
                             @SerializedName(value = "kids") val kids: ArrayList<String>?,
                             @SerializedName(value = "score") val score: String?,
                             @SerializedName(value = "time") val time: String?,
                             @SerializedName(value = "title") val title: String?,
                             @SerializedName(value = "type") val type: String?,
                             @SerializedName(value = "url") val url: String?,
                             @SerializedName(value = "parent") val parent: String?,
                             @SerializedName(value = "text") val text: String?) : Serializable {

    override fun toString(): String {
        return "GetStoryDetailResponse(by=$by, descendants=$descendants, id=$id, kids=$kids, score=$score, time=$time, title=$title, type=$type, url=$url, parent=$parent, text=$text)"
    }
}
