package com.example.workout.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Set
import com.example.domain.model.Workout
import com.example.domain.repository.WorkoutRepository
//import com.example.domain.repository.WorkoutRepository
import com.example.workout.screens.details.state.WorkoutDetailsUIAction
import com.example.workout.screens.details.state.WorkoutDetailsUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@HiltViewModel
class WorkoutDetailsViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository
): ViewModel() {

    private val _state: MutableStateFlow<WorkoutDetailsUIState> = MutableStateFlow(WorkoutDetailsUIState.initial())
    val state: StateFlow<WorkoutDetailsUIState> by lazy {
        _state.onStart {
            viewModelScope.launch {
                //TODO: fetch workout details
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = WorkoutDetailsUIState.initial()
        )
    }

    fun onAction(action: WorkoutDetailsUIAction){
        when(action){
            is WorkoutDetailsUIAction.AddExercise ->{
                _state.update { currentState ->
                    currentState.copy(
                        exerciseList = currentState.exerciseList + action.exercise
                    )
                }
            }
            is WorkoutDetailsUIAction.AddSet -> {
                _state.update { currentState ->
                    currentState.copy(
                        exerciseList = currentState.exerciseList.map { exercise ->
                            if (exercise.id == action.exerciseId) {
                                exercise.copy(
                                    sets = exercise.sets + Set(id = exercise.sets.size, count = 1, weight = "", reps = "")
                                )
                            } else exercise
                        }
                    )
                }
            }
            is WorkoutDetailsUIAction.SaveWorkout -> {
                viewModelScope.launch {

                    val formatter = DateTimeFormatter.ofPattern("dd-MM")
                    val current = LocalDateTime.now().format(formatter)

                    workoutRepository.addWorkout(
                        Workout(
                            date = current,
                            exerciseList = state.value.exerciseList
                        )
                    )
                }
            }
        }
    }
}