package edu.miu.cs489.insightHub.view.signIn

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import edu.miu.cs489.insightHub.R
import edu.miu.cs489.insightHub.data.LoginRequest
import edu.miu.cs489.insightHub.view.util.Screen
import edu.miu.cs489.insightHub.viewmodel.UserSignInViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(navController: NavHostController, viewModel: UserSignInViewModel) {

    Scaffold { innerPadding ->

        val isUserExist by viewModel.userExit.collectAsState()
        viewModel.getSaveLoginData()

        if (isUserExist) {
            viewModel.setSignInStateEmpty()
            navController.navigate(Screen.HomeScreen.route)
        }else{
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                var email by remember {
                    mutableStateOf(TextFieldValue(""))
                }

                var password by remember {
                    mutableStateOf(TextFieldValue(""))
                }

                val signInState by viewModel.signInState.collectAsState()
                val context = LocalContext.current


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
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { newValue -> email = newValue },
                    label = { Text(text = "Email") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        disabledContainerColor = MaterialTheme.colorScheme.surface,
                    )
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { newValue -> password = newValue },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        disabledContainerColor = MaterialTheme.colorScheme.surface,
                    )
                )

                Button(
                    onClick = {
                        val errors = mutableListOf<String>()
                        if (email.text.isBlank()) errors.add("Email is required")
                        if (password.text.isBlank()) errors.add("Password is required")
                        if (errors.isEmpty()){
                            viewModel.signIn(
                                LoginRequest(
                                    email = email.text,
                                    password = password.text
                                )
                            )
                        }else{
                            errors.forEach { errorMessage ->
                                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Text(text = "Sign In")
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = {
                        viewModel.setSignInStateEmpty()
                        navController.navigate(Screen.SignUpScreen.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = false
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Sign Up")
                }

                when(signInState){
                    is UserSignInViewModel.SignInState.Loading -> {
                        Log.d("Message", "Loading")

                    }
                    is UserSignInViewModel.SignInState.Success -> {
                        Log.d("Message", "Success")
                        viewModel.setSignInStateEmpty()
                        navController.navigate(Screen.HomeScreen.route)

                    }
                    is UserSignInViewModel.SignInState.Error -> {
                        viewModel.setSignInStateEmpty()
                        Toast.makeText(context, (signInState as UserSignInViewModel.SignInState.Error).message, Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }
            }
        }
    }
}