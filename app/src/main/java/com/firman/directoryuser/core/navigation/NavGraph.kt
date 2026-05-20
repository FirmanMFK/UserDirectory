package com.firman.directoryuser.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.firman.directoryuser.feature.user.domain.model.User
import com.firman.directoryuser.feature.user.presentation.UserDetailScreen
import com.firman.directoryuser.feature.user.presentation.UserScreen
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Serializable
object UserListRoute

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
        composable<UserListRoute> {
            UserScreen(
                isDarkMode = isDarkMode,
                onThemeToggle = onThemeToggle,
                onUserClick = { user ->
                    navController.navigate(UserDetailRoute(user))
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
