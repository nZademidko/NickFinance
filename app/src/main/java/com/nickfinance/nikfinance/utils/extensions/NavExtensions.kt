package com.nickfinance.nikfinance.utils.extensions

import android.content.Intent
import android.util.SparseArray
import androidx.core.util.forEach
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

fun BottomNavigationView.setupWithNavController(
    navGraphIds: List<Int>,
    fragmentManager: FragmentManager,
    containerId: Int,
    intent: Intent,
    indexSelected: Int
): LiveData<NavController> {
    val graphIdToTagMap = SparseArray<String>()
    val selectedNavController = MutableLiveData<NavController>()

    var firstFragmentGraphId = 0
    navGraphIds.forEachIndexed { index, navGraphId ->
        val fragmentTag = getFragmentTag(index)
        val navHostFragment = obtainNavHostFragment(
            fragmentManager,
            fragmentTag,
            navGraphId,
            containerId
        )
        val graphId = navHostFragment.navController.graph.id

        if (index == indexSelected) {
            firstFragmentGraphId = graphId
        }

        graphIdToTagMap.put(graphId, fragmentTag)

        if (this.selectedItemId == graphId) {
            selectedNavController.value = navHostFragment.navController
            attachNavHostFragment(fragmentManager, navHostFragment, index == indexSelected)
        } else {
            detachNavHostFragment(fragmentManager, navHostFragment)
        }
    }

    var selectedItemTag = graphIdToTagMap[this.selectedItemId]
    val firstFragmentTag = graphIdToTagMap[firstFragmentGraphId]
    var isOnFirstFragment = selectedItemTag == firstFragmentTag

    setOnItemReselectedListener { item ->
        val tag = graphIdToTagMap[item.itemId]
        val targetFragment = fragmentManager.findFragmentByTag(tag) as NavHostFragment
        selectedNavController.value?.popBackStack(
            destinationId = targetFragment.navController.graph.startDestinationId,
            inclusive = false
        )
    }

    setOnItemSelectedListener { item ->
        if (fragmentManager.isStateSaved) {
            false
        } else {
            val newlySelectedItemTag = graphIdToTagMap[item.itemId]
            if (selectedItemTag == newlySelectedItemTag) {
                false
            } else {
                fragmentManager.popBackStack(
                    firstFragmentTag,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                )
                val selectedFragment = fragmentManager.findFragmentByTag(newlySelectedItemTag)
                        as NavHostFragment

                if (firstFragmentTag != newlySelectedItemTag) {
                    fragmentManager.beginTransaction()
                        .attach(selectedFragment)
                        .setPrimaryNavigationFragment(selectedFragment)
                        .apply {
                            graphIdToTagMap.forEach { _, fragmentTagItem ->
                                if (fragmentTagItem != newlySelectedItemTag) {
                                    detach(fragmentManager.findFragmentByTag(firstFragmentTag)!!)
                                }
                            }
                        }
                        .addToBackStack(firstFragmentTag)
                        .setReorderingAllowed(true)
                        .commit()
                }
                selectedItemTag = newlySelectedItemTag
                isOnFirstFragment = selectedItemTag == firstFragmentTag
                selectedNavController.value = selectedFragment.navController
                true
            }
        }
    }

    setupDeepLinks(navGraphIds, fragmentManager, containerId, intent)

    fragmentManager.addOnBackStackChangedListener {
        if (!isOnFirstFragment && !fragmentManager.isOnBackStack(firstFragmentTag)) {
            this.selectedItemId = firstFragmentGraphId
        }
        selectedNavController.value?.let { controller ->
            if (controller.currentDestination == null) {
                controller.navigate(controller.graph.id)
            }
        }
    }
    return selectedNavController
}

private fun BottomNavigationView.setupDeepLinks(
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
            && selectedItemId != navHostFragment.navController.graph.id
        ) {
            this.selectedItemId = navHostFragment.navController.graph.id
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

private fun getFragmentTag(index: Int) = "bottomNavigation#$index"
