package com.example.workout.screens.exersices

import android.content.res.Configuration
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.theme.WorkoutTracerTheme
import com.example.workout.screens.exersices.state.ExerciseListUIAction
import com.example.workout.screens.exercises.state.ExerciseListUIState
import com.example.workout.screens.exercises.state.ExerciseTypeUIModel
import com.example.workout.screens.exersices.state.ExerciseListSideEffect
import kotlinx.collections.immutable.ImmutableList

@Composable
private fun rememberExerciseListViewModel(workoutId: Int): ExerciseListViewModel =
    hiltViewModel<ExerciseListViewModel, ExerciseListViewModel.Factory>(
        creationCallback = { factory: ExerciseListViewModel.Factory ->
            factory.create(workoutId)
        }
    )

@Composable
fun ExerciseListScreen(
    workoutId: Int,
    viewModel: ExerciseListViewModel = rememberExerciseListViewModel(workoutId),
    navigateBack: () -> Unit = {},
) {

    val uiState = viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ExerciseListSideEffect.NavigateBack -> navigateBack()
                is ExerciseListSideEffect.ShowToast -> Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    ExerciseListScreenView(
        uiState = uiState.value,
        onSelectExercise = {
            viewModel.onAction(ExerciseListUIAction.SelectExercise(it))
        },
        onAddNewExerciseType = {
            viewModel.onAction(ExerciseListUIAction.AddNewExerciseType(it))
        },
        onSaveExerciseToWorkout = {
            viewModel.onAction(ExerciseListUIAction.SaveExerciseToWorkout)
        },
        onDeleteWorkoutType = {
            viewModel.onAction(ExerciseListUIAction.DeleteExerciseType(it))
        })
}

@Composable
fun ExerciseListScreenView(
    uiState: ExerciseListUIState,
    onSelectExercise: (Int) -> Unit = {},
    onAddNewExerciseType: (String) -> Unit = {},
    onSaveExerciseToWorkout: () -> Unit = {},
    onDeleteWorkoutType: (Int) -> Unit = {},
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        ExerciseList(
            exerciseTypeList = uiState.exerciseList,
            onSelectExercise,
            onAddNewExerciseType,
            onDeleteWorkoutType
        )

        Button(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            onClick = onSaveExerciseToWorkout

        ) {
            Text("Add exercise")
        }
    }

}

@Composable
fun ExerciseList(
    exerciseTypeList: ImmutableList<ExerciseTypeUIModel>,
    onSelectExercise: (Int) -> Unit = {},
    onAddExercise: (String) -> Unit = {},
    onDeleteWorkoutType: (Int) -> Unit = {},
) {
    LazyColumn {
        items(exerciseTypeList, key = { it.id }) { item ->
            ExerciseItem(
                modifier = Modifier.fillMaxWidth(),
                name = item.name,
                isSelected = item.isSelected,
                onSelectExercise = {
                    onSelectExercise.invoke(item.id)
                },
                onDeleteWorkoutType = {
                    onDeleteWorkoutType.invoke(item.id)
                }

            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(
                        MaterialTheme.colorScheme.primary
                    )
            )
        }

        item {
            ExerciseEditableItem(
                modifier = Modifier.padding(vertical = 12.dp),
                onAddExercise = onAddExercise
            )
        }
    }

}

@Composable
fun ExerciseItem(
    modifier: Modifier,
    name: String,
    isSelected: Boolean,
    onSelectExercise: () -> Unit,
    onDeleteWorkoutType: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .clickable(onClick = onSelectExercise)
            .height(60.dp),
    ) {
        Row(modifier = Modifier.align(Alignment.CenterStart)) {
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .fillMaxHeight()
                        .width(10.dp)
                        .background(
                            MaterialTheme.colorScheme.primary
                        )
                )
            }
            Text(text = name, fontSize = 24.sp)
        }
        Button(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .align(Alignment.CenterEnd),
            onClick = onDeleteWorkoutType) {
            Text("delete")
        }
    }
}

@Composable
fun ExerciseEditableItem(modifier: Modifier, onAddExercise: (String) -> Unit = {}) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val text = remember { mutableStateOf("") }

        TextField(
            modifier = modifier,
            value = text.value,
            singleLine = true,
            onValueChange = {
                text.value = it
            },
        )
        Spacer(modifier = Modifier.padding(horizontal = 12.dp))
        Button(onClick = {
            onAddExercise.invoke(text.value)
        }) {
            Text("+")
        }
    }
}

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
            ExerciseListScreenView(uiState = ExerciseListUIState.preview())
        }
    }
}