package co.tiagoaguiar.atway.ui

import android.widget.EditText
import co.tiagoaguiar.atway.ui.util.Mask

fun EditText.phoneMask() {
  this.addTextChangedListener(Mask.mask("(##) ####-####", this))
}

fun EditText.cpfMask() {
  val maskCPF = "###.###.###-##"
  this.addTextChangedListener(Mask.mask(maskCPF, this))
}