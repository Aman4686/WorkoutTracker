package com.example.workout.screens.details

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.domain.model.ExerciseType
import com.example.domain.model.Exersice
import com.example.domain.model.Set
import com.example.feature.R
import com.example.workout.screens.details.state.WorkoutDetailsUIAction
import com.example.workout.screens.details.state.WorkoutDetailsUIState

@Composable
private fun rememberWorkoutDetailsViewModel(workoutId: Int): WorkoutDetailsViewModel = hiltViewModel<WorkoutDetailsViewModel, WorkoutDetailsViewModel.Factory>(
    creationCallback = { factory: WorkoutDetailsViewModel.Factory ->
        factory.create(workoutId)
    }
)

@Composable
fun WorkoutDetailsScreen(
    workoutId: Int,
    onBack: () -> Unit = {},
    onAddExersiceClick: () -> Unit = {},
    viewModel: WorkoutDetailsViewModel= rememberWorkoutDetailsViewModel(workoutId),
) {

    val uiState = viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.navigateBack.collect { onBack() }
    }

    WorkoutDetailsView(
        uiState.value,
        onAddExersiceClick = { exersice ->
            onAddExersiceClick.invoke()
        },
        onAddSetClick = { exersiceId ->
            viewModel.onAction(WorkoutDetailsUIAction.AddSet(exersiceId = exersiceId))
        },
        onSaveWorkoutClick = {
            viewModel.onAction(WorkoutDetailsUIAction.SaveWorkout)
        },
        onUpdateSet = { id, set ->
            viewModel.onAction(WorkoutDetailsUIAction.UpdateSet(id, set))
        },
        onUpdateExersice = { exersice ->
            viewModel.onAction(WorkoutDetailsUIAction.UpdateExersice(exersice))
        },
        onDeleteWorkoutClick = {  ->
            viewModel.onAction(WorkoutDetailsUIAction.DeleteWorkout)
        })
}

@Composable
fun WorkoutDetailsView(
    uiState: WorkoutDetailsUIState,
    onAddExersiceClick: (Exersice) -> Unit = {},
    onAddSetClick: (exersiceId: Int) -> Unit = {},
    onSaveWorkoutClick: () -> Unit = {},
    onDeleteWorkoutClick: () -> Unit = {},
    onUpdateSet: (exersiceId: Int, set: Set) -> Unit = { _, _ -> },
    onUpdateExersice: (Exersice) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(6.dp)
    ) {
        LazyColumn {
            items(uiState.exersiceList.size) {
                val exersice = uiState.exersiceList.get(index = it)
                ExersiceItem(exersice = exersice,
                    onAddSetClick = onAddSetClick,
                    onUpdateSet = onUpdateSet
                )
            }
        }

        WorkoutBottomButtons(
            uiState = uiState,
            onAddExersiceClick = onAddExersiceClick,
            onSaveWorkoutClick = onSaveWorkoutClick,
            onDeleteWorkoutClick = onDeleteWorkoutClick
        )
    }
}

@Composable
fun WorkoutBottomButtons(
    uiState: WorkoutDetailsUIState,
    onAddExersiceClick: (Exersice) -> Unit = {},
    onSaveWorkoutClick: () -> Unit = {},
    onDeleteWorkoutClick: () -> Unit = {},
){
    Row(modifier = Modifier.fillMaxWidth()) {
        Button(
            modifier = Modifier.weight(1f),
            onClick = {
                onAddExersiceClick.invoke(
                    Exersice(
                        id = uiState.exersiceList.size,
                        //TODO update
                        type = ExerciseType(name ="Bench Press"),
                        sets = emptyList()
                    )
                )
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Text(
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimary,
                text = "Add exersice"
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
                color = MaterialTheme.colorScheme.onPrimary,
                text = "Delete"
            )
        }
        Spacer(modifier = Modifier.padding(horizontal = 6.dp))
        Button(
            modifier = Modifier.weight(1f),
            onClick = {
                onSaveWorkoutClick.invoke()
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Text(
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimary,
                text = "Save"
            )
        }
    }
}

@Composable
fun ExersiceHeader(exersiceName: String) {
    Column {
        val textFieldDefaults = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            errorContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )

        TextField(
            value = exersiceName,
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
fun ExersiceItem(
    exersice: Exersice,
    onAddSetClick: (exersiceId: Int) -> Unit = {},
    onUpdateSet: (exersiceId: Int, set: Set) -> Unit = { _, _ -> },
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        //TODO can be error
        ExersiceHeader(exersice.type.name)
        Column(modifier = Modifier.fillMaxWidth()) {
            repeat(exersice.sets.size) {
                val set = exersice.sets.get(index = it)
                SetItem(
                    set,
                    onWeightChanged = {
                        onUpdateSet.invoke(exersice.id, set.copy(weight = it))
                    },
                    onRepsChanged = {
                        onUpdateSet.invoke(exersice.id, set.copy(reps = it))
                    })
            }
        }

        Button(onClick = {
            onAddSetClick.invoke(exersice.id)
        }) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = "Add set"
            )
        }
    }

}

@Composable
fun SetItem(
    set: Set,
    onRepsChanged: (String) -> Unit = {},
    onWeightChanged: (String) -> Unit = {},
) {
    Row(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth(),
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
        //placeholder = { Text(text = "0") },
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