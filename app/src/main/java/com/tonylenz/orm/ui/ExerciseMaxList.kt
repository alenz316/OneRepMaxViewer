package com.tonylenz.orm.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.AppTheme
import com.tonylenz.orm.business.usecase.OneRepMax
import com.tonylenz.orm.model.Exercise
import com.tonylenz.orm.model.Weight
import com.tonylenz.orm.model.WeightUnit
import kotlinx.datetime.LocalDate

@Composable
fun ExerciseMaxList(
    oneRepMaxes: List<OneRepMax>,
    onClick: ((item: OneRepMax) -> Unit)? = null,
) {
    LazyColumn {
        itemsIndexed(oneRepMaxes, { _, item -> item.exercise.name }) { index, item ->
            ExerciseMaxItem(itemData = item, onClick = onClick)
            if (index < oneRepMaxes.size - 1) {
                Divider(modifier = Modifier.padding(vertical = 8.dp))
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        Surface {
            ExerciseMaxList(
                oneRepMaxes = listOf(
                    OneRepMax(
                        exercise = Exercise("Back Squat"),
                        weight = Weight(407.64706f, WeightUnit.Pounds),
                        dailyOneRepMaxes = listOf(
                            LocalDate(2020, 6, 4) to Weight(407.64706f, WeightUnit.Pounds),
                        )
                    ),
                    OneRepMax(
                        exercise = Exercise("Deadlift"),
                        weight = Weight(580.0f, WeightUnit.Pounds),
                        dailyOneRepMaxes = listOf(
                            LocalDate(2020, 6, 6) to Weight(580.0f, WeightUnit.Pounds),
                            LocalDate(2020, 6, 7) to Weight(124.13793103448276f, WeightUnit.Pounds),
                            LocalDate(2020, 6, 8) to Weight(163.125f, WeightUnit.Pounds),
                        )
                    )
                )
            )
        }
    }
}
