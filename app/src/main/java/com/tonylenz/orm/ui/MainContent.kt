package com.tonylenz.orm.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.tonylenz.orm.R
import com.tonylenz.orm.business.usecase.OneRepMax
import com.tonylenz.orm.ui.state.ContentUiState

@Composable
fun MainContent(
    paddingValues: PaddingValues,
    uiState: ContentUiState,
    onItemClick: (item: OneRepMax) -> Unit
) {
    val alignment: Alignment = when (uiState) {
        is ContentUiState.ExerciseMaxDetails -> Alignment.TopStart
        is ContentUiState.ExerciseMaxesList -> Alignment.TopStart
        else -> Alignment.Center
    }
    // TODO: Add transition animations
    Box(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .wrapContentSize(alignment)
    ) {
        when (val state = uiState) {
            ContentUiState.Error -> Text(text = stringResource(id = R.string.generic_error_message))
            is ContentUiState.ExerciseMaxDetails -> Text(text = state.max.exercise.name)
            is ContentUiState.ExerciseMaxesList ->
                ExerciseMaxList(
                    oneRepMaxes = state.exerciseMaxes,
                    onClick = onItemClick
                )

            ContentUiState.Loading -> CircularProgressIndicator()
            ContentUiState.NoData -> Text(text = stringResource(id = R.string.no_data_message))
        }

    }
}
