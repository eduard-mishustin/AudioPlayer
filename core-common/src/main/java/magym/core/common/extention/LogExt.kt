package magym.core.common.extention

import android.util.Log

private const val TAG = "myTag"

fun String?.log() = Log.d(TAG, this)

fun Any?.log() = this.toString().log()

fun Exception.log() = Log.e(TAG, "", this)

fun Throwable.log() = Log.e(TAG, "", this)