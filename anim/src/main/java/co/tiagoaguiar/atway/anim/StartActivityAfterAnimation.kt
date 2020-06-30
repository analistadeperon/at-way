package co.tiagoaguiar.atway.anim

import android.content.Context
import android.content.Intent
import android.view.animation.Animation
import java.lang.reflect.InvocationTargetException

class StartActivityAfterAnimation(private val context: Context, private val intent: Intent) :
  AfterAnimationCallback() {

  override fun onAnimationEnd(animation: Animation?) {
    context.startActivity(intent)
    if (UIConstants.overridePendingTransitionMethod != null) {
      try {
        UIConstants.overridePendingTransitionMethod?.invoke(
          context,
          R.anim.anim_activity_fade_in,
          R.anim.anim_activity_fade_out
        )
      } catch (e: InvocationTargetException) {
        // TODO: debug Class
      } catch (ex: IllegalAccessException) {
        // TODO: debug Class
      }
    }
  }

}
