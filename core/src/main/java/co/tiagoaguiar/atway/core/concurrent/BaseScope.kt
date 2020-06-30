package co.tiagoaguiar.atway.core.concurrent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

abstract class BaseScope : CoroutineScope {

  private var viewModelJob: Job = SupervisorJob()

  override val coroutineContext: CoroutineContext
    get() = viewModelJob + handler + Dispatchers.Main


  private val connectionState = MutableLiveData<Result<Throwable>>()
  val connectionErrorData: LiveData<Result<Throwable>> get() = connectionState

//  override fun onCleared() {
//    super.onCleared()
//    viewModelJob.cancel()
//  }

  protected val handler = CoroutineExceptionHandler { _, throwable ->
//    if (throwable is NoConnectivityException)
//      connectionState.value = Result.Error(throwable)

    handlerError(throwable)
  }

  protected fun startCoroutineJob() {
    viewModelJob = SupervisorJob()
  }

  protected fun isStackedTrace(throwable: Throwable, methodName: String): Boolean {
    return throwable.stackTrace.firstOrNull { it.methodName == methodName } != null
  }

  abstract fun handlerError(throwable: Throwable)

}