package com.tonylenz.orm.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.compose.AppTheme
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.tonylenz.orm.R
import com.tonylenz.orm.business.usecase.OneRepMax
import com.tonylenz.orm.model.Exercise
import com.tonylenz.orm.model.Weight
import com.tonylenz.orm.model.WeightUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char


@Composable
fun ExerciseMaxDetails(
    oneRepMax: OneRepMax,
) {
    val lineColor = MaterialTheme.colorScheme.secondary
    val gridColor = MaterialTheme.colorScheme.outlineVariant
    val graphTextColor = MaterialTheme.colorScheme.onPrimaryContainer
    val poundsPostFix = " ${stringResource(id = R.string.pounds_short)}"
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        ExerciseMaxItem(itemData = oneRepMax)
        AndroidView(
            factory = { context ->
                LineChart(context).apply {
                    legend.isEnabled = false
                    description.isEnabled = false

                    val lineEntries = oneRepMax.dailyOneRepMaxes.map {
                        val (date, max) = it
                        Entry(date.toEpochDays().toFloat(), max.value)
                    }
                    val dataset = LineDataSet(lineEntries, "")
                    dataset.color = lineColor.toArgb()
                    dataset.circleColors = listOf(lineColor.toArgb())
                    dataset.setDrawValues(false)
                    data = LineData(dataset)

                    xAxis.isEnabled = true
                    xAxis.position = XAxis.XAxisPosition.BOTTOM
                    xAxis.valueFormatter = object : ValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            return LocalDate.fromEpochDays(value.toInt()).format(dateFormat)
                        }
                    }
                    xAxis.textColor = graphTextColor.toArgb()
                    xAxis.setGranularity(1.0f)
                    xAxis.gridColor = gridColor.toArgb()
                    xAxis.axisLineColor = gridColor.toArgb()

                    axisRight.isEnabled = false
                    axisLeft.textColor = graphTextColor.toArgb()
                    val defaultFormatter = axisLeft.valueFormatter
                    axisLeft.valueFormatter = object : ValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            return "${defaultFormatter.getFormattedValue(value)} $poundsPostFix"
                        }
                    }
                    axisLeft.gridColor = gridColor.toArgb()
                    axisLeft.axisLineColor = gridColor.toArgb()

                    invalidate()
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .height(300.dp)
        )
    }
}

// ex. "Nov 26 2019"
private val dateFormat = LocalDate.Format {
    monthName(MonthNames.ENGLISH_ABBREVIATED)
    char(' ')
    dayOfMonth()
}


@Preview
@Composable
private fun Preview() {
    AppTheme {
        Surface {
            ExerciseMaxDetails(
                oneRepMax = OneRepMax(
                    exercise = Exercise("Deadlift"),
                    weight = Weight(580.0f, WeightUnit.Pounds),
                    dailyOneRepMaxes = listOf(
                        LocalDate(2020, 6, 6) to Weight(580.0f, WeightUnit.Pounds),
                        LocalDate(2020, 6, 7) to Weight(124.13793103448276f, WeightUnit.Pounds),
                        LocalDate(2020, 6, 8) to Weight(163.125f, WeightUnit.Pounds),
                    )
                )
            )
        }
    }
}
