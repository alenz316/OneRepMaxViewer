package com.tonylenz.orm.business.usecase

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.tonylenz.orm.business.parseHistoricalEntry
import com.tonylenz.orm.data.HistoricalDataRepo
import com.tonylenz.orm.model.WorkoutSet
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.ensureActive
import okio.Source
import okio.buffer

/**
 * The import data use-case. Unspecified functional requirements around subsequent imports, I chose
 * to treat the application as a historical data file viewer. e.g. The app only allows you to see
 * the historical data from one file at a time.
 *
 * // TODO: Allow cancelling import in the UI
 */
suspend fun importHistoricalData(
    data: Source,
    repo: HistoricalDataRepo,
): Result<Unit, Exception> = coroutineScope{
    try {
        data.buffer().use { data ->
            val dataList = mutableListOf<WorkoutSet>()
            var line = data.readUtf8Line()
            while (line != null) {
                dataList.add(parseHistoricalEntry(line))
                ensureActive() // Allow cancellation
                line = data.readUtf8Line()
            }
            repo.saveWorkoutSets(dataList, replaceAll = true)
        }
        Ok(Unit)
    } catch (e: CancellationException) {
        throw e // Allow coroutine cancellation
    } catch (e: Exception) {
        Err(e)
    }
}
