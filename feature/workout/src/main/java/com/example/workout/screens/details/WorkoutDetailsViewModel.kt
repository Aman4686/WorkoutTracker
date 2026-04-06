package com.example.workout.screens.details

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.WorkoutDomain
import com.example.domain.model.Set
import com.example.domain.model.Workout
import com.example.workout.screens.details.state.WorkoutDetailsUIAction
import com.example.workout.screens.details.state.WorkoutDetailsUIState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@HiltViewModel(assistedFactory = WorkoutDetailsViewModel.Factory::class)
class WorkoutDetailsViewModel @AssistedInject constructor(
    @Assisted val workoutId: Int,
    private val workoutDomain: WorkoutDomain,
) : ViewModel() {

    private val _navigateBack = MutableSharedFlow<Unit>()
    val navigateBack: SharedFlow<Unit> = _navigateBack.asSharedFlow()

    private val _state: MutableStateFlow<WorkoutDetailsUIState> =
        MutableStateFlow(WorkoutDetailsUIState.initial())

    val state: StateFlow<WorkoutDetailsUIState> by lazy {

        workoutDomain.getExerciseFlow(workoutId).map {
            WorkoutDetailsUIState(exerciseList = it) }

            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = WorkoutDetailsUIState.initial()
            )
    }

    fun onAction(action: WorkoutDetailsUIAction) {
        when (action) {
            is WorkoutDetailsUIAction.AddExercise -> onAddExerciseAction(action)
            is WorkoutDetailsUIAction.AddSet -> onAddSetAction(action)
            is WorkoutDetailsUIAction.SaveWorkout -> onSaveWorkoutAction()
            is WorkoutDetailsUIAction.UpdateExercise -> onUpdateExercise(action)
            is WorkoutDetailsUIAction.UpdateSet -> onUpdateSet(action)
            is WorkoutDetailsUIAction.DeleteWorkout -> onDeleteWorkout()
        }
    }

    private fun onUpdateExercise(action: WorkoutDetailsUIAction.UpdateExercise) {
        _state.update { currentState ->

            currentState.copy(
                exerciseList = currentState.exerciseList.map { exercise ->
                    if (exercise.id == action.exercise.id) {
                        exercise.copy(
                            sets = exercise.sets,
                            type = exercise.type
                        )
                    } else exercise
                }
            )
        }
    }

    private fun onUpdateSet(action: WorkoutDetailsUIAction.UpdateSet) {
        _state.update { currentState ->
            currentState.copy(
                exerciseList = currentState.exerciseList.map { exercise ->
                    if (exercise.id == action.exerciseId) {
                        exercise.copy(
                            sets = exercise.sets.map { set ->
                                if (set.count == action.set.count) action.set else set
                            }
                        )
                    } else exercise
                }
            )
        }
    }


    private fun onDeleteWorkout() {
        viewModelScope.launch {
            workoutDomain.deleteWorkout(workoutId)
            _navigateBack.emit(Unit)
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
                        val exerciseSetsSize = exercise.sets.size
                        exercise.copy(
                            sets = exercise.sets + Set(
                                id = exercise.sets.size,
                                count = exerciseSetsSize.plus(1),
                                weight = "0",
                                reps = "0"
                            )
                        )
                    } else exercise
                }
            )
        }
    }

    private fun onSaveWorkoutAction() {
        viewModelScope.launch {
            //TODO move to core
            val formatter = DateTimeFormatter.ofPattern("dd-MM")
            val current = LocalDateTime.now().format(formatter)

            workoutDomain.insertOrUpdateWorkout(
                Workout(
                    id = workoutId,
                    date = current,
                    exerciseList = state.value.exerciseList
                )
            )
        }
    }


    @AssistedFactory
    interface Factory {
        fun create(workoutId: Int): WorkoutDetailsViewModel
    }
}
