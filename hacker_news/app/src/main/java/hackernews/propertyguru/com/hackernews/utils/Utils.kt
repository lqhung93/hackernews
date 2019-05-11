package hackernews.propertyguru.com.hackernews.utils

import java.net.URI
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * Created by hung on 5/9/19.
 */
class Utils {

    companion object {
        val times: MutableMap<String, Long> = hashMapOf(
                "year" to TimeUnit.DAYS.toMillis(365),
                "month" to TimeUnit.DAYS.toMillis(30),
                "week" to TimeUnit.DAYS.toMillis(7),
                "day" to TimeUnit.DAYS.toMillis(1),
                "hour" to TimeUnit.HOURS.toMillis(1),
                "minute" to TimeUnit.MINUTES.toMillis(1),
                "second" to TimeUnit.SECONDS.toMillis(1)
        )

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

        fun toRelative(duration: Long, maxLevel: Int): String {
            var duration = duration
            val res = StringBuilder()
            var level = 0
            for (time in times.entries) {
                val timeDelta = duration / time.value
                if (timeDelta > 0) {
                    res.append(timeDelta)
                            .append(" ")
                            .append(time.key)
                            .append(if (timeDelta > 1) "s" else "")
                            .append(", ")
                    duration -= time.value * timeDelta
                    level++
                }
                if (level == maxLevel) {
                    break
                }
            }
            if ("" == res.toString()) {
                return "0 seconds ago"
            } else {
                res.setLength(res.length - 2)
                res.append(" ago")
                return res.toString()
            }
        }

        fun toRelative(duration: Long): String {
            return toRelative(duration, times.size)
        }

        fun toRelative(start: Date, end: Date): String {
            return toRelative(end.time - start.time)
        }

        fun toRelative(start: Date, end: Date, level: Int): String {
            return toRelative(end.time - start.time, level)
        }
    }
}