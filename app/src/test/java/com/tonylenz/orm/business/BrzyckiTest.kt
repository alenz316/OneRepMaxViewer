package com.tonylenz.orm.business

import com.tonylenz.orm.model.Brzycki
import com.tonylenz.orm.model.Reps
import com.tonylenz.orm.model.Weight
import com.tonylenz.orm.model.WeightUnit.Pounds
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

// Expected values calculated with https://www.vcalc.com/wiki/brzycki
class BrzyckiTest {

    @Test
    fun `Standard calculations`() {
        assertEquals(
            Weight(112.5f, Pounds),
            Brzycki.calculateTheoreticalOneRepMax(
                Weight(100.0f, Pounds),
                Reps(5)
            )
        )

        assertEquals(
            Weight(112.5f, Pounds),
            Brzycki.calculateTheoreticalOneRepMax(
                Weight(100.0f, Pounds),
                Reps(5)
            )
        )
    }

    @Test
    fun `Fail 1 rep`() {
        assertFailsWith(IllegalArgumentException::class) {
            Brzycki.calculateTheoreticalOneRepMax(
                Weight(100.0f, Pounds),
                Reps(1)
            )
        }
    }

    @Test
    fun `ORM 0 weight`() {
        // Silly but acceptable
        assertEquals(
            Weight(0.0f, Pounds),
            Brzycki.calculateTheoreticalOneRepMax(
                Weight(0.0f, Pounds),
                Reps(5)
            )
        )
    }

}
