package com.tonylenz.orm.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.compose.AppTheme
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.tonylenz.orm.business.usecase.OneRepMax
import com.tonylenz.orm.model.Exercise
import com.tonylenz.orm.model.Weight
import com.tonylenz.orm.model.WeightUnit
import kotlinx.datetime.LocalDate

@Composable
fun ExerciseMaxDetails(
    oneRepMax: OneRepMax,
) {
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        ExerciseMaxItem(itemData = oneRepMax)
        AndroidView(
            factory = { context ->
                LineChart(context).apply {
                    val lineEntries = oneRepMax.dailyOneRepMaxes.map {
                        val (date, max) = it
                        Entry(date.toEpochDays().toFloat(), max.value)
                    }
                    val dataset = LineDataSet(lineEntries, "Label")

                    data = LineData(dataset)
                    invalidate()
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .height(300.dp)
        )
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
