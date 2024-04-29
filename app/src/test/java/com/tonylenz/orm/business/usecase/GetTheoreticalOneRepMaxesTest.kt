package com.tonylenz.orm.business.usecase

import com.tonylenz.orm.data.HistoricalDataMemRepo
import com.tonylenz.orm.data.HistoricalDataRepo
import com.tonylenz.orm.model.Exercise
import com.tonylenz.orm.model.OneRepMaxEstimateStrategy
import com.tonylenz.orm.model.Reps
import com.tonylenz.orm.model.Weight
import com.tonylenz.orm.model.WeightUnit
import com.tonylenz.orm.model.WorkoutSet
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

//TODO: Add more edge-case tests
class GetTheoreticalOneRepMaxesTest {

    @Test
    fun `Happy path`() = runTest {
        val historyData = listOf(
            WorkoutSet(
                exercise = Exercise("Back Squat"),
                reps = Reps(3),
                weight = Weight(385.0, WeightUnit.Pounds),
                date = LocalDate(2020, 6, 4),
            ),
            WorkoutSet(
                exercise = Exercise("Deadlift"),
                reps = Reps(1),
                weight = Weight(580.0, WeightUnit.Pounds),
                date = LocalDate(2020, 6, 6),
            ),
            WorkoutSet(
                exercise = Exercise("Deadlift"),
                reps = Reps(8),
                weight = Weight(100.0, WeightUnit.Pounds),
                date = LocalDate(2020, 6, 7),
            ),
            WorkoutSet(
                // Same day as above but a smaller ORM, should be dropped in the results
                exercise = Exercise("Deadlift"),
                reps = Reps(6),
                weight = Weight(100.0, WeightUnit.Pounds),
                date = LocalDate(2020, 6, 7),
            ),
            WorkoutSet(
                exercise = Exercise("Deadlift"),
                reps = Reps(5),
                weight = Weight(145.0, WeightUnit.Pounds),
                date = LocalDate(2020, 6, 8),
            ),
        ).shuffled() // Slight fuzzing to ensure correctness

        val repo = HistoricalDataMemRepo()
        repo.saveWorkoutSets(historyData, replaceAll = false)

        val result = getTheoreticalOneRepMaxes(repo, OneRepMaxEstimateStrategy.Brzycki)

        assertTrue(result.isOk)

        assertEquals(
            listOf(
                OneRepMax(
                    exercise = Exercise("Back Squat"),
                    weight = Weight(407.6470588235294, WeightUnit.Pounds),
                    dailyOneRepMax = listOf(
                        LocalDate(2020, 6, 4) to Weight(407.6470588235294, WeightUnit.Pounds),
                    )
                ),
                OneRepMax(
                    exercise = Exercise("Deadlift"),
                    weight = Weight(580.0, WeightUnit.Pounds),
                    dailyOneRepMax = listOf(
                        LocalDate(2020, 6, 6) to Weight(580.0, WeightUnit.Pounds),
                        LocalDate(2020, 6, 7) to Weight(124.13793103448276, WeightUnit.Pounds),
                        LocalDate(2020, 6, 8) to Weight(163.125, WeightUnit.Pounds),
                    )
                )
            ),
            result.value
        )
    }

    class ExpectedException : Exception()

    @Test
    fun `Fail expected exception`() = runTest {
        val result = getTheoreticalOneRepMaxes(object : HistoricalDataRepo {
            override suspend fun saveWorkoutSets(workouts: List<WorkoutSet>, replaceAll: Boolean) {
                throw ExpectedException()
            }

            override suspend fun getWorkoutSets(): List<WorkoutSet> {
                throw ExpectedException()
            }

            override suspend fun getWorkoutSetsByExercise(exercise: Exercise): List<WorkoutSet> {
                throw ExpectedException()
            }

        }, OneRepMaxEstimateStrategy.Brzycki)

        assertTrue(result.isErr)

        assertIs<ExpectedException>(result.error)
    }
}
