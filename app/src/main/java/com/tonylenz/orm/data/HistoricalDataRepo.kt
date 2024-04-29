package com.tonylenz.orm.data

import com.tonylenz.orm.model.Exercise
import com.tonylenz.orm.model.WorkoutSet

interface HistoricalDataRepo {

    /**
     * Save the list of workouts sets.
     *
     * @param workouts The [List] of [WorkoutSet] to persist
     * @param replaceAll Completely replace existing [WorkoutSet] data if `true` or append to
     * existing [WorkoutSet] data if `false`.
     */
    suspend fun saveWorkoutSets(workouts: List<WorkoutSet>, replaceAll: Boolean)

    /**
     * @return The saved [List] of [WorkoutSet] data
     */
    suspend fun getWorkoutSets(): List<WorkoutSet>

    /**
     * @return The saved [List] of [WorkoutSet] data for a specified [Exercise]
     */
    suspend fun getWorkoutSetsByExercise(exercise: Exercise): List<WorkoutSet>
}

/**
 * In-memory implementation for testing and basic implementation
 */
class HistoricalDataMemRepo : HistoricalDataRepo {

    private val data: MutableList<WorkoutSet> = mutableListOf()

    override suspend fun saveWorkoutSets(workouts: List<WorkoutSet>, replaceAll: Boolean) {
        synchronized(this) { // Prevent concurrent modifications of the underlying data
            if (replaceAll) {
                data.clear()
            }
            data.addAll(workouts)
        }

    }

    override suspend fun getWorkoutSets(): List<WorkoutSet> {
        return synchronized(this) {
            data.map { it } // Copy list, prevent leaking underlying mutable list
        }
    }

    override suspend fun getWorkoutSetsByExercise(exercise: Exercise): List<WorkoutSet> {
        return synchronized(this) {
            data.filter { it.exercise == exercise }
        }
    }
}

// TODO: Implement using some backing data access mechanism, e.g. Room or an API
class HistoricalDataRepoImpl : HistoricalDataRepo {
    override suspend fun saveWorkoutSets(workouts: List<WorkoutSet>, replaceAll: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun getWorkoutSets(): List<WorkoutSet> {
        TODO("Not yet implemented")
    }

    override suspend fun getWorkoutSetsByExercise(exercise: Exercise): List<WorkoutSet> {
        TODO("Not yet implemented")
    }
}
