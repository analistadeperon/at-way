package co.tiagoaguiar.atway.ui

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.android.material.snackbar.Snackbar


fun Activity.snack(message: String?) {
  Snackbar.make(findViewById(android.R.id.content), message ?: "", Snackbar.LENGTH_LONG).show()
}

fun Activity.snackInfinite(message: String?) {
  Snackbar.make(findViewById(android.R.id.content), message ?: "", Snackbar.LENGTH_INDEFINITE)
    .show()
}

fun Activity.hideSoftKeyboard() {
  val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
  var view = currentFocus
  if (view == null) {
    view = View(this)
  }
  imm!!.hideSoftInputFromWindow(view.windowToken, 0)
}
