package com.example.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.workout.screens.details.WorkoutDetailsScreen
import com.example.workout.screens.list.WorkoutListScreen

@Composable
fun MainDisplay(modifier : Modifier) {
    val backStack = rememberNavBackStack(Screen.WorkoutList)
    NavDisplay(
        modifier = modifier
            .fillMaxSize(),
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<Screen.WorkoutList> {
                WorkoutListScreen(navigateToWorkoutDetails = {
                    backStack.add(Screen.WorkoutDetails)
                })
            }
            entry<Screen.WorkoutDetails> {
                WorkoutDetailsScreen()
            }
        }
    )
}