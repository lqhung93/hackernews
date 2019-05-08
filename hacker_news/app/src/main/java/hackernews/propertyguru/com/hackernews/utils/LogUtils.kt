package hackernews.propertyguru.com.hackernews.utils

import android.util.Log
import hackernews.propertyguru.com.hackernews.BuildConfig

/**
 * Created by hung on 5/8/19.
 */
class LogUtils {

    companion object {

        private const val MAX_LOG_TAG_LENGTH = 23
        private const val LOG_PREFIX = "hn_"

        enum class LogLevel constructor(internal val id: Int) {
            NONE(0),
            VERBOSE(1),
            DEBUG(2),
            INFO(3),
            WARN(4),
            ERROR(5)
        }

        // http://stackoverflow.com/questions/6321555/what-is-the-size-limit-for-logcat-and-how-to-change-its-capacity
        // Format: E/[tag]:[msg] => 4076 characters
        private const val MAX_LOG_MESSAGE_INCLUDE_TAG_LENGTH = 4076 - 3 // Remove: "E", "/", ":"

        fun makeTag(str: String): String {
            return if (str.length > MAX_LOG_TAG_LENGTH - LOG_PREFIX.length) {
                LOG_PREFIX + str.substring(0, MAX_LOG_TAG_LENGTH - LOG_PREFIX.length - 1)
            } else LOG_PREFIX + str

        }

        fun makeTag(cls: Class<*>): String {
            return makeTag(cls.simpleName)
        }

        /**
         * When log message is too long, logcat can't show log message enough.
         * This API handles this problem.
         *
         * @param tag Used to identify the source of a log message.  It usually identifies
         * the class or activity where the log call occurs.
         * @param msg The message you would like logged.
         */
        fun v(tag: String, msg: String) {
            val maxLogLength = MAX_LOG_MESSAGE_INCLUDE_TAG_LENGTH - tag.length

            for (i in 0..msg.length / maxLogLength) {
                val start = i * maxLogLength
                var end = (i + 1) * maxLogLength
                end = if (end > msg.length) msg.length else end
                if (canPrint(LogLevel.VERBOSE)) {
                    Log.v(tag, msg.substring(start, end))
                }
            }
        }

        /**
         * When log message is too long, logcat can't show log message enough.
         * This API handles this problem.
         *
         * @param tag Used to identify the source of a log message.  It usually identifies
         * the class or activity where the log call occurs.
         * @param msg The message you would like logged.
         */
        fun d(tag: String, msg: String) {
            val maxLogLength = MAX_LOG_MESSAGE_INCLUDE_TAG_LENGTH - tag.length

            for (i in 0..msg.length / maxLogLength) {
                val start = i * maxLogLength
                var end = (i + 1) * maxLogLength
                end = if (end > msg.length) msg.length else end
                if (canPrint(LogLevel.DEBUG)) {
                    Log.d(tag, msg.substring(start, end))
                }
            }
        }

        /**
         * When log message is too long, logcat can't show log message enough.
         * This API handles this problem.
         *
         * @param tag Used to identify the source of a log message.  It usually identifies
         * the class or activity where the log call occurs.
         * @param msg The message you would like logged.
         */
        fun i(tag: String, msg: String) {
            val maxLogLength = MAX_LOG_MESSAGE_INCLUDE_TAG_LENGTH - tag.length

            for (i in 0..msg.length / maxLogLength) {
                val start = i * maxLogLength
                var end = (i + 1) * maxLogLength
                end = if (end > msg.length) msg.length else end
                if (canPrint(LogLevel.INFO)) {
                    Log.i(tag, msg.substring(start, end))
                }
            }
        }

        /**
         * When log message is too long, logcat can't show log message enough.
         * This API handles this problem.
         *
         * @param tag Used to identify the source of a log message.  It usually identifies
         * the class or activity where the log call occurs.
         * @param msg The message you would like logged.
         */
        fun w(tag: String, msg: String) {
            val maxLogLength = MAX_LOG_MESSAGE_INCLUDE_TAG_LENGTH - tag.length

            for (i in 0..msg.length / maxLogLength) {
                val start = i * maxLogLength
                var end = (i + 1) * maxLogLength
                end = if (end > msg.length) msg.length else end
                if (canPrint(LogLevel.WARN)) {
                    Log.w(tag, msg.substring(start, end))
                }
            }
        }

        /**
         * When log message is too long, logcat can't show log message enough.
         * This API handles this problem.
         *
         * @param tag Used to identify the source of a log message.  It usually identifies
         * the class or activity where the log call occurs.
         * @param msg The message you would like logged.
         */
        fun e(tag: String, msg: String) {
            val maxLogLength = MAX_LOG_MESSAGE_INCLUDE_TAG_LENGTH - tag.length

            for (i in 0..msg.length / maxLogLength) {
                val start = i * maxLogLength
                var end = (i + 1) * maxLogLength
                end = if (end > msg.length) msg.length else end
                if (canPrint(LogLevel.ERROR)) {
                    Log.e(tag, msg.substring(start, end))
                }
            }
        }

        /**
         * When log message is too long, logcat can't show log message enough.
         * This API handles this problem.
         *
         * @param tag Used to identify the source of a log message.  It usually identifies
         * the class or activity where the log call occurs.
         * @param msg The message you would like logged.
         */
        fun e(tag: String, msg: String, t: Throwable) {
            val maxLogLength = MAX_LOG_MESSAGE_INCLUDE_TAG_LENGTH - tag.length

            for (i in 0..msg.length / maxLogLength) {
                val start = i * maxLogLength
                var end = (i + 1) * maxLogLength
                end = if (end > msg.length) msg.length else end
                if (canPrint(LogLevel.ERROR)) {
                    Log.e(tag, msg.substring(start, end), t)
                }
            }
        }

        // Utils
        private fun canPrint(level: LogLevel): Boolean {
            return BuildConfig.DEBUG
        }
    }
}