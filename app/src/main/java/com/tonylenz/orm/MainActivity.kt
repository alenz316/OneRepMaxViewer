package com.tonylenz.orm

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.compose.AppTheme
import com.tonylenz.orm.ui.MainContent
import com.tonylenz.orm.ui.state.ContentUiState
import com.tonylenz.orm.viewmodel.MainViewModel
import okio.source

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                contentResolver.openInputStream(it)?.use { stream ->
                    viewModel.importData(stream.source())
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val uiState by viewModel.contentUiState.collectAsState()
            BackHandler(uiState is ContentUiState.ExerciseMaxDetails) {
                viewModel.back()
            }

            AppTheme {
                Scaffold(
                    topBar = { TopBar() },
                    floatingActionButton = {
                        Fab(uiState) {
                            getContent.launch("text/plain")
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                ) { paddingValues ->
                    MainContent(
                        paddingValues = paddingValues,
                        uiState = uiState,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    CenterAlignedTopAppBar(
        title = {
            Text(text = stringResource(id = R.string.app_name))
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onSecondary,
        ),
    )
}

@Composable
fun Fab(
    uiState: ContentUiState,
    onClick: () -> Unit,
) {
    if (uiState !is ContentUiState.Loading) {
        FloatingActionButton(
            onClick = { onClick() },
        ) {
            Icon(Icons.Filled.Add, "Import data button")
        }
    }
}
