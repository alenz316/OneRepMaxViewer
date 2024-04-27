package com.tonylenz.orm.business

import com.tonylenz.orm.model.Reps
import com.tonylenz.orm.model.Weight
import java.lang.IllegalArgumentException

fun calculateBrzyckiOneRepMax(weight: Weight, reps: Reps): Weight {
    if (reps.count <= 1 ) throw IllegalArgumentException("Need more than 1 rep to calculate ORM")

    return Weight(
        value = weight.value * 36 / (37 - reps.count),
        unit = weight.unit,
    )
}


