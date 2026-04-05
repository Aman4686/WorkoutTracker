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
import kotlinx.coroutines.flow.onStart
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
        _state.onStart {
            loadWorkout()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = WorkoutDetailsUIState.initial()
        )
    }

    private fun loadWorkout() {
        viewModelScope.launch {
            if (workoutId == 0) return@launch
            Log.d("WorkoutLog", "loadWorkout: $workoutId")
            val workout = workoutDomain.getWorkout(workoutId)
            workout?.let {
                _state.update { currentState ->
                    currentState.copy(exersiceList = it.exersiceList)
                }
            }
        }
    }

    fun onAction(action: WorkoutDetailsUIAction) {
        when (action) {
            is WorkoutDetailsUIAction.AddExersice -> onAddExersiceAction(action)
            is WorkoutDetailsUIAction.AddSet -> onAddSetAction(action)
            is WorkoutDetailsUIAction.SaveWorkout -> onSaveWorkoutAction()
            is WorkoutDetailsUIAction.UpdateExersice -> onUpdateExersice(action)
            is WorkoutDetailsUIAction.UpdateSet -> onUpdateSet(action)
            is WorkoutDetailsUIAction.DeleteWorkout -> onDeleteWorkout()
        }
    }

    private fun onUpdateExersice(action: WorkoutDetailsUIAction.UpdateExersice) {
        _state.update { currentState ->

            currentState.copy(
                exersiceList = currentState.exersiceList.map { exersice ->
                    if (exersice.id == action.exersice.id) {
                        exersice.copy(
                            sets = exersice.sets,
                            type = exersice.type
                        )
                    } else exersice
                }
            )
        }
    }

    private fun onUpdateSet(action: WorkoutDetailsUIAction.UpdateSet) {
        _state.update { currentState ->
            currentState.copy(
                exersiceList = currentState.exersiceList.map { exersice ->
                    if (exersice.id == action.exersiceId) {
                        exersice.copy(
                            sets = exersice.sets.map { set ->
                                if (set.count == action.set.count) action.set else set
                            }
                        )
                    } else exersice
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

    private fun onAddExersiceAction(addExersiceAction: WorkoutDetailsUIAction.AddExersice) {
        _state.update { currentState ->
            currentState.copy(
                exersiceList = currentState.exersiceList + addExersiceAction.exersice
            )
        }
    }

    private fun onAddSetAction(addSetAction: WorkoutDetailsUIAction.AddSet) {
        _state.update { currentState ->
            currentState.copy(
                exersiceList = currentState.exersiceList.map { exersice ->
                    if (exersice.id == addSetAction.exersiceId) {
                        val exersiceSetsSize = exersice.sets.size
                        exersice.copy(
                            sets = exersice.sets + Set(
                                id = exersice.sets.size,
                                count = exersiceSetsSize.plus(1),
                                weight = "0",
                                reps = "0"
                            )
                        )
                    } else exersice
                }
            )
        }
    }

    private fun onSaveWorkoutAction() {
        viewModelScope.launch {
            val formatter = DateTimeFormatter.ofPattern("dd-MM")
            val current = LocalDateTime.now().format(formatter)

            workoutDomain.insertOrUpdateWorkout(
                Workout(
                    id = workoutId,
                    date = current,
                    exersiceList = state.value.exersiceList
                )
            )
        }
    }


    @AssistedFactory
    interface Factory {
        fun create(workoutId: Int): WorkoutDetailsViewModel
    }
}
