package co.tiagoaguiar.atway.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class ATActivity<T: ViewBinding>(
  private val bindable: (LayoutInflater) -> T
) : AppCompatActivity() {

  lateinit var binding: T

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = bindable(layoutInflater)

    setContentView(binding.root)

    setupViews()
  }

  abstract fun setupViews()

}