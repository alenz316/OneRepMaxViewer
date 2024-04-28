package com.tonylenz.orm.business

import com.tonylenz.orm.model.Exercise
import com.tonylenz.orm.model.Reps
import com.tonylenz.orm.model.Weight
import com.tonylenz.orm.model.WeightUnit
import com.tonylenz.orm.model.WorkoutSet
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char

// ex. "Nov 26 2019"
private val dateFormat = LocalDate.Format {
    monthName(MonthNames.ENGLISH_ABBREVIATED)
    char(' ')
    dayOfMonth()
    char(' ')
    year()
}

/**
 * Parses a [WorkoutSet] from the given [entry] that follows the following format:
 * "Date of workout, Exercise Name, Reps, Weight"
 */
fun parseHistoricalEntry(entry: String): WorkoutSet {
    val elements = entry.split(",")
    require(elements.size == 4) { "Invalid entry format.\n Found $entry\n Expected Date of workout, Exercise Name, Reps, Weight" }

    return WorkoutSet(
        exercise = Exercise(elements[1]),
        reps = Reps(elements[2].toInt()),
        weight = Weight(elements[3].toDouble(), WeightUnit.Pounds), // Assumes pounds
        date = LocalDate.parse(elements[0], dateFormat)
    )
}
