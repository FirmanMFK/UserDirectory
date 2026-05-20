package com.firman.directoryuser.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.firman.directoryuser.feature.user.domain.model.User
import com.firman.directoryuser.feature.user.presentation.add.AddUserScreen
import com.firman.directoryuser.feature.user.presentation.detail.UserDetailScreen
import com.firman.directoryuser.feature.user.presentation.list.UserScreen
import com.firman.directoryuser.feature.user.presentation.list.UserViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import kotlin.reflect.typeOf

@Serializable
object UserListRoute

@Serializable
object AddUserRoute

@Serializable
data class UserDetailRoute(val user: User)

@Composable
fun NavGraph(
    navController: NavHostController,
    isDarkMode: Boolean,
    onThemeToggle: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = UserListRoute
    ) {
        composable<UserListRoute> { backStackEntry ->
            val viewModel: UserViewModel = koinViewModel()
            val userAdded by backStackEntry.savedStateHandle.getStateFlow("user_added", false).collectAsState()

            LaunchedEffect(userAdded) {
                if (userAdded) {
                    viewModel.onRefresh()
                    backStackEntry.savedStateHandle["user_added"] = false
                }
            }

            UserScreen(
                isDarkMode = isDarkMode,
                onThemeToggle = onThemeToggle,
                onUserClick = { user ->
                    navController.navigate(UserDetailRoute(user))
                },
                onAddUserClick = {
                    navController.navigate(AddUserRoute)
                },
                viewModel = viewModel
            )
        }
        composable<AddUserRoute> {
            AddUserScreen(
                onBackClick = { navController.navigateUp() },
                onSuccess = {
                    navController.previousBackStackEntry?.savedStateHandle?.set("user_added", true)
                    navController.navigateUp()
                }
            )
        }
        composable<UserDetailRoute>(
            typeMap = mapOf(typeOf<User>() to NavCustomType.UserType)
        ) { backStackEntry ->
            val route: UserDetailRoute = backStackEntry.toRoute()
            UserDetailScreen(
                user = route.user,
                onBackClick = { navController.navigateUp() }
            )
        }
    }
}
