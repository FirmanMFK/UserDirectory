package com.firman.directoryuser.feature.user.presentation.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.firman.directoryuser.feature.user.domain.model.User
import com.firman.directoryuser.feature.user.presentation.components.EmptyState
import com.firman.directoryuser.feature.user.presentation.components.ErrorState
import com.firman.directoryuser.feature.user.presentation.components.FilterSection
import com.firman.directoryuser.feature.user.presentation.components.SearchBar
import com.firman.directoryuser.feature.user.presentation.components.UserCard
import com.firman.directoryuser.feature.user.presentation.components.UserCardSkeleton
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen(
    isDarkMode: Boolean,
    onThemeToggle: () -> Unit,
    onUserClick: (User) -> Unit,
    onAddUserClick: () -> Unit,
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
                onClick = onAddUserClick,
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

            when {
                (state.isLoading || state.isRefreshing) && state.users.isEmpty() -> {
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
                state.error != null && state.users.isEmpty() -> {
                    ErrorState(onRetry = viewModel::onRefresh)
                }
                state.users.isEmpty() && !state.isLoading && !state.isRefreshing -> {
                    EmptyState(onClearFilters = viewModel::onClearFilters)
                }
                else -> {
                    PullToRefreshBox(
                        isRefreshing = state.isRefreshing,
                        onRefresh = viewModel::onRefresh,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        if (state.isRefreshing) {
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
                                items(state.users) { user ->
                                    UserCard(
                                        user = user,
                                        onDetailClick = onUserClick
                                    )
                                }
                                if (state.isLoading && state.users.isNotEmpty()) {
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
