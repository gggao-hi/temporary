import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.sqlite.*
import org.sqlite.Function
import java.sql.Connection
import java.sql.DriverManager

class DBHelper {
    companion object {
        private const val KEY_ROM_VERSION = "romVersion"
        private const val KEY_APP_VERSION = "appVersion"
        private const val KEY_DISABLED_FEATURE_COLLECTION = "disabledFeatureCollection"

    }

    private val gson = GsonBuilder().create()
    private fun openDataBase(sqlPath: String): Connection? {

        var conn: Connection? = null
        Class.forName("org.sqlite.JDBC")
        Logger.write("after register")
        try {
            conn = DriverManager.getConnection("${JDBC.PREFIX}${sqlPath}")
            Logger.write("after getConnection")

        } catch (e: Exception) {
            Logger.write("after getConnection:${e.message}")
        }

        return conn
    }

    fun query(sqlPath: String, callback: (List<Item>) -> Unit) {
        try {
            val connection = openDataBase(sqlPath)
            Logger.write("after openDataBase")
            val statement = connection?.createStatement()
            Logger.write("after createStatement")
            val resultSet = statement?.executeQuery("SELECT * FROM Rule")
            Logger.write("after executeQuery")
            val items = mutableListOf<Item>()
            while (resultSet?.next() == true) {
                val appVersion = resultSet.getString(resultSet.findColumn(KEY_APP_VERSION))
                val romVersion = resultSet.getString(resultSet.findColumn(KEY_ROM_VERSION))
                val features = resultSet.getString(resultSet.findColumn(KEY_DISABLED_FEATURE_COLLECTION))
                val featureList = gson.fromJson<List<String>>(features, object : TypeToken<List<String>>() {}.type)
                items.add(Item(appVersion, romVersion, featureList))
            }
            callback.invoke(items)
            statement?.closeOnCompletion()
            connection?.close()

        } catch (e: Exception) {
            Logger.write(e.message!!)

        }


    }

    data class Item(val appVersion: String?, val romVersion: String?, val features: List<String>?)
}

