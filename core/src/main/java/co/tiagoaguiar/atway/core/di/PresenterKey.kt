package co.tiagoaguiar.atway.core.di

import co.tiagoaguiar.atway.core.Presenter
import dagger.MapKey
import kotlin.reflect.KClass

@MapKey
@Target(AnnotationTarget.FUNCTION)
annotation class PresenterKey(
  val value: KClass<out Presenter>
)