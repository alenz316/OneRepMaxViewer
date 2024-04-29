package com.tonylenz.orm.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.tonylenz.orm.R
import com.tonylenz.orm.ui.state.ContentUiState
import com.tonylenz.orm.viewmodel.MainViewModel

@Composable
fun MainContent(
    paddingValues: PaddingValues,
    viewModel: MainViewModel,
) {
    val uiState by viewModel.contentUiState.collectAsState()
    BackHandler(uiState is ContentUiState.ExerciseMaxDetails) {
        viewModel.back()
    }
    Box(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        when (val state = uiState) {
            ContentUiState.Error -> TODO()
            is ContentUiState.ExerciseMaxDetails -> TODO()
            is ContentUiState.ExerciseMaxesList -> ExerciseMaxList(oneRepMaxes = state.exerciseMaxes) {

            }

            ContentUiState.Loading -> CircularProgressIndicator()
            ContentUiState.NoData -> Text(text = stringResource(id = R.string.no_data_message))
        }

    }
}
