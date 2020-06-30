package co.tiagoaguiar.atway.anim

import android.app.Activity
import java.lang.reflect.Method


object UIConstants {
  var overridePendingTransitionMethod: Method? = null

  init {
    try {
      overridePendingTransitionMethod = Activity::class.java.getMethod(
        "overridePendingTransition",
        Int::class.javaPrimitiveType,
        Int::class.javaPrimitiveType
      )
    } catch (e: NoSuchMethodException) {
    }
  }
}