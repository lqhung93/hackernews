package hackernews.propertyguru.com.hackernews.utils

import android.support.test.InstrumentationRegistry
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

/**
 * Created by hung on 5/11/19.
 */
class AssetsReader {

    companion object {

        private const val BUFFER_SIZE = 4 * 1024

        fun asset(assetPath: String): String {
            try {
                val inputStream = InstrumentationRegistry.getTargetContext().assets.open("$assetPath")
                return inputStreamToString(inputStream, "UTF-8")
            } catch (e: IOException) {
                e.printStackTrace()
                throw RuntimeException(e)
            }

        }

        private fun inputStreamToString(inputStream: InputStream, charsetName: String): String {
            val builder = StringBuilder()
            val reader = InputStreamReader(inputStream, charsetName)
            val buffer = CharArray(BUFFER_SIZE)
            var length = reader.read(buffer)

            reader.use { reader ->
                while (length != -1) {
                    builder.append(buffer, 0, length)
                    length = reader.read(buffer)
                }
            }
            return builder.toString()
        }
    }
}