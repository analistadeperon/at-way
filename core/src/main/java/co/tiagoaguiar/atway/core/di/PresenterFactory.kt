package co.tiagoaguiar.atway.core.di

import co.tiagoaguiar.atway.core.Presenter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PresenterFactory @Inject constructor(private val presenterMap: Map<Class<out Presenter>, Presenter>) {

  @Suppress("UNCHECKED_CAST")
  fun <T : Presenter?> create(modelClass: Class<T>): T {
    val creator = presenterMap[modelClass] ?: presenterMap.asIterable().firstOrNull {
      modelClass.isAssignableFrom(it.key)
    }?.value
    ?: throw IllegalArgumentException("unknown model class $modelClass")
    return try {
      creator as T
    } catch (e: Exception) {
      throw RuntimeException(e)
    }
  }

}
