package magym.core.common.extention

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.widget.SearchView

fun EditText.onTextChanged(onTextChanged: (String) -> Unit) {
	addTextChangedListener(object : TextWatcher {
		override fun afterTextChanged(s: Editable) {}
		
		override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
		
		override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
			onTextChanged.invoke(text.toString())
		}
	})
}

// У SearchView отсутствует addTextChangedListener, только setOnQueryTextListener, поэтому пересоздавать этот слушатель нельзя
fun SearchView.onTextChanged(onTextChanged: (String) -> Unit) {
	setOnQueryTextListener(object : SearchView.OnQueryTextListener {
		override fun onQueryTextSubmit(query: String?) = false
		
		override fun onQueryTextChange(newText: String?): Boolean {
			onTextChanged.invoke(newText.toString())
			return false
		}
	})
}