package edu.miu.cs489.insightHub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import edu.miu.cs489.insightHub.ui.theme.InsightHubTheme
import edu.miu.cs489.insightHub.view.bookmark.BookmarkScreen
import edu.miu.cs489.insightHub.view.content.AddContentScreen
import edu.miu.cs489.insightHub.view.content.ContentDetailsScreen
import edu.miu.cs489.insightHub.view.content.EditContentScreen
import edu.miu.cs489.insightHub.view.home.HomeScreen
import edu.miu.cs489.insightHub.view.profile.ProfileScreen
import edu.miu.cs489.insightHub.view.util.Screen
import edu.miu.cs489.insightHub.view.search.SearchScreen
import edu.miu.cs489.insightHub.view.signIn.SignInScreen
import edu.miu.cs489.insightHub.view.signUp.SignUpScreen
import edu.miu.cs489.insightHub.viewmodel.CategoryViewModel
import edu.miu.cs489.insightHub.viewmodel.ContentViewModel
import edu.miu.cs489.insightHub.viewmodel.UserSignInViewModel
import edu.miu.cs489.insightHub.viewmodel.UserSignUpViewModel
import edu.miu.cs489.insightHub.viewmodel.UserViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController
    private val signInViewModel: UserSignInViewModel by viewModels()
    private val signUpViewModel: UserSignUpViewModel by viewModels()
    private val categoryViewModel: CategoryViewModel by viewModels()
    private val contentViewModel: ContentViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InsightHubTheme {
                navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }

    @Composable
    fun NavGraph(navController: NavHostController) {
        NavHost(
            navController = navController,
            startDestination = Screen.SignInScreen.route
        ) {

            composable(Screen.HomeScreen.route) {
                HomeScreen(navController = navController, contentViewModel)
            }

            composable(Screen.SearchScreen.route) {
                SearchScreen(navController = navController, contentViewModel)
            }

            composable(Screen.BookmarkScreen.route) {
                BookmarkScreen(navController = navController, contentViewModel)
            }

            composable(Screen.ProfileScreen.route) {
                ProfileScreen(navController = navController, userViewModel, signInViewModel)
            }

            composable(Screen.SignInScreen.route) {
                SignInScreen(navController = navController, viewModel = signInViewModel)
            }

            composable(Screen.SignUpScreen.route) {
                SignUpScreen(navController = navController, viewModel = signUpViewModel)
            }

            composable(Screen.AddContentScreen.route) {
                AddContentScreen(navController = navController, categoryViewModel, contentViewModel)
            }

            composable(Screen.ContentDetailsScreen.route) {
                ContentDetailsScreen(navController = navController, contentViewModel, categoryViewModel)
            }

            composable(Screen.EditContentScreen.route) {
                    EditContentScreen(navController = navController, contentViewModel)
                }

        }
    }
}