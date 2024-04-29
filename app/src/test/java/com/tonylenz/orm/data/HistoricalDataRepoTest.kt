package com.tonylenz.orm.data

import com.tonylenz.orm.model.Exercise
import com.tonylenz.orm.model.Reps
import com.tonylenz.orm.model.Weight
import com.tonylenz.orm.model.WeightUnit
import com.tonylenz.orm.model.WorkoutSet
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import java.time.Month
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

// Base test class
abstract class HistoricalDataRepoTest {

    abstract val repo: HistoricalDataRepo

    @Test
    fun `Save and fetch data - with append`() = runTest {
        val data = listOf(
            WorkoutSet(
                exercise = Exercise("Squat"),
                reps = Reps(15),
                weight = Weight(145.0, WeightUnit.Pounds),
                date = LocalDate(2024, Month.JANUARY, 5)
            ),
            WorkoutSet(
                exercise = Exercise("Squat"),
                reps = Reps(8),
                weight = Weight(175.0, WeightUnit.Pounds),
                date = LocalDate(2024, Month.JANUARY, 8)
            ),
            WorkoutSet(
                exercise = Exercise("Bench"),
                reps = Reps(15),
                weight = Weight(135.0, WeightUnit.Pounds),
                date = LocalDate(2024, Month.JANUARY, 5)
            )
        )

        repo.saveWorkoutSets(data, false)

        repo.getWorkoutSets().also { repoData ->
            assertTrue(repoData.containsAll(data))
            assertEquals(data.size, repoData.size)
        }

        repo.getWorkoutSetsByExercise(Exercise("Bench")).also { repoData ->
            val benchData = data.filter { it.exercise == Exercise(("Bench")) }
            assertTrue(repoData.containsAll(benchData))
            assertEquals(benchData.size, repoData.size)
        }

        repo.getWorkoutSetsByExercise(Exercise("Squat")).also { repoData ->
            val squatData = data.filter { it.exercise == Exercise(("Squat")) }
            assertTrue(repoData.containsAll(squatData))
            assertEquals(squatData.size, repoData.size)
        }

        val additionalData = listOf(
            WorkoutSet(
                exercise = Exercise("Deadlift"),
                reps = Reps(8),
                weight = Weight(350.0, WeightUnit.Pounds),
                date = LocalDate(2024, Month.JANUARY, 8)
            )
        )

        repo.saveWorkoutSets(additionalData, false)

        repo.getWorkoutSets().also { repoData ->
            assertTrue(repoData.containsAll(data + additionalData))
            assertEquals(data.size + additionalData.size, repoData.size)
        }
    }


    @Test
    fun `Save and fetch data - with replace`() = runTest {
        val data = listOf(
            WorkoutSet(
                exercise = Exercise("Squat"),
                reps = Reps(15),
                weight = Weight(145.0, WeightUnit.Pounds),
                date = LocalDate(2024, Month.JANUARY, 5)
            ),
            WorkoutSet(
                exercise = Exercise("Squat"),
                reps = Reps(8),
                weight = Weight(175.0, WeightUnit.Pounds),
                date = LocalDate(2024, Month.JANUARY, 8)
            ),
            WorkoutSet(
                exercise = Exercise("Bench"),
                reps = Reps(15),
                weight = Weight(135.0, WeightUnit.Pounds),
                date = LocalDate(2024, Month.JANUARY, 5)
            )
        )

        repo.saveWorkoutSets(data, false)

        repo.getWorkoutSets().also { repoData ->
            assertTrue(repoData.containsAll(data))
            assertEquals(data.size, repoData.size)
        }

        val additionalData = listOf(
            WorkoutSet(
                exercise = Exercise("Deadlift"),
                reps = Reps(8),
                weight = Weight(350.0, WeightUnit.Pounds),
                date = LocalDate(2024, Month.JANUARY, 8)
            )
        )

        repo.saveWorkoutSets(additionalData, true)

        repo.getWorkoutSets().also { repoData ->
            assertTrue(repoData.containsAll(additionalData))
            assertEquals(additionalData.size, repoData.size)
        }
    }
}

class HistoricalDataMemRepoTest : HistoricalDataRepoTest() {
    override val repo: HistoricalDataRepo = HistoricalDataMemRepo()
}

// TODO: Uncomment below when HistoricalDataRepoImpl is implemented
//class HistoricalDataRepoImplTest : HistoricalDataRepoTest() {
//    override val repo: HistoricalDataRepo = HistoricalDataRepoImpl()
//}
