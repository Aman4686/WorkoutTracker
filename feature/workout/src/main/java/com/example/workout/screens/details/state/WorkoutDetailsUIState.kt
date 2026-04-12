package com.example.workout.screens.details.state

import com.example.domain.model.ExerciseType
import com.example.domain.model.Set
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

sealed interface WorkoutFlatListItem {
    data class ExerciseHeader(val exercise: ExerciseUIModel) : WorkoutFlatListItem
    data class ExerciseSet(val exerciseId: Int, val set: SetUIModel) : WorkoutFlatListItem
    data class AddSetButton(val exerciseId: Int, val count: Int) : WorkoutFlatListItem
}

data class WorkoutDetailsUIState(
    val isLoading: Boolean = true,
    val exerciseFlatList: ImmutableList<WorkoutFlatListItem> = persistentListOf(),
) {

    companion object {
        fun initial() =
            WorkoutDetailsUIState(isLoading = true, exerciseFlatList = persistentListOf())

        fun preview(): WorkoutDetailsUIState {

            val exercisePreview1 = ExerciseUIModel.preview(1)
            val exercisePreview2 = ExerciseUIModel.preview(2)

            val exercisePreviewList = listOf(exercisePreview1, exercisePreview2)

            return WorkoutDetailsUIState(
                isLoading = false,
                exerciseFlatList = exercisePreviewList.flatMap { exercise ->
                    buildList {
                        add(WorkoutFlatListItem.ExerciseHeader(exercise))
                        addAll(exercise.sets.map { set ->
                            WorkoutFlatListItem.ExerciseSet(exercise.id, set)
                        })
                        add(WorkoutFlatListItem.AddSetButton(exercise.id, exercise.sets.size.plus(1)))
                    }
                }.toImmutableList()
            )
        }
    }
}


data class ExerciseUIModel(
    val id: Int = 0,
    val type: ExerciseType,
    val sets: ImmutableList<SetUIModel> = persistentListOf(),
) {
    companion object {

        fun preview(id: Int): ExerciseUIModel = ExerciseUIModel(
            id = id,
            type = ExerciseType(
                name = "Bench Press"
            ),
            sets = persistentListOf(SetUIModel.preview(1), SetUIModel.preview(2))
        )
    }
}

data class SetUIModel(
    val id: Int,
    val count: Int = 1,
    val weight: String = "0",
    val reps: String = "0",
    val isCheck: Boolean = false,
) {
    companion object {
        fun preview(id: Int): SetUIModel = SetUIModel(
            id = id,
            count = 1,
            weight = "55",
            reps = "12"
        )
    }
}

fun SetUIModel.toDomain(): Set = Set(
    id = this.id,
    count = this.count,
    weight = this.weight,
    reps = this.reps
)