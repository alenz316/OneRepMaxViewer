package com.tonylenz.orm.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.AppTheme
import com.tonylenz.orm.R
import com.tonylenz.orm.business.usecase.OneRepMax
import com.tonylenz.orm.model.Exercise
import com.tonylenz.orm.model.Weight
import com.tonylenz.orm.model.WeightUnit
import kotlinx.datetime.LocalDate

@Composable
fun ExerciseMaxItem(
    itemData: OneRepMax,
    onClick: ((item: OneRepMax) -> Unit)? = null,
) {
    Column(
        modifier = Modifier
            .let {
                // Only make clickable if there is an onClick
                if (onClick != null) it.clickable {
                    onClick(itemData)
                } else it
            }
            .padding(8.dp)
    ) {
        Row {
            Text(text = itemData.exercise.name)
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "%.0f".format(itemData.weight.value))
        }
        Spacer(modifier = Modifier.padding(2.dp))
        Row {
            Text(
                text = stringResource(id = R.string.one_rep_max_record),
                style = MaterialTheme.typography.bodySmall,
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = itemData.weight.unit.toUiString(),
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Composable
fun WeightUnit.toUiString() = when (this) {
    WeightUnit.Pounds -> stringResource(id = R.string.pounds_short)
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        Surface {
            ExerciseMaxItem(
                itemData = OneRepMax(
                    exercise = Exercise("Back Squat"),
                    weight = Weight(407.65f, WeightUnit.Pounds),
                    dailyOneRepMaxes = listOf(
                        LocalDate(2020, 6, 4) to Weight(407.65f, WeightUnit.Pounds),
                    )
                )
            )
        }
    }
}
