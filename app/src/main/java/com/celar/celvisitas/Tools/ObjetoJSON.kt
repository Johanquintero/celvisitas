import org.json.JSONObject
import java.lang.Exception

open class ObjetoJSON {
    var item: JSONObject?
    fun clearData() {
        item = null
    }

    constructor() {
        item = null
    }

    constructor(obj: JSONObject?) {
        item = obj
    }

    fun getField(field: String?): String {
        return try {
            item!!.getString(field)
        } catch (e: Exception) {
            ""
        }
    }

    fun setField(field: String?, value: String?) {
        try {
            item!!.put(field, value)
        } catch (e: Exception) {
        }
    }

    val iD: String
        get() = getField("id")
}


