package com.tonylenz.orm.ui.state

import com.tonylenz.orm.business.usecase.OneRepMax

sealed interface ContentUiState {

    data object NoData : ContentUiState
    data object Loading : ContentUiState
    data object Error : ContentUiState

    data class ExerciseMaxesList(
        val exerciseMaxes: List<OneRepMax>
    ) : ContentUiState

    data class ExerciseMaxDetails(
        val max: OneRepMax
    ) : ContentUiState
}
