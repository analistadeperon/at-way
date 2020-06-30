package co.tiagoaguiar.atway.core

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasFragmentInjector
import dagger.android.support.DaggerAppCompatActivity
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

abstract class BaseFragmentActivity : DaggerAppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContentView(getLayout())

    if (savedInstanceState == null) {
      supportFragmentManager.beginTransaction()
        .replace(R.id.container, setupFragment())
        .commitNow()
    }
  }

  abstract fun setupFragment() : Fragment

  @LayoutRes
  abstract fun getLayout(): Int

}