package co.tiagoaguiar.atway.ui.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class Mask {

  companion object {
    private fun replaceChars(full: String): String {
      return full.replace(".", "").replace("-", "")
        .replace("(", "").replace(")", "")
        .replace("/", "").replace(" ", "")
        .replace("*", "")
    }

    fun mask(mask: String, et: EditText): TextWatcher {

      return object : TextWatcher {
        var isUpdating: Boolean = false
        var oldString: String = ""
        var _mask = mask
        override fun beforeTextChanged(
          charSequence: CharSequence,
          i: Int,
          i1: Int,
          i2: Int
        ) {
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
          val str = replaceChars(s.toString())
          var cpfWithMask = ""

          if (_mask == "(##) ####-####") {
            if (start >= 14 && s[5] == '9') {
              _mask = "(##) #####-####"
            }
          }

          if (count == 0) {//is deleting
            isUpdating = true
            if (_mask == "(##) #####-####" && s.length == 14) {
              _mask = "(##) ####-####"
            }
          }

          if (isUpdating) {
            oldString = str
            isUpdating = false
            return
          }

          var i = 0
          for (m: Char in _mask.toCharArray()) {
            if (m != '#' && str.length > oldString.length) {
              cpfWithMask += m
              continue
            }
            try {
              cpfWithMask += str.get(i)
            } catch (e: Exception) {
              break
            }
            i++
          }

          isUpdating = true

          if (cpfWithMask == "(0") {
            et.setText("")
            et.setSelection(0)
            return
          }

          et.setText(cpfWithMask)
          et.setSelection(cpfWithMask.length)
        }

        override fun afterTextChanged(editable: Editable) {
        }
      }
    }
  }

}