package com.vholodynskyi.assignment.presentation.navigation

sealed class Screen(val route: String) {
    object ContactListScreen : Screen("contacts_list_screen")
    object DetailsScreen : Screen("details_screen")
}