package com.vholodynskyi.assignment.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.vholodynskyi.assignment.presentation.contactslist.ContactListScreen
import com.vholodynskyi.assignment.presentation.details.DetailsScreen

@Composable
fun NavigationComponent() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.ContactListScreen.route
    ) {
        composable(route = Screen.ContactListScreen.route) {
            ContactListScreen(navController = navController)
        }
        composable(
            route = Screen.DetailsScreen.route + "?contactId={contactId}",
            arguments = listOf(
                navArgument(
                    name = "contactId"
                ) {
                    type = NavType.StringType
                    defaultValue = "-1"
                }),
            deepLinks = listOf(navDeepLink { uriPattern = "abb_contacts://" + Screen.DetailsScreen.route + "?contactId={contactId}" })
        ) {
            DetailsScreen(
                id = it.arguments?.getString("contactId") ?: "-1",
                navController = navController
            )
        }
    }
}