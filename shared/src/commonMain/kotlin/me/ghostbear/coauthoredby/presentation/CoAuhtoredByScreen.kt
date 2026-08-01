package me.ghostbear.coauthoredby.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.onClick
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastAny
import coauthoredby.shared.generated.resources.*
import dev.zacsweers.metrox.viewmodel.metroViewModel
import kotlinx.browser.window
import me.ghostbear.coauthoredby.core.Optional
import me.ghostbear.coauthoredby.core.icons.GitHub
import me.ghostbear.coauthoredby.core.icons.Icons
import me.ghostbear.coauthoredby.core.icons.delete
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource

@Composable
fun CoAuthoredByScreen() {
    val viewModel = metroViewModel<CoAuthoredByViewModel>()

    val state by viewModel.state.collectAsState()
    val events = viewModel.events

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is CoAuthorByEvent.Copied -> {
                    snackbarHostState.showSnackbar(getString(Res.string.commit_message_copied))
                }

                is CoAuthorByEvent.Error -> {
                    snackbarHostState.showSnackbar(
                        getString(Res.string.error_with_message, event.message),
                        duration = SnackbarDuration.Long
                    )
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.app_name)) },
                actions = {
                    IconButton(onClick = { window.open("https://github.com/ghostbear/coauthoredby") }) {
                        Icon(Icons.Filled.GitHub, contentDescription = null)
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .widthIn(max = 720.dp)
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Text(stringResource(Res.string.app_description))
            }
            item {
                Text(
                    text = stringResource(Res.string.question_co_authors),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
            }
            items(
                items = state.coauthors,
                key = { it.id }
            ) { coauthor ->
                val focusManager = LocalFocusManager.current
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = coauthor.name,
                        onValueChange = { value ->
                            viewModel.onIntent(CoAuthorByIntent.Update(coauthor.id, value))
                        },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .focusRequester(FocusRequester.Default)
                            .onFocusChanged { focusState ->
                                if (focusState.isFocused) {
                                    viewModel.onIntent(CoAuthorByIntent.Focus(coauthor.id))
                                } else {
                                    viewModel.onIntent(CoAuthorByIntent.Unfocus)
                                }
                            }
                            .onPreviewKeyEvent { keyEvent ->
                                if (keyEvent.key == Key.Tab || keyEvent.key == Key.Enter) {
                                    focusManager.moveFocus(FocusDirection.Down)
                                    return@onPreviewKeyEvent true
                                }
                                return@onPreviewKeyEvent false
                            }
                    )
                    IconButton(
                        onClick = { viewModel.onIntent(CoAuthorByIntent.Delete(coauthor.id)) },
                        enabled = coauthor.name.isNotBlank()
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.delete,
                            contentDescription = stringResource(Res.string.action_delete)
                        )
                    }
                }
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
                ) {
                    TextButton(
                        onClick = { viewModel.onIntent(CoAuthorByIntent.Reset) },
                        enabled = state.coauthors.fastAny { it.name.isNotBlank() } && !state.isGenerating
                    ) {
                        Text(stringResource(Res.string.action_reset))
                    }
                    Button(
                        onClick = { viewModel.onIntent(CoAuthorByIntent.Submit) },
                        enabled = state.coauthors.fastAny { it.name.isNotBlank() } && !state.isGenerating
                    ) {
                        Text(text = stringResource(Res.string.action_generate))
                    }
                }
            }
        }

        when (val commitMessage = state.commitMessage) {
            Optional.None -> {

            }

            is Optional.Some<String> -> {
                AlertDialog(
                    onDismissRequest = {
                        viewModel.onIntent(CoAuthorByIntent.Dismiss)
                    },
                    confirmButton = {
                        Button(onClick = { viewModel.onIntent(CoAuthorByIntent.Copy) }) {
                            Text(text = stringResource(Res.string.action_copy))
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { viewModel.onIntent(CoAuthorByIntent.Dismiss) }) {
                            Text(stringResource(Res.string.action_cancel))
                        }
                    },
                    title = { Text(stringResource(Res.string.commit_message)) },
                    text = {
                        CommitMessageCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 196.dp),
                            value = commitMessage.value,
                            onClick = { viewModel.onIntent(CoAuthorByIntent.Copy) }
                        )
                    }
                )
            }
        }

    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun CommitMessageCard(
    modifier: Modifier,
    value: String,
    onClick: () -> Unit
) {
    Card(modifier = modifier.onClick(onClick = onClick)) {
        Text(
            text = value,
            modifier = Modifier.padding(8.dp),
        )
    }
}
