package com.example.workout.screens.details

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.domain.WorkoutDomain
import com.example.domain.model.Set
import com.example.domain.model.Workout
import com.example.domain.repository.WorkoutRepository
import com.example.workout.navigation.Route
//import com.example.domain.repository.WorkoutRepository
import com.example.workout.screens.details.state.WorkoutDetailsUIAction
import com.example.workout.screens.details.state.WorkoutDetailsUIState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@HiltViewModel
class WorkoutDetailsViewModel @Inject constructor(
    private val workoutDomain: WorkoutDomain,
) : ViewModel() {

    private val _state: MutableStateFlow<WorkoutDetailsUIState> =
        MutableStateFlow(WorkoutDetailsUIState.initial())
    val state: StateFlow<WorkoutDetailsUIState> = _state.asStateFlow()

    fun onAction(action: WorkoutDetailsUIAction) {
        when (action) {
            is WorkoutDetailsUIAction.LoadWorkout -> onLoadWorkoutAction(action)
            is WorkoutDetailsUIAction.AddExercise -> onAddExerciseAction(action)
            is WorkoutDetailsUIAction.AddSet -> onAddSetAction(action)
            is WorkoutDetailsUIAction.SaveWorkout -> onSaveWorkoutAction()
        }
    }

    private fun onLoadWorkoutAction(loadWorkoutAction: WorkoutDetailsUIAction.LoadWorkout) {
        viewModelScope.launch {
            if (loadWorkoutAction.workoutId == 0) return@launch
            Log.d("WorkoutLog", "initial: ${loadWorkoutAction.workoutId}")
            val workout = workoutDomain.getWorkout(loadWorkoutAction.workoutId)

            workout?.let {
                _state.update { currentState ->
                    currentState.copy(
                        exerciseList = it.exerciseList
                    )
                }
            }
        }
    }

    private fun onAddExerciseAction(addExerciseAction: WorkoutDetailsUIAction.AddExercise) {
        _state.update { currentState ->
            currentState.copy(
                exerciseList = currentState.exerciseList + addExerciseAction.exercise
            )
        }
    }

    private fun onAddSetAction(addSetAction: WorkoutDetailsUIAction.AddSet) {
        _state.update { currentState ->
            currentState.copy(
                exerciseList = currentState.exerciseList.map { exercise ->
                    if (exercise.id == addSetAction.exerciseId) {
                        exercise.copy(
                            sets = exercise.sets + Set(
                                id = exercise.sets.size,
                                count = 1,
                                weight = "",
                                reps = ""
                            )
                        )
                    } else exercise
                }
            )
        }
    }

    private fun onSaveWorkoutAction() {
        viewModelScope.launch {
            val formatter = DateTimeFormatter.ofPattern("dd-MM")
            val current = LocalDateTime.now().format(formatter)

            workoutDomain.addWorkout(
                Workout(
                    date = current,
                    exerciseList = state.value.exerciseList
                )
            )
        }
    }

}
