package co.tiagoaguiar.atway.ui

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

fun Fragment.snack(message: String?, listener: ((View) -> Unit)? = null) {
  activity?.let { act ->
    val snack = Snackbar.make(
      act.findViewById(android.R.id.content),
      message ?: "",
      if (listener == null) Snackbar.LENGTH_LONG else Snackbar.LENGTH_INDEFINITE
    )
    snack.setAction("Ok", listener)
    snack.show()
  }
}

fun Fragment.snack(@StringRes message: Int, listener: ((View) -> Unit)? = null) {
  activity?.let { act ->
    val snack = Snackbar.make(
      act.findViewById(android.R.id.content),
      getString(message),
      if (listener == null) Snackbar.LENGTH_LONG else Snackbar.LENGTH_INDEFINITE
    )
    snack.setAction("Ok", listener)
    snack.show()
  }
}

fun Fragment.hideSoftKeyboard() {
  val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
  var view = activity?.currentFocus
  if (view == null) {
    view = View(activity)
  }
  imm!!.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Fragment.showSoftKeyboard(editText: EditText) {
  val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
  var view = activity?.currentFocus
  if (view == null) {
    view = View(activity)
  }
  imm?.toggleSoftInputFromWindow(view.windowToken, InputMethodManager.SHOW_FORCED, 0);
}
