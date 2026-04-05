package com.example.workout.screens.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.WorkoutDomain
import com.example.domain.repository.WorkoutRepository
import com.example.workout.screens.details.state.WorkoutDetailsUIState
import com.example.workout.screens.list.state.WorkoutListUIState
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class WorkoutListViewModel @Inject constructor(
    private val workoutDomain: WorkoutDomain,
) : ViewModel() {

    private val _state: MutableStateFlow<WorkoutListUIState> = MutableStateFlow(
        WorkoutListUIState.initial()
    )

    val state: StateFlow<WorkoutListUIState> by lazy {
        workoutDomain.getWorkoutsFlow().map {
            WorkoutListUIState(
                workoutsList = it.toImmutableList()
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = WorkoutListUIState.initial()
        )
    }
}