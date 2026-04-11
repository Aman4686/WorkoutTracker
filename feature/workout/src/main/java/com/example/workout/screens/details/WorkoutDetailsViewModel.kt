package com.example.workout.screens.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.WorkoutDomain
import com.example.domain.model.Exercise
import com.example.domain.model.Set
import com.example.workout.screens.details.state.ExerciseUIModel
import com.example.workout.screens.details.state.SetUIModel
import com.example.workout.screens.details.state.WorkoutDetailsSideEffect
import com.example.workout.screens.details.state.WorkoutDetailsUIAction
import com.example.workout.screens.details.state.WorkoutDetailsUIState
import com.example.workout.screens.details.state.toDomain
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TAG = "WorkoutDetailsViewModel"

@OptIn(FlowPreview::class)
@HiltViewModel(assistedFactory = WorkoutDetailsViewModel.Factory::class)
class WorkoutDetailsViewModel @AssistedInject constructor(
    @Assisted val workoutId: Int,
    private val workoutDomain: WorkoutDomain,
) : ViewModel() {

    private val _effect = Channel<WorkoutDetailsSideEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    private val _setsToUpdate = MutableStateFlow<Map<Int, SetUIModel>>(emptyMap())

    private val _state = MutableStateFlow(WorkoutDetailsUIState.initial())
    val state: StateFlow<WorkoutDetailsUIState> = _state

    init {

        workoutDomain.getExerciseFlow(workoutId).onEach {
            val state = mapToUIState(it)
            _state.emit(state)
        }.launchIn(viewModelScope)



        _setsToUpdate
            .debounce(1000)
            .onEach {
                workoutDomain.updateSets(it.values.map { set ->
                    set.toDomain()
                }.toList())


                _setsToUpdate.update { emptyMap() }
            }.launchIn(viewModelScope)

    }

    fun onAction(action: WorkoutDetailsUIAction) {
        when (action) {
            is WorkoutDetailsUIAction.AddSet -> onAddSetAction(action)
            is WorkoutDetailsUIAction.UpdateSet -> onUpdateSet(action)
            is WorkoutDetailsUIAction.DeleteWorkout -> onDeleteWorkout()
        }
    }

    private fun mapToUIState(
        exercises: List<Exercise>,
    ): WorkoutDetailsUIState {
        val exerciseList = exercises.map { exercise ->
            ExerciseUIModel(
                id = exercise.id,
                type = exercise.type,
                sets = exercise.sets.map { set ->
                    SetUIModel(
                        id = set.id,
                        count = set.count,
                        weight = set.weight,
                        reps = set.reps,
                    )
                }.toImmutableList()
            )
        }.toImmutableList()

        return WorkoutDetailsUIState(exerciseList = exerciseList)
    }

    private fun onUpdateSet(action: WorkoutDetailsUIAction.UpdateSet) {
        Log.d(TAG, "onUpdateSet: ${action}")

        _state.update {
            val exercise = it.exerciseList.map { exercise ->
                if (exercise.id == action.exerciseId) {
                    exercise.copy(
                        sets =
                            exercise.sets.map { set ->
                                if (set.id == action.set.id) {
                                    set.copy(
                                        weight = action.set.weight,
                                        reps = action.set.reps
                                    )
                                } else set
                            }.toImmutableList()
                    )
                } else exercise
            }.toImmutableList()



            WorkoutDetailsUIState(exerciseList = exercise)

        }

        _setsToUpdate.update {
            it + (action.set.id to action.set)
        }
    }

    private fun onDeleteWorkout() {
        viewModelScope.launch {
            workoutDomain.deleteWorkout(workoutId)
            _effect.send(WorkoutDetailsSideEffect.NavigateBack)
        }
    }

    private fun onAddSetAction(addSetAction: WorkoutDetailsUIAction.AddSet) {
        val exerciseId = addSetAction.exerciseId
        val currentExercise = state.value.exerciseList.find { it.id == exerciseId }
        val setsSize = currentExercise?.sets?.size ?: 0

        val count = if (setsSize == 0) 1 else setsSize.plus(1)

        viewModelScope.launch {
            workoutDomain.addSet(addSetAction.exerciseId, Set.new(count = count))
        }
    }


    @AssistedFactory
    interface Factory {
        fun create(workoutId: Int): WorkoutDetailsViewModel
    }
}
