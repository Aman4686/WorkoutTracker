package com.example.workout.screens.details

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.theme.WorkoutTracerTheme
import com.example.feature.R
import com.example.workout.screens.details.state.SetUIModel
import com.example.workout.screens.details.state.WorkoutDetailsUIAction
import com.example.workout.screens.details.state.WorkoutDetailsUIState
import com.example.workout.screens.details.state.WorkoutFlatListItem

@Composable
private fun rememberWorkoutDetailsViewModel(workoutId: Int): WorkoutDetailsViewModel =
    hiltViewModel<WorkoutDetailsViewModel, WorkoutDetailsViewModel.Factory>(
        creationCallback = { factory: WorkoutDetailsViewModel.Factory ->
            factory.create(workoutId)
        }
    )

private const val TAG = "WorkoutDetailsScreen"

@Composable
fun WorkoutDetailsScreen(
    workoutId: Int,
    onBack: () -> Unit = {},
    onAddExerciseClick: () -> Unit = {},
    viewModel: WorkoutDetailsViewModel = rememberWorkoutDetailsViewModel(workoutId),
) {

    val uiState = viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect {
            Log.d(TAG, "onBack")
            onBack()

        }
    }

    WorkoutDetailsView(
        uiState.value,
        onAddExerciseClick = {
            onAddExerciseClick.invoke()
        },
        onAddSetClick = { exerciseId, count ->
            viewModel.onAction(
                WorkoutDetailsUIAction.AddSet(
                    exerciseId = exerciseId,
                    count = count
                )
            )
        },
        onUpdateSet = { id, set ->
            viewModel.onAction(WorkoutDetailsUIAction.UpdateSet(id, set))
        },
        onDeleteWorkoutClick = { ->
            viewModel.onAction(WorkoutDetailsUIAction.DeleteWorkout)
        })
}

@Composable
fun WorkoutDetailsView(
    uiState: WorkoutDetailsUIState,
    onAddExerciseClick: () -> Unit = {},
    onAddSetClick: (exerciseId: Int, count: Int) -> Unit = { _, _ -> },
    onDeleteWorkoutClick: () -> Unit = {},
    onUpdateSet: (exerciseId: Int, set: SetUIModel) -> Unit = { _, _ -> },
) {
    Column(
        modifier = Modifier
            .padding(6.dp)
    ) {
        // in LazyColumn
        LazyColumn {
            items(
                uiState.exerciseFlatList,
                key = { item ->
                    when (item) {
                        is WorkoutFlatListItem.ExerciseHeader -> "header_${item.exercise.id}"
                        is WorkoutFlatListItem.ExerciseSet -> "set_${item.exerciseId}_${item.set.id}"
                        is WorkoutFlatListItem.AddSetButton -> "add_${item.exerciseId}_${item.count}"

                    }
                },
                contentType = { item ->
                    when (item) {
                        is WorkoutFlatListItem.ExerciseHeader -> 0
                        is WorkoutFlatListItem.ExerciseSet -> 1
                        is WorkoutFlatListItem.AddSetButton -> 2
                    }
                }
            ) { item ->
                when (item) {
                    is WorkoutFlatListItem.ExerciseHeader -> ExerciseHeader(item.exercise.type.name)
                    is WorkoutFlatListItem.ExerciseSet -> {
                        val onWeightChanged = remember(item.set.id) {
                            { text: String ->
                                onUpdateSet(
                                    item.exerciseId,
                                    item.set.copy(weight = text)
                                )
                            }
                        }

                        val onRepsChanged = remember(item.set.id) {
                            { text: String ->
                                onUpdateSet(
                                    item.exerciseId,
                                    item.set.copy(reps = text)
                                )
                            }
                        }

                        SetItem(
                            set = item.set,
                            onWeightChanged = onWeightChanged,
                            onRepsChanged = onRepsChanged
                        )
                    }

                    is WorkoutFlatListItem.AddSetButton -> {

                        val onAddSet = remember(item.exerciseId, item.count) { {
                                onAddSetClick(item.exerciseId, item.count)
                            } }

                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = onAddSet
                        ) {
                            Text("Add set")
                        }
                    }
                }
            }

            item {
                WorkoutBottomButtons(
                    onAddExerciseClick = onAddExerciseClick,
                    onDeleteWorkoutClick = onDeleteWorkoutClick
                )
            }
        }
    }
}

@Composable
fun WorkoutBottomButtons(
    onAddExerciseClick: () -> Unit = {},
    onDeleteWorkoutClick: () -> Unit = {},
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Button(
            modifier = Modifier.weight(1f),
            onClick = {
                onAddExerciseClick.invoke()
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Text(
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.inverseSurface,
                text = "Add exercise"
            )
        }
        Spacer(modifier = Modifier.padding(horizontal = 6.dp))
        Button(
            modifier = Modifier.weight(1f),
            onClick = {
                onDeleteWorkoutClick.invoke()
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Text(
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.inverseSurface,
                text = "Delete"
            )
        }
    }
}

@Composable
fun ExerciseHeader(exerciseName: String) {
    Column {
        val textFieldDefaults =
            TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )

        TextField(
            value = exerciseName,
            onValueChange = {

            },
            colors = textFieldDefaults,
            textStyle = MaterialTheme.typography.titleLarge
        )
        Spacer(Modifier.padding(vertical = 6.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            val labelColor = MaterialTheme.colorScheme.onSurfaceVariant
            Text(text = stringResource(R.string.set), color = labelColor)
            Text(text = stringResource(R.string.kg), color = labelColor)
            Text(text = stringResource(R.string.reps), color = labelColor)
            Text(text = stringResource(R.string.check), color = labelColor)
        }
    }
}

@Composable
fun SetItem(
    set: SetUIModel,
    onRepsChanged: (String) -> Unit = {},
    onWeightChanged: (String) -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween

    ) {

        Text(text = set.count.toString(), style = MaterialTheme.typography.titleLarge)

        WorkoutSetsTextField(
            modifier = Modifier.width(width = 100.dp),
            value = set.weight,
            onValueChange = { text ->
                if (text.length <= 5) {
                    onWeightChanged.invoke(text)
                }
            },
        )

        WorkoutSetsTextField(
            modifier = Modifier.width(width = 100.dp),
            value = set.reps,
            onValueChange = { text ->
                if (text.length <= 5) {
                    onRepsChanged.invoke(text)
                }
            },
        )

        val isCheck = remember { mutableStateOf(false) }
        Checkbox(
            checked = isCheck.value,
            onCheckedChange = {
                isCheck.value = it
            })
    }
}

@Composable
fun WorkoutSetsTextField(
    modifier: Modifier,
    value: String,
    onValueChange: (String) -> Unit,
) {

    val textFieldDefaults = TextFieldDefaults.colors(
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        disabledContainerColor = Color.Transparent,
        errorContainerColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent
    )

    TextField(
        modifier = modifier,
        value = value,
        singleLine = true,
        onValueChange = onValueChange,
        colors = textFieldDefaults,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),
        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
    )
}

//@Preview(
//    showBackground = true,
//    uiMode = Configuration.UI_MODE_NIGHT_YES,
//    device = Devices.PIXEL_8_PRO,
//    apiLevel = 31,
//    name = "Default Preview Dark"
//)
//@Composable
//fun WorkoutDetailsScreenPreviewDark() {
//    WorkoutTracerTheme(){
//        Surface {
//            WorkoutDetailsView()
//        }
//    }
//}

//@Preview(
//    showBackground = true,
//    uiMode = Configuration.UI_MODE_NIGHT_YES,
//    device = Devices.PIXEL_8_PRO,
//    apiLevel = 31,
//    name = "Default Preview Dark"
//)
//@Composable
//fun WorkoutDetailItemPreview() {
//    WorkoutTracerTheme() {
//        Surface {
//            WorkoutDetailItem()
//        }
//    }
//}
//
//@Preview(
//    showBackground = true,
//    uiMode = Configuration.UI_MODE_NIGHT_YES,
//    device = Devices.PIXEL_8_PRO,
//    apiLevel = 31,
//    name = "Default Preview Dark"
//)
//@Composable
//fun WorkoutSetItemPreview() {
//    WorkoutTracerTheme() {
//        Surface {
//            WorkoutSetItem()
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
fun WorkoutDetailsViewPreview() {
    WorkoutTracerTheme() {
        Surface {
            WorkoutDetailsView(uiState = WorkoutDetailsUIState.preview())
        }
    }
}