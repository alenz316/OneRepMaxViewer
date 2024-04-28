package com.tonylenz.orm.business.usecase

import com.github.michaelbull.result.Ok
import com.tonylenz.orm.common.toSource
import com.tonylenz.orm.data.HistoricalDataMemRepo
import com.tonylenz.orm.model.Exercise
import com.tonylenz.orm.model.Reps
import com.tonylenz.orm.model.Weight
import com.tonylenz.orm.model.WeightUnit
import com.tonylenz.orm.model.WorkoutSet
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ImportHistoricalDataTest {

    @Test
    fun `Basic import`() = runTest {
        val input = """
            Oct 11 2020,Back Squat,6,245
            Oct 05 2020,Barbell Bench Press,4,45
        """.trimIndent().toSource()
        val repo = HistoricalDataMemRepo()
        assertEquals(Ok(Unit), importHistoricalData(input, repo))

        val expectedData = listOf(
            WorkoutSet(
                exercise = Exercise("Back Squat"),
                reps = Reps(6),
                weight = Weight(245.0, WeightUnit.Pounds),
                date = LocalDate(2020, 10, 11),
            ),
            WorkoutSet(
                exercise = Exercise("Barbell Bench Press"),
                reps = Reps(4),
                weight = Weight(45.0, WeightUnit.Pounds),
                date = LocalDate(2020, 10, 5),
            ),
        )

        repo.getWorkoutSets().also { data ->
            assertTrue(data.containsAll(expectedData))
            assertEquals(expectedData.size, data.size)
        }
    }

    @Test
    fun `Import fail`() = runTest {
        assertTrue(
            importHistoricalData(
                """
                Oct 11 2020,Back Squat,6,245,t
                """.trimIndent().toSource(),
                HistoricalDataMemRepo()
            ).isErr
        )

        assertTrue(
            importHistoricalData(
                """
                Oct 11 2020,Back Squat,6,245
                Oct 11 2020,Back Squat,6
                """.trimIndent().toSource(),
                HistoricalDataMemRepo()
            ).isErr
        )
    }
}
