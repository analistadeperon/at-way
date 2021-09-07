package co.tiagoaguiar.atway.ui

import android.content.Intent
import android.util.SparseArray
import android.view.MenuItem
import androidx.core.util.forEach
import androidx.core.util.set
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.navigation.NavigationView

/**
 * Manages the various graphs needed for a [NavigationView].
 *
 * This sample is a workaround until the Navigation Component supports multiple back stacks.
 */

fun NavigationView.setupWithNavController(
  navGraphIds: List<Int>,
  fragmentManager: FragmentManager,
  containerId: Int,
  intent: Intent
): LiveData<Pair<NavController?, Int>> {

  // Map of tags
  val graphIdToTagMap = SparseArray<String>()

  // Result. Mutable live data with the selected controlled
  val selectedNavController = MutableLiveData<Pair<NavController?, Int>>()

  var firstFragmentGraphId = 0

  // First create a NavHostFragment for each NavGraph ID
  navGraphIds.forEachIndexed { index, navGraphId ->
    val fragmentTag = getFragmentTag(index)

    // Find or create the Navigation host fragment
    val navHostFragment =
      obtainNavHostFragment(
        fragmentManager,
        fragmentTag,
        navGraphId,
        containerId
      )

    // Obtain its id
    val graphId = navHostFragment.navController.graph.id

    if (index == 0) {
      firstFragmentGraphId = graphId
    }

    // Save to the map
    graphIdToTagMap[graphId] = fragmentTag

    // Attach or detach nav host fragment depending on whether it's the selected item.
    if (checkedItem?.itemId == graphId) {
      // Update live data with the selected graph
      selectedNavController.value = Pair(navHostFragment.navController, graphId)
      attachNavHostFragment(
        fragmentManager,
        navHostFragment,
        index == 0
      )
    } else {
      detachNavHostFragment(
        fragmentManager,
        navHostFragment
      )
    }
  }

  // Now connect selecting an item with swapping Fragments
  var selectedItemTag = graphIdToTagMap[checkedItem?.itemId ?: 0]
  val firstFragmentTag = graphIdToTagMap[firstFragmentGraphId]
  var isOnFirstFragment = selectedItemTag == firstFragmentTag

  // When a navigation item is selected
  setNavigationItemSelectedListener { item: MenuItem ->
    // Don't do anything if the state is state has already been saved.
    if (fragmentManager.isStateSaved) {
      false
    } else {
      val newlySelectedItemTag = graphIdToTagMap[item.itemId]
      if (newlySelectedItemTag != null && selectedItemTag != newlySelectedItemTag) {
        // Pop everything above the first fragment (the "fixed start destination")
        fragmentManager.popBackStack(
          firstFragmentTag,
          FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
        val selectedFragment =
          fragmentManager.findFragmentByTag(newlySelectedItemTag) as? NavHostFragment

        if (selectedFragment != null) {
          // Exclude the first fragment tag because it's always in the back stack.
          if (firstFragmentTag != newlySelectedItemTag) {
            // Commit a transaction that cleans the back stack and adds the first fragment
            // to it, creating the fixed started destination.
            fragmentManager.beginTransaction()
              .setCustomAnimations(
                R.anim.nav_default_enter_anim,
                R.anim.nav_default_exit_anim,
                R.anim.nav_default_pop_enter_anim,
                R.anim.nav_default_pop_exit_anim
              )
              .attach(selectedFragment)
              .setPrimaryNavigationFragment(selectedFragment)
              .apply {
                // Detach all other Fragments
                graphIdToTagMap.forEach { _, fragmentTagIter ->
                  if (fragmentTagIter != newlySelectedItemTag) {
                    detach(fragmentManager.findFragmentByTag(firstFragmentTag)!!)
                  }
                }
              }
              .addToBackStack(firstFragmentTag)
              .setReorderingAllowed(true)
              .commit()
          }
        } else {
          // delegate when some app need to handle navigation view without host fragment
          selectedNavController.value = Pair(null, item.itemId)
          return@setNavigationItemSelectedListener true
        }

        selectedItemTag = newlySelectedItemTag
        isOnFirstFragment = selectedItemTag == firstFragmentTag
        selectedNavController.value = Pair(selectedFragment.navController, item.itemId)
        true
      } else {
        val selectedFragment =
          fragmentManager.findFragmentByTag(newlySelectedItemTag) as? NavHostFragment
        selectedNavController.value = Pair(selectedFragment?.navController, item.itemId)
        false
      }
    }
  }

  // Handle deep link
  setupDeepLinks(navGraphIds, fragmentManager, containerId, intent)

  // Finally, ensure that we update our BottomNavigationView when the back stack changes
  fragmentManager.addOnBackStackChangedListener {
    if (!isOnFirstFragment && !fragmentManager.isOnBackStack(firstFragmentTag)) {
      this.tag = (firstFragmentGraphId)
    }

    // Reset the graph if the currentDestination is not valid (happens when the back
    // stack is popped after using the back button).
    selectedNavController.value?.let { controller ->
      controller.first?.let {
        if (it.currentDestination == null) {
          it.navigate(it.graph.id)
        }
      }

    }
  }
  return selectedNavController

}


private fun NavigationView.setupDeepLinks(
  navGraphIds: List<Int>,
  fragmentManager: FragmentManager,
  containerId: Int,
  intent: Intent
) {

  navGraphIds.forEachIndexed { index, navGraphId ->
    val fragmentTag = getFragmentTag(index)

    // Find or create the Navigation host fragment
    val navHostFragment = obtainNavHostFragment(
      fragmentManager,
      fragmentTag,
      navGraphId,
      containerId
    )
    // Handle Intent
    if (navHostFragment.navController.handleDeepLink(intent)
      && tag != navHostFragment.navController.graph.id
    ) {
      this.tag = (navHostFragment.navController.graph.id)
    }
  }
}

private fun detachNavHostFragment(
  fragmentManager: FragmentManager,
  navHostFragment: NavHostFragment
) {
  fragmentManager.beginTransaction()
    .detach(navHostFragment)
    .commitNow()
}

private fun attachNavHostFragment(
  fragmentManager: FragmentManager,
  navHostFragment: NavHostFragment,
  isPrimaryNavFragment: Boolean
) {
  fragmentManager.beginTransaction()
    .attach(navHostFragment)
    .apply {
      if (isPrimaryNavFragment) {
        setPrimaryNavigationFragment(navHostFragment)
      }
    }
    .commitNow()
}

private fun obtainNavHostFragment(
  fragmentManager: FragmentManager,
  fragmentTag: String,
  navGraphId: Int,
  containerId: Int
): NavHostFragment {
  // If the Nav Host fragment exists, return it
  val existingFragment = fragmentManager.findFragmentByTag(fragmentTag) as NavHostFragment?
  existingFragment?.let { return it }

  // Otherwise, create it and return it.
  val navHostFragment = NavHostFragment.create(navGraphId)
  fragmentManager.beginTransaction()
    .add(containerId, navHostFragment, fragmentTag)
    .commitNow()
  return navHostFragment
}

private fun FragmentManager.isOnBackStack(backStackName: String): Boolean {
  val backStackCount = backStackEntryCount
  for (index in 0 until backStackCount) {
    if (getBackStackEntryAt(index).name == backStackName) {
      return true
    }
  }
  return false
}

private fun getFragmentTag(index: Int) = "navigation#$index"