package com.tonylenz.orm.model

import kotlinx.datetime.LocalDate

enum class OneRepMaxEstimateStrategy {
    Brzycki, // Add more estimating strategies here
}

@JvmInline
value class Exercise(val name: String)

@JvmInline
value class Reps(val count: Int) {
    init {
        if (count < 0) throw IllegalArgumentException("Negative reps don't make sense: $count")
    }
}

enum class WeightUnit {
    Pounds
}

// TODO: Refactor `Weight` to be more ergonomic. e.g. 15.lb and 25.kg. (Similar to compose .db and .sp)
data class Weight(val value: Double, val unit: WeightUnit) {
    init {
        if (value < 0) throw IllegalArgumentException("Negative weight doesn't make sense: $value")
    }
}

data class WorkoutSet(
    val exercise: Exercise,
    val reps: Reps,
    val weight: Weight,
    val date: LocalDate,
)
