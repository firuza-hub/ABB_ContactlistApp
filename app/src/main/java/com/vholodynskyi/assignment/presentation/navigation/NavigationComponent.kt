package com.vholodynskyi.assignment.presentation.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.vholodynskyi.assignment.presentation.contactslist.ContactListScreen
import com.vholodynskyi.assignment.presentation.details.DetailsScreen
import kotlinx.coroutines.delay

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
            deepLinks = listOf(navDeepLink {
                uriPattern =
                    "abb_contacts://" + Screen.DetailsScreen.route + "?contactId={contactId}"
            })
        ) {
            EnterAnimation {
                DetailsScreen(
                    id = it.arguments?.getString("contactId") ?: "-1",
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun EnterAnimation(content: @Composable () -> Unit) {

    var state by remember { mutableStateOf(false)}
    LaunchedEffect(key1 = true){
        state = true
    }
    AnimatedVisibility(
        visible = state,
        enter = slideInVertically(
            initialOffsetY = { -500 },
            animationSpec = tween(durationMillis = 2000, easing = LinearOutSlowInEasing)
        )
    ) { content() }
}