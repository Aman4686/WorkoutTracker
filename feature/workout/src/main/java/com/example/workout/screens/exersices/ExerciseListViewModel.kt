package com.example.workout.screens.exersices

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.WorkoutDomain
import com.example.domain.model.Exersice
import com.example.workout.screens.details.state.WorkoutDetailsUIAction
import com.example.workout.screens.exersices.state.ExerciseListUIAction
import com.example.workout.screens.exersices.state.ExerciseListUIState
import com.example.workout.screens.exersices.state.toUIModel
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

@HiltViewModel
class ExersiceListViewModel @Inject constructor(
    private val workoutDomain: WorkoutDomain
) : ViewModel() {

    private val _state: MutableStateFlow<ExerciseListUIState> = MutableStateFlow(
        ExerciseListUIState.initial()
    )

    val state: StateFlow<ExerciseListUIState> by lazy {
        _state.onStart {

        //TODO update
//        workoutDomain.getExersiceFlow().map { exerciseList ->
//            ExerciseListUIState(
//                exersiceList = exerciseList.map { exercise ->
//                    exercise.toUIModel()
//                }.toImmutableList()
//            )
//        }
      //      ExerciseListUIState.initial()

        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = ExerciseListUIState.initial()
        )
    }

    fun onAction(action: ExerciseListUIAction) {
        when (action) {
            is ExerciseListUIAction.SelectExercise -> onExerciseSelected(action.exerciseId)

        }
    }

    // TODO use HashMap here
    private fun onExerciseSelected(id: Int) {
        Log.d("fsdfdsfsdf", "onExerciseSelected: $id")
        _state.update {
            it.copy(
                exersiceList = it.exersiceList.map { exersice ->
                    if (exersice.id == id) {
                        exersice.copy(isSelected = !exersice.isSelected)
                    } else exersice
                }.toImmutableList()
            )
        }
    }

}