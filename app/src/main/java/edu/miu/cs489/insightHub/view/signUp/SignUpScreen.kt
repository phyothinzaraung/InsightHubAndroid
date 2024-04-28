package edu.miu.cs489.insightHub.view.signUp

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import edu.miu.cs489.insightHub.data.User
import edu.miu.cs489.insightHub.view.util.Screen
import edu.miu.cs489.insightHub.viewmodel.UserSignUpViewModel

@Composable
fun SignUpScreen(navController: NavHostController, viewModel: UserSignUpViewModel) {

    var firstName by remember {
        mutableStateOf(TextFieldValue(""))
    }
    var lastName by remember {
        mutableStateOf(TextFieldValue(""))
    }
    var email by remember {
        mutableStateOf(TextFieldValue(""))
    }
    var password by remember {
        mutableStateOf(TextFieldValue(""))
    }

    var description by remember {
        mutableStateOf(TextFieldValue(""))
    }

    val signUpState by viewModel.signUpState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(signUpState) {
        when (signUpState) {
            is UserSignUpViewModel.SignUpState.Loading -> {
                // Handle loading state
            }
            is UserSignUpViewModel.SignUpState.Success -> {
                viewModel.setSignUpStateEmpty()
                navController.navigate(Screen.SignInScreen.route)
            }
            is UserSignUpViewModel.SignUpState.Error -> {
                viewModel.setSignUpStateEmpty()
                val errorMessage = (signUpState as UserSignUpViewModel.SignUpState.Error).message
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
            else -> Unit
        }
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val logoPainter: Painter = painterResource(id = R.drawable.insight)
            Image(
                painter = logoPainter,
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(100.dp)
                    .padding(top = 16.dp)
            )
            Text(
                text = "Insight Hub",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            OutlinedTextField(
                value = firstName,
                onValueChange = { newName -> firstName = newName },
                label = { Text(text = "First Name") },
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
                value = lastName,
                onValueChange = { newName -> lastName = newName },
                label = { Text(text = "Last Name") },
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
                value = email,
                onValueChange = { newEmail -> email = newEmail },
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
                onValueChange = { newPassword -> password = newPassword },
                label = { Text(text = "Password") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                visualTransformation = PasswordVisualTransformation(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    disabledContainerColor = MaterialTheme.colorScheme.surface,
                )
            )

            OutlinedTextField(
                value = description,
                onValueChange = { newDescription -> description = newDescription },
                label = { Text(text = "Description") },
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
                    if (firstName.text.isBlank()) errors.add("First name is required.")
                    if (lastName.text.isBlank()) errors.add("Last name is required.")
                    if (email.text.isBlank() || !email.text.contains("@")) errors.add("Valid email is required.")
                    if (password.text.isBlank()) errors.add("Password is required")
                    if (description.text.isBlank()) errors.add("Description is required.")
                    if (errors.isEmpty()) {
                        viewModel.signUp(
                            User(
                                firstName = firstName.text,
                                lastName = lastName.text,
                                email = email.text,
                                password = password.text,
                                description = description.text
                            )
                        )
                    } else {
                        errors.forEach { errorMessage ->
                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),


                ) {
                Text(
                    text = "Register",
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }

}