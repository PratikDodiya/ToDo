package com.pd.todo.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import java.text.SimpleDateFormat
import java.util.*

// TAG - classname prints in main method only but can't prints in retrofit inner methods or others
val Any.TAG: String
    get() {
//        val tag = javaClass.simpleName
//        return if (tag.length <= 23) tag else tag.substring(0, 23)
        return if (!javaClass.isAnonymousClass) {
            val name = javaClass.simpleName
            if (name.length <= 23) name else name.substring(0, 23)// first 23 chars
        } else {
            val name = javaClass.name
            if (name.length <= 23) name else name.substring(name.length - 23, name.length)// last 23 chars
        }
    }


fun hideKeyboard(context: Context, view: View?) {
    val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view?.windowToken, 0)
}

fun isOnline(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val netInfo = connectivityManager.activeNetworkInfo
    return netInfo != null && netInfo.isConnected
}

fun Context.toast(message: CharSequence, duration: Int) =
    Toast.makeText(this, message, duration).show()

fun String.isValidEmail(): Boolean = this.isNotEmpty() &&
        Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun getRequestJSONBody(value: String): RequestBody =
    RequestBody.create("application/json".toMediaTypeOrNull(), value)

/**
 * Wrapping try/catch to ignore catch block
 */
inline fun <T> justTry(block: () -> T) = try {
    block()
} catch (e: Throwable) {
    e.printStackTrace()
}

//Date Format
const val DEFAULT_DATE_FORMAT = "yyyy-MM-dd" //Default Date Object Format
const val DEFAULT_TIME_FORMAT = "HH:mm" //Default Time Object Format
const val DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm" //Default Time Object Format

fun convertDateToString(objDate: Date?, parseFormat: String): String {
    var value: String = ""
    justTry {
        return SimpleDateFormat(parseFormat).format(objDate)
    }
    return value
}

/**
 * Converts raw string to date object using [SimpleDateFormat]
 */
fun convertStringToDate(strDate: String, currentFormat: String): Date? {
    val simpleDateFormat = SimpleDateFormat(currentFormat, Locale.getDefault())

    var value: Date? = null
    justTry {
        value = simpleDateFormat.parse(strDate)
    }
    return value
}

fun convertDefaultDateString(strDate: String): String? {
    return convertDateToString(
        convertStringToDate(strDate, DEFAULT_DATE_FORMAT),
        DEFAULT_DATE_FORMAT
    )
}

@SuppressLint("SimpleDateFormat")
fun convertTimeString(strTime: String): String? {
    return SimpleDateFormat(DEFAULT_TIME_FORMAT).format(SimpleDateFormat(DEFAULT_TIME_FORMAT).parse(strTime))
}
