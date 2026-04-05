package com.example.workout.screens.exersices

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.Composable
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
import com.example.workout.screens.exersices.state.ExerciseListUIState

@Composable
fun ExersiceListScreen(
    viewModel: ExersiceListViewModel = hiltViewModel(),
) {

    val uiState = viewModel.state.collectAsStateWithLifecycle()

    ExersiceListScreenView(uiState = uiState.value, onSelectExercise = {
        viewModel.onAction(ExerciseListUIAction.SelectExercise(it))
    })
}

@Composable
fun ExersiceListScreenView(uiState: ExerciseListUIState, onSelectExercise: (Int) -> Unit = {}) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        ExerciseList(uiState = uiState, onSelectExercise)

        Button(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            onClick = {}
        ) {
            Text("Add exercise")
        }
    }

}

@Composable
fun ExerciseList(uiState: ExerciseListUIState, onSelectExercise: (Int) -> Unit) {
    LazyColumn {
        items(uiState.exersiceList, key = { it.id }) {
            ExerciseItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                name = it.name,
                isSelected = it.isSelected,
                onClick = { onSelectExercise.invoke(it.id) }
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
            ExerciseEditableItem(modifier = Modifier.padding(vertical = 12.dp))
        }
    }
}

@Composable
fun ExerciseItem(
    modifier: Modifier,
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .clickable { onClick.invoke() }
            .height(60.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Log.d("fsdfdsfsdf", "ExerciseItem: $isSelected")
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
        Text(modifier = modifier, text = name, fontSize = 24.sp)
    }
}

@Composable
fun ExerciseEditableItem(modifier: Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxHeight()
                .width(2.dp)
                .background(
                    MaterialTheme.colorScheme.primary
                )
        )
        Text(modifier = modifier, text = "Edit my here", fontSize = 24.sp)
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
            ExersiceListScreenView(uiState = ExerciseListUIState.preview())
        }
    }
}