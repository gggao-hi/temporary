import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.res.ResourceLoader
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader

class VerifyHelper {
    private val gson = GsonBuilder().create()
    var onVerifyResultListener: OnVerifyResultListener? = null
    fun verify(data: List<DBHelper.Item>) {
        verifyAppVersion(data)
        verifyRomVersion(data)
        verifyDisabledFeatures(data)
    }

    @OptIn(ExperimentalComposeUiApi::class)
    fun loadDisabledFeatures(): List<String> {
        return ResourceLoader.Default.load("disabled_features.json").let { stream ->
            InputStreamReader(stream).let { reader ->
                val result = reader.readText()
                stream.close()
                reader.close()
                gson.fromJson(result, object : TypeToken<List<String>>() {}.type)
            }
        }
    }

    private fun isDigit(str: String): Boolean {

        var result = true
        str.forEach {
            if (!it.isDigit()) {
                result = false
            }
        }
        return result

    }

    private fun verifyAppVersion(data: List<DBHelper.Item>) {

        val appResults = mutableListOf<String>()
        data.forEach { item ->
            var result: String? = null
            if ("any" != item.appVersion) {
                item.appVersion?.apply {

                    this.split(".").takeIf { it.size == 5 }?.apply {
                        var isCorrect = true
                        this.forEach {
                            if (!isDigit(it)) {
                                isCorrect = false
                            }
                        }

                        if (!isCorrect) {
                            result =
                                "the Rom Version:${item.romVersion}; disabled features:${item.features?.toString()} has error appVersion:${item.appVersion}"
                        }
                    } ?: run {
                        "the Rom Version:${item.romVersion}; disabled features:${item.features?.toString()} has error appVersion:${item.appVersion}"
                    }


                } ?: run {
                    result =
                        "the Rom Version:${item.romVersion}; disabled features:${item.features?.toString()} has no appVersion"
                }
            }

            result?.apply { appResults.add(this) }

        }
        onVerifyResultListener?.onAppVersionResult(appResults)

    }

    private fun verifyRomVersion(data: List<DBHelper.Item>) {
        val appResults = mutableListOf<String>()
        data.forEach { item ->
            var result: String? = null
            if ("any" != item.romVersion) {
                item.romVersion?.apply {

                    this.split(".").takeIf { it.size == 5 }?.apply {
                        var isCorrect = true
                        this.forEach {
                            if (!isDigit(it)) {
                                isCorrect = false
                            }
                        }

                        if (isCorrect) {
                            val firstCorrect = (this[0] == "13" || this[0] == "15")
                            val secondCorrect = this[1] == "1"
                            val thirdCorrect = if (this[0] == "13") {
                                this[2] == "26" || this[2] == "29" || this[2] == "32" || this[2] == "33"
                            } else {
                                this[2] == "9" || this[2] == "28"
                            }
                            val fourthCorrect = this[3].matches(Regex("^\\d{2}[01]\\d[0123]\\d0\$"))
                            val fifthCorrect = this[4] == "1" || this[4] == "2"
                            isCorrect = firstCorrect && secondCorrect && thirdCorrect && fourthCorrect && fifthCorrect
                        }
                        if (!isCorrect) {
                            result =
                                "the App Version:${item.appVersion}; disabled features:${item.features?.toString()} has error romVersion:${item.romVersion}"
                        }
                    } ?: run {
                        result =
                            "the App Version:${item.appVersion}; disabled features:${item.features?.toString()} has error romVersion:${item.romVersion}"
                    }


                } ?: run {
                    result =
                        "the App Version:${item.appVersion}; disabled features:${item.features?.toString()} has no romVersion"
                }
            }

            result?.apply { appResults.add(this) }

        }
        onVerifyResultListener?.onRomVersionResult(appResults)


    }

    private fun verifyDisabledFeatures(data: List<DBHelper.Item>) {

        val features = loadDisabledFeatures()
        val result = mutableListOf<String>()
        data.forEach { item ->
            item.features?.forEach { feature ->
                if (!features.contains(feature)) {
                    result.add("the appVersion:${item.appVersion};romVersion:${item.romVersion} has disabled feature name error: the error name is $feature")
                }
            }
                ?: run { result.add("the appVersion:${item.appVersion};romVersion:${item.romVersion} has no disabled feature") }
        }

        onVerifyResultListener?.onDisabledFeaturesResult(result)


    }


    interface OnVerifyResultListener {
        fun onAppVersionResult(result: List<String>)
        fun onRomVersionResult(result: List<String>)
        fun onDisabledFeaturesResult(result: List<String>)
    }
}