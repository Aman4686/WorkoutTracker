package com.example.workout.screens.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.WorkoutDomain
import com.example.domain.model.Workout
import com.example.workout.screens.list.state.WorkoutListSideEffect
import com.example.workout.screens.list.state.WorkoutListUIAction
import com.example.workout.screens.list.state.WorkoutListUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class WorkoutListViewModel @Inject constructor(
    private val workoutDomain: WorkoutDomain,
) : ViewModel() {

    private val _effect = Channel<WorkoutListSideEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    private val _state: MutableStateFlow<WorkoutListUIState> = MutableStateFlow(
        WorkoutListUIState.initial()
    )

    val state: StateFlow<WorkoutListUIState> = combine(
        _state,
        workoutDomain.getWorkoutsFlow()
    ) { state, workouts ->
        state.copy(workoutsList = workouts.toImmutableList())
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = WorkoutListUIState.initial()
    )

    fun onAction(action: WorkoutListUIAction) {
        when (action) {
            is WorkoutListUIAction.AddNewWorkout -> onAddNewWorkout()
            is WorkoutListUIAction.DeleteWorkout -> onDeleteWorkout(action.workoutId)
        }
    }

    private fun onAddNewWorkout() {
        viewModelScope.launch {
            workoutDomain.insertOrUpdateWorkout(Workout.new())
        }
    }

    private fun onDeleteWorkout(workoutId: Int) {
        viewModelScope.launch {
            workoutDomain.deleteWorkout(workoutId)
        }
    }

    override fun onCleared() {
        super.onCleared()
        _effect.close()
    }
}
