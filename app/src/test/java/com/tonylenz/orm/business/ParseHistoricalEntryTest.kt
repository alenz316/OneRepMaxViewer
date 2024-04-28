package com.tonylenz.orm.business

import com.tonylenz.orm.model.Exercise
import com.tonylenz.orm.model.Reps
import com.tonylenz.orm.model.Weight
import com.tonylenz.orm.model.WeightUnit
import com.tonylenz.orm.model.WorkoutSet
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class ParseHistoricalEntryTest {

    @Test
    fun `Parse historical entry - success`() {
        assertEquals(
            WorkoutSet(
                exercise = Exercise("Back Squat"),
                reps = Reps(3),
                weight = Weight(285.0, WeightUnit.Pounds),
                date = LocalDate(2020, 6, 4),
            ),
            parseHistoricalEntry("Jun 04 2020,Back Squat,3,285")
        )

        assertEquals(
            WorkoutSet(
                exercise = Exercise("Back Squat"),
                reps = Reps(6),
                weight = Weight(245.0, WeightUnit.Pounds),
                date = LocalDate(2020, 10, 11),
            ),
            parseHistoricalEntry("Oct 11 2020,Back Squat,6,245")
        )
    }

    @Test
    fun `Invalid format - failure`() {

        assertFails {
            parseHistoricalEntry("Jun 04 2020,Back Squat,3,285,3")
        }

        assertFails {
            parseHistoricalEntry("June 04 2020,Back Squat,3,285")
        }

        assertFails {
            parseHistoricalEntry("Jun 04 2020,Back Squat,-3,285")
        }

    }
}
