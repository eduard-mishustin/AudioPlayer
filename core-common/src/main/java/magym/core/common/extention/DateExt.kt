package magym.core.common.extention

import java.text.SimpleDateFormat
import java.util.*

val currentTime get() = System.currentTimeMillis()

fun String.stringDateToLong(format: String = "MM:ss"): Long {
	return format.toDateFormat().parse(this).time
}

private fun String.toDateFormat(): SimpleDateFormat {
	return SimpleDateFormat(this, Locale.getDefault())
}