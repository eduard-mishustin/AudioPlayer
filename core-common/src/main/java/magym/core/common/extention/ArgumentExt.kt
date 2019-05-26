package magym.core.common.extention

import android.os.Parcelable
import androidx.fragment.app.Fragment


fun <T : Parcelable> Fragment.argumentParcelable(key: String) = lazy { arguments?.getParcelable<T>(key) }
fun <T : Parcelable> Fragment.argumentParcelableArrayList(key: String) = lazy { arguments?.getParcelableArrayList<T>(key) }
fun Fragment.argumentBoolean(key: String) = lazy { arguments?.getBoolean(key) ?: false }
fun Fragment.argumentInt(key: String) = lazy { arguments?.getInt(key) ?: 0 }
fun Fragment.argumentLong(key: String) = lazy { arguments?.getLong(key) ?: 0L }
fun Fragment.argumentShort(key: String) = lazy { arguments?.getShort(key) ?: 0 }
fun Fragment.argumentChar(key: String) = lazy { arguments?.getChar(key) ?: ' ' }
fun Fragment.argumentByte(key: String) = lazy { arguments?.getByte(key) ?: 0 }
fun Fragment.argumentFloat(key: String) = lazy { arguments?.getFloat(key) ?: 0f }
fun Fragment.argumentDouble(key: String) = lazy { arguments?.getDouble(key) ?: 0.toDouble() }
fun Fragment.argumentCharSequence(key: String) = lazy { arguments?.getCharSequence(key) }
fun Fragment.argumentString(key: String) = lazy { arguments?.getString(key) ?: "" }

fun Fragment.argumentIsExist(key: String) = arguments?.containsKey(key) ?: false
fun Fragment.argumentIsNotExist(key: String) = !argumentIsExist(key)