package co.tiagoaguiar.atway.core.di

import co.tiagoaguiar.atway.core.Presenter
import dagger.Module
import dagger.Provides

@Module
open class PresenterModule {

  @Provides
  fun getPresenterFactory(map: Map<Class<out Presenter>, @JvmSuppressWildcards Presenter>) =
    PresenterFactory(map)

}