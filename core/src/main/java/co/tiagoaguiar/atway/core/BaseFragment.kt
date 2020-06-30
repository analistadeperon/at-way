package co.tiagoaguiar.atway.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import co.tiagoaguiar.atway.core.di.PresenterFactory
import dagger.android.AndroidInjection
import dagger.android.support.AndroidSupportInjection
import java.lang.reflect.ParameterizedType
import javax.inject.Inject

abstract class BaseFragment<T : Presenter> : Fragment() {

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

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    AndroidSupportInjection.inject(this)

  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(getLayout(), container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    setupViews()
  }

  abstract fun setupViews()

  abstract fun bindPresenter(presenter: T)

  @LayoutRes
  abstract fun getLayout(): Int

}