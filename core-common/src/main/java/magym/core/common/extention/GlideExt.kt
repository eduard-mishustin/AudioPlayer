package magym.core.common.extention

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

fun Context.loadBitmap(url: String, callback: (Bitmap) -> Unit) {
	Glide.with(this)
		.asBitmap()
		.load(url)
		.into(object : CustomTarget<Bitmap>() {
			override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
				callback.invoke(resource)
			}
			
			override fun onLoadCleared(placeholder: Drawable?) {}
		})
}