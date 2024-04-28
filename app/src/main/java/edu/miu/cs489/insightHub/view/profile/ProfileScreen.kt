package edu.miu.cs489.insightHub.view.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import edu.miu.cs489.insightHub.R
import edu.miu.cs489.insightHub.data.User
import edu.miu.cs489.insightHub.view.util.BottomNavigationBar
import edu.miu.cs489.insightHub.view.util.Screen
import edu.miu.cs489.insightHub.viewmodel.UserSignInViewModel
import edu.miu.cs489.insightHub.viewmodel.UserViewModel

@Composable
fun ProfileScreen(navController: NavHostController, userViewModel: UserViewModel, signInViewModel: UserSignInViewModel) {

    val logout = {
        userViewModel.clearPreference()
        userViewModel.clearPreference()
        signInViewModel.resetSignInState()
        navController.navigate(Screen.SignInScreen.route) {
            popUpTo(navController.graph.startDestinationId) {
                inclusive = true
            }
        }
    }

    
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController, currentRoute = Screen.ProfileScreen.route)
        }
    ) { innerPadding -> 
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {

            var user by remember {
                mutableStateOf<User>(User())
            }
            LaunchedEffect(Unit){
                userViewModel.getUserById()
            }
            userViewModel.userState.observeForever {
                when(it){
                    is UserViewModel.UserState.Loading -> {

                    }

                    is UserViewModel.UserState.Success -> {
                        user = it.user
                    }

                    is UserViewModel.UserState.Error -> {
                        it.message
                    }

                    is UserViewModel.UserState.Initial -> {

                    }
                }
            }

            val logoPainter: Painter = painterResource(id = R.drawable.insight)
            Image(
                painter = logoPainter,
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(100.dp)
            )

            Text(
                text = "Insight Hub",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Text(
                text = "${user.firstName} ${user.lastName}",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = user.description,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = user.email,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Button(
                onClick = logout,
                shape = RoundedCornerShape(12.dp), // Rounded corners
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                )
            ) {
                Text(text = "Logout")
            }
        }

    }
    
}