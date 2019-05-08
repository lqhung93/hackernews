package hackernews.propertyguru.com.hackernews.utils

import java.net.URI

/**
 * Created by hung on 5/9/19.
 */
class Utils {

    companion object {
        fun getDomainName(url: String?): String? {
            val uri: URI
            return try {
                uri = URI(url)
                val domain = uri.host
                if (domain.startsWith("www.")) domain.substring(4) else domain
            } catch (e: Exception) {
                url
            }

        }
    }
}