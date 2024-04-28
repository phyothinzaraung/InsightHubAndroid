package edu.miu.cs489.insightHub.view.util

sealed class Screen(val route: String) {
    data object HomeScreen : Screen("home")

    data object SearchScreen: Screen("search")

    data object BookmarkScreen: Screen("bookmark")

    data object ProfileScreen: Screen("profile")

    data object SignInScreen: Screen("signIn")

    data object SignUpScreen: Screen("signUp")

    data object AddContentScreen: Screen("addContent")

    data object ContentDetailsScreen: Screen("contentDetails")

    data object EditContentScreen: Screen("editContent")
}