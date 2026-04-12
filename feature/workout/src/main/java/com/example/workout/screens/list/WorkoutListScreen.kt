package com.example.workout.screens.list

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.rememberNavController
import com.example.core.theme.WorkoutTracerTheme
import com.example.domain.model.Workout
import com.example.workout.navigation.Route
import com.example.workout.screens.list.state.WorkoutListSideEffect
import com.example.workout.screens.list.state.WorkoutListUIAction
import com.example.workout.screens.list.state.WorkoutListUIState
import ui.components.FullScreenLoading

@Composable
fun WorkoutListScreen(
    viewModel: WorkoutListViewModel = hiltViewModel(),
    navigateToWorkoutDetails: (id: Int) -> Unit = {},
) {
    val uiState = viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is WorkoutListSideEffect.NavigateToWorkoutDetails -> navigateToWorkoutDetails(effect.workoutId)
            }
        }
    }

    if(uiState.value.isLoading){
        FullScreenLoading()
    }else {
        WorkoutListScreenView(
            uiState = uiState.value,
            navigateToWorkoutDetails = navigateToWorkoutDetails,
            addNewWorkout = {
                viewModel.onAction(WorkoutListUIAction.AddNewWorkout)
            }
        )
    }
}

@Composable
fun WorkoutListScreenView(
    uiState: WorkoutListUIState,
    navigateToWorkoutDetails: (id: Int) -> Unit = {},
    addNewWorkout: () -> Unit= {}
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            LazyColumn {
                items(uiState.workoutsList, key = { it.id }) { workout ->
                    WorkoutListItem(
                        workoutId = workout.id,
                        workoutDate = workout.date,
                        onWorkoutClick = navigateToWorkoutDetails
                    )
                }
            }
        }

        AddFloatingButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            onClick = {
                addNewWorkout.invoke()
            }
        )
    }
}

@Composable
fun AddFloatingButton(modifier: Modifier, onClick: () -> Unit = {}) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        shape = ShapeDefaults.Medium
    ) {
        Icon(Icons.Filled.Add, "Floating action button.")
    }
}

@Composable
fun WorkoutListItem(
    workoutDate: String,
    workoutId: Int,
    onWorkoutClick: (id: Int) -> Unit = {},
) {
    Row(
        modifier = Modifier
            .padding(6.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .fillMaxWidth()
            .clickable {
                onWorkoutClick(workoutId)
            }
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(workoutDate, fontSize = 24.sp)
    }
}

//@Preview(
//    showBackground = true,
//    uiMode = Configuration.UI_MODE_NIGHT_NO,
//    device = Devices.PIXEL_8_PRO,
//    apiLevel = 31,
//    name = "Default Preview Light"
//)
//@Composable
//fun WorkoutListScreenPreviewLight() {
//    WorkoutTracerTheme {
//        Surface {
//            WorkoutListScreenView()
//        }
//    }
//}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = Devices.PIXEL_8_PRO,
    apiLevel = 31,
    name = "Default Preview Dark"
)
@Composable
fun WorkoutListScreenPreviewDark() {
    WorkoutTracerTheme {
        Surface {
            WorkoutListScreenView(uiState = WorkoutListUIState.preview())
        }
    }
}