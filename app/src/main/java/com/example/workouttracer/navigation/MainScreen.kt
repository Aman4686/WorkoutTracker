package com.example.workouttracer.navigation

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.workout.navigation.Route
import com.example.workout.screens.details.WorkoutDetailsScreen
import com.example.workout.screens.list.WorkoutListScreen
import com.example.workouttracer.TAG

@Composable
fun MainScreen(modifier : Modifier) {
    val backStack = rememberNavBackStack(Route.WorkoutList)
    NavDisplay(
        modifier = modifier
            .fillMaxSize(),
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<Route.WorkoutList> {
                WorkoutListScreen(navigateToWorkoutDetails = {
                    backStack.add(Route.WorkoutDetails(it))
                })
            }
            entry<Route.WorkoutDetails> { entry ->
                Log.d(TAG, "WorkoutDetailsScreen: ${entry.workoutId}")
                WorkoutDetailsScreen(
                    workoutId = entry.workoutId,
                    onBack = { backStack.removeLastOrNull() }
                )
            }
        }
    )
}