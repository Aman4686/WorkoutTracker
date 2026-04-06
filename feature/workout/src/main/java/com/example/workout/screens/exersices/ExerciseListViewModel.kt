package com.example.workout.screens.exersices

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.WorkoutDomain
import com.example.domain.model.ExerciseType
import com.example.workout.screens.details.WorkoutDetailsViewModel
import com.example.workout.screens.exercises.state.ExerciseListUIAction
import com.example.workout.screens.exercises.state.ExerciseListUIState
import com.example.workout.screens.exercises.state.toUIModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = ExerciseListViewModel.Factory::class)
class ExerciseListViewModel @AssistedInject constructor(
    @Assisted val workoutId: Int,
    private val workoutDomain: WorkoutDomain,
) : ViewModel() {

    private val _selectedIds = MutableStateFlow<Set<Int>>(emptySet())

    val state: StateFlow<ExerciseListUIState> = combine(
        workoutDomain.getExerciseTypeFlow(),
        _selectedIds,
    ) { exerciseTypes, selectedIds ->
        ExerciseListUIState(
            exerciseList = exerciseTypes.map { it.toUIModel(isSelected = it.id in selectedIds) }.toImmutableList()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = ExerciseListUIState.initial(),
    )

    fun onAction(action: ExerciseListUIAction) {
        when (action) {
            is ExerciseListUIAction.SelectExercise -> onExerciseSelected(action.exerciseId)
            is ExerciseListUIAction.AddExercise -> onAddExercise(action.exerciseName)
        }
    }

    private fun onExerciseSelected(id: Int) {
        _selectedIds.update { selected ->
            if (id in selected) selected.minus(id) else selected.plus(id)
        }
    }

    private fun onAddExercise(name: String) {
        viewModelScope.launch {
            workoutDomain.addExerciseType(ExerciseType(name = name))
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(workoutId: Int): ExerciseListViewModel
    }

}