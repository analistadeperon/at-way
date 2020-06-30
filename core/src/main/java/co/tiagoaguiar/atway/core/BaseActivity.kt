package co.tiagoaguiar.atway.core

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import co.tiagoaguiar.atway.core.di.PresenterFactory
import dagger.android.AndroidInjection
import java.lang.reflect.ParameterizedType
import javax.inject.Inject

abstract class BaseActivity<T : Presenter> : AppCompatActivity() {

  @Inject
  lateinit var presenterFactory: PresenterFactory

  protected val presenter: T by lazy {
    val tClass = getTClass()
     presenterFactory.create(tClass).apply {
       bindPresenter(this)
     }
  }

  private fun getTClass(): Class<T> {
    return (javaClass.genericSuperclass as ParameterizedType)
      .actualTypeArguments[0] as Class<T>
  }

  private fun <V> getVClass(clazz: Class<V>): V {
    return javaClass.asSubclass(clazz) as V
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    AndroidInjection.inject(this)

    setContentView(getLayout())

    setupViews()
  }

  abstract fun setupViews()

  abstract fun bindPresenter(presenter: T)

  @LayoutRes
  abstract fun getLayout(): Int

}