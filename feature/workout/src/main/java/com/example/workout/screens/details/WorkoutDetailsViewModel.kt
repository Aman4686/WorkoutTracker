package com.example.workout.screens.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.WorkoutDomain
import com.example.domain.model.Set
import com.example.workout.screens.details.state.ExerciseUIModel
import com.example.workout.screens.details.state.SetEdit
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
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = WorkoutDetailsViewModel.Factory::class)
class WorkoutDetailsViewModel @AssistedInject constructor(
    @Assisted val workoutId: Int,
    private val workoutDomain: WorkoutDomain,
) : ViewModel() {

    private val _effect = Channel<WorkoutDetailsSideEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    private val _editSet = MutableStateFlow<Map<Int, SetEdit>>(emptyMap())

    private val editQueue = Channel<SetEdit>(Channel.UNLIMITED)

    init {
        viewModelScope.launch {
            val jobs = HashMap<Int, Job>()

            for (edit in editQueue) {               // suspends, processes each item
                jobs[edit.set.id]?.cancel()         // cancel previous debounce for this set
                jobs[edit.set.id] = launch {
                    delay(1000)
                    workoutDomain.updateSet(edit.exerciseId, edit.set.toDomain())
                    _editSet.update { it - edit.set.id }
                }
            }
        }
    }



    val state: StateFlow<WorkoutDetailsUIState> =
        combine(
            _editSet,
            workoutDomain.getExerciseFlow(workoutId),
        ) { editSet, exercises ->
            Log.d("fdsfdsfsd", "state uptate ${editSet}")
            WorkoutDetailsUIState(exerciseList = exercises.map { exercise ->
                val setsList = exercise.sets.map { set ->
                    val edit = editSet[set.id]
                    val weight = edit?.set?.weight ?: set.weight
                    val reps = edit?.set?.reps ?: set.reps

                    SetUIModel(
                        id = set.id,
                        count = set.count,
                        weight = weight,
                        reps = reps,
                    )


                }.toImmutableList()

                ExerciseUIModel(
                    id = exercise.id,
                    type = exercise.type,
                    sets = setsList
                )


            }.toImmutableList())
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = WorkoutDetailsUIState.initial()
        )

    fun onAction(action: WorkoutDetailsUIAction) {
        when (action) {
            is WorkoutDetailsUIAction.AddSet -> onAddSetAction(action)
            is WorkoutDetailsUIAction.UpdateSet -> onUpdateSet(action)
            is WorkoutDetailsUIAction.DeleteWorkout -> onDeleteWorkout()
        }
    }

    private fun onUpdateSet(action: WorkoutDetailsUIAction.UpdateSet) {
        _editSet.update { it + (action.set.id to SetEdit(action.exerciseId, action.set)) }
        editQueue.trySend(SetEdit(action.exerciseId, action.set))
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
