package com.tonylenz.orm.model

data object Brzycki : OneRepMaxEstimateStrategy {
    override fun calculateTheoreticalOneRepMax(weight: Weight, reps: Reps): Weight {
        if (reps.count <= 1) throw IllegalArgumentException("Need more than 1 rep to calculate ORM")

        return Weight(
            value = weight.value * 36 / (37 - reps.count),
            unit = weight.unit,
        )
    }
}
