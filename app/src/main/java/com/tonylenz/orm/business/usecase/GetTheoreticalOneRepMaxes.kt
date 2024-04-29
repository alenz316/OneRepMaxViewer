package com.tonylenz.orm.business.usecase

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.tonylenz.orm.business.calculateBrzyckiOneRepMax
import com.tonylenz.orm.data.HistoricalDataRepo
import com.tonylenz.orm.model.Exercise
import com.tonylenz.orm.model.OneRepMaxEstimateStrategy
import com.tonylenz.orm.model.Weight
import com.tonylenz.orm.model.WeightUnit
import com.tonylenz.orm.model.WorkoutSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate

data class OneRepMax(
    val exercise: Exercise,
    val weight: Weight,
    val dailyOneRepMax: List<Pair<LocalDate, Weight>>,
)


/**
 * Retrieves the historical workout data and calculates the theoretical one-rep max for each exercise.
 *
 * @param repo The [HistoricalDataRepo] containing the workout data
 * @param strategy The [OneRepMaxEstimateStrategy] used to calculate the theoretical one-rep max
 *
 * // TODO: Update to add support for more than just pounds
 * // TODO: Make cancellable
 */
suspend fun getTheoreticalOneRepMaxes(
    repo: HistoricalDataRepo,
    strategy: OneRepMaxEstimateStrategy
): Result<List<OneRepMax>, Exception> = withContext(Dispatchers.Default) {
    try {
        val theoreticalOneRepMaxCalc = when (strategy) {
            OneRepMaxEstimateStrategy.Brzycki -> ::calculateBrzyckiOneRepMax
            // TODO: Support more theoretical max calculations
        }
        // Group all workout sets by exercise
        val exerciseMap: Map<Exercise, List<WorkoutSet>> =
            repo.getWorkoutSets().groupBy { it.exercise }

        val oneRepMaxes = exerciseMap.map { exerciseSetsEntry ->
            val (exercise, sets) = exerciseSetsEntry
            val dailySets: Map<LocalDate, List<WorkoutSet>> = sets.groupBy { it.date }
            val dailyMaxes = dailySets.map { dateSetsEntry ->
                val (date, dateSets) = dateSetsEntry
                // Find set that yields greatest max
                val maxCalcVal = dateSets.maxOf { set ->
                    if (set.reps.count == 1) {
                        set.weight.value
                    } else {
                        theoreticalOneRepMaxCalc(set.weight, set.reps).value
                    }
                }
                date to Weight(maxCalcVal, WeightUnit.Pounds)
            }
            OneRepMax(
                exercise = exercise,
                weight = dailyMaxes.maxBy { it.second.value }.second,
                dailyOneRepMax = dailyMaxes.sortedBy { it.first }
            )
        }
        Ok(oneRepMaxes.sortedBy { it.exercise.name })
    } catch (e: Exception) {
        Err(e)
    }
}
