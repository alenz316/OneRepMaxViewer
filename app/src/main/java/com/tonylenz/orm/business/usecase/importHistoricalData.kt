package com.tonylenz.orm.business.usecase

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.tonylenz.orm.business.parseHistoricalEntry
import com.tonylenz.orm.data.HistoricalDataRepo
import com.tonylenz.orm.model.WorkoutSet
import okio.Source
import okio.buffer

/**
 * The import data use-case. Unspecified functional requirements around subsequent imports, I chose
 * to treat the application as a historical data file viewer. e.g. The app only allows you to see
 * the historical data from one file at a time.
 */
@Suppress("NAME_SHADOWING") // allow name shadowing to restrict arg scope
suspend fun importHistoricalData(
    data: Source,
    repo: HistoricalDataRepo,
): Result<Unit, Exception> {
    return try {
        data.buffer().use { data ->
            val dataList = mutableListOf<WorkoutSet>()
            var line = data.readUtf8Line()
            while (line != null) {
                dataList.add(parseHistoricalEntry(line))
                line = data.readUtf8Line()
            }
            repo.saveWorkoutSets(dataList, replaceAll = true)
        }
        Ok(Unit)
    } catch (e: Exception) {
        Err(e)
    }
}
