package com.firman.directoryuser.feature.user.presentation

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.firman.directoryuser.feature.user.presentation.components.*
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen(
    isDarkMode: Boolean,
    onThemeToggle: () -> Unit,
    viewModel: UserViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val listState = rememberLazyListState()

    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull() ?: return@derivedStateOf false
            lastVisibleItem.index >= listState.layoutInfo.totalItemsCount - 2
        }
    }

    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value) {
            viewModel.loadNextPage()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("User Directory", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = onThemeToggle) {
                        Icon(
                            imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = "Toggle Theme"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* TODO: Add user */ },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add User")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            SearchBar(
                query = state.searchQuery,
                onQueryChange = viewModel::onSearchQueryChange,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            FilterSection(
                cities = state.cities,
                selectedCity = state.selectedCity,
                onCitySelected = viewModel::onCityFilterChange,
                isAscending = state.isAscending,
                onSortToggle = viewModel::onSortChange,
                onClearFilters = viewModel::onClearFilters
            )

            Spacer(modifier = Modifier.height(16.dp))

            Crossfade(
                targetState = state,
                animationSpec = tween(durationMillis = 500),
                label = "ScreenState"
            ) { targetState ->
                when {
                    (targetState.isLoading || targetState.isRefreshing) && targetState.users.isEmpty() -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            contentPadding = PaddingValues(bottom = 80.dp)
                        ) {
                            items(5) {
                                UserCardSkeleton()
                            }
                        }
                    }
                    targetState.error != null && targetState.users.isEmpty() -> {
                        ErrorState(onRetry = viewModel::onRefresh)
                    }
                    targetState.users.isEmpty() && !targetState.isLoading && !targetState.isRefreshing -> {
                        EmptyState(onClearFilters = viewModel::onClearFilters)
                    }
                    else -> {
                        PullToRefreshBox(
                            isRefreshing = targetState.isRefreshing,
                            onRefresh = viewModel::onRefresh,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Crossfade(
                                targetState = targetState.isRefreshing,
                                animationSpec = tween(durationMillis = 400),
                                label = "RefreshTransition"
                            ) { isRefreshing ->
                                if (isRefreshing) {
                                    LazyColumn(
                                        modifier = Modifier.fillMaxSize(),
                                        verticalArrangement = Arrangement.spacedBy(16.dp),
                                        contentPadding = PaddingValues(bottom = 80.dp)
                                    ) {
                                        items(5) {
                                            UserCardSkeleton()
                                        }
                                    }
                                } else {
                                    LazyColumn(
                                        state = listState,
                                        modifier = Modifier.fillMaxSize(),
                                        verticalArrangement = Arrangement.spacedBy(16.dp),
                                        contentPadding = PaddingValues(bottom = 80.dp)
                                    ) {
                                        items(targetState.users) { user ->
                                            UserCard(user = user)
                                        }
                                        if (targetState.isLoading && targetState.users.isNotEmpty()) {
                                            item {
                                                Box(
                                                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
