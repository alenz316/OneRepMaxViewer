package com.tonylenz.orm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tonylenz.orm.business.usecase.OneRepMax
import com.tonylenz.orm.business.usecase.getTheoreticalOneRepMaxes
import com.tonylenz.orm.business.usecase.importHistoricalData
import com.tonylenz.orm.data.HistoricalDataMemRepo
import com.tonylenz.orm.data.HistoricalDataRepo
import com.tonylenz.orm.model.Brzycki
import com.tonylenz.orm.ui.state.ContentUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okio.Source

class MainViewModel : ViewModel() {

    private val _contentUiState = MutableStateFlow<ContentUiState>(ContentUiState.Loading)
    val contentUiState: StateFlow<ContentUiState> = _contentUiState

    private var cachedListState: ContentUiState.ExerciseMaxesList? = null

    private val repo: HistoricalDataRepo = HistoricalDataMemRepo()

    init {
        viewModelScope.launch {
            delay(2000) // So we can see the spinner
            loadData()
        }
    }

    fun importData(source: Source) {
        viewModelScope.launch {
            _contentUiState.value = ContentUiState.Loading
            val result = importHistoricalData(source, repo)
            if (result.isErr) {
                _contentUiState.value = ContentUiState.Error
            } else {
                loadData()
            }
        }
    }

    private suspend fun loadData() {
        val result = getTheoreticalOneRepMaxes(repo, Brzycki)
        if (result.isErr) {
            _contentUiState.value = ContentUiState.Error
        } else {
            if (result.value.isEmpty()) {
                _contentUiState.value = ContentUiState.NoData
            } else {
                val listState = ContentUiState.ExerciseMaxesList(result.value)
                cachedListState = listState
                _contentUiState.value = listState
            }
        }
    }

    fun back() {
        cachedListState.let { listState ->
            if (listState != null) {
                _contentUiState.value = listState
            } else {
                _contentUiState.value = ContentUiState.Loading
                viewModelScope.launch { loadData() }
            }
        }
    }

    fun showDetails(oneRepMax: OneRepMax) {
        // TODO: Dynamically query a date range
        _contentUiState.value.let { state ->
            require(state is ContentUiState.ExerciseMaxesList)
            _contentUiState.value = ContentUiState.ExerciseMaxDetails(oneRepMax)
        }
    }
}
