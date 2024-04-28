package edu.miu.cs489.insightHub.view.home

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import edu.miu.cs489.insightHub.data.Content
import edu.miu.cs489.insightHub.view.util.BottomNavigationBar
import edu.miu.cs489.insightHub.view.util.ListContainer
import edu.miu.cs489.insightHub.view.util.Screen
import edu.miu.cs489.insightHub.view.util.showLoadingDialog
import edu.miu.cs489.insightHub.viewmodel.ContentViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(navController: NavHostController, contentViewModel: ContentViewModel) {

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController, currentRoute = Screen.HomeScreen.route)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    contentViewModel.clearContentState()
                    navController.navigate(Screen.AddContentScreen.route)
                },
                modifier = Modifier.padding(16.dp),
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = Color.White
                )
            }
        }
    ) { innerPadding ->

        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()
        var firstBackPress by remember {
            mutableStateOf(false)
        }

        BackHandler {
            if (firstBackPress){
                val activity = context as Activity
                activity.finish()
            }else{
                firstBackPress = true
                Toast.makeText(context, "Press again to exit", Toast.LENGTH_SHORT).show()
                coroutineScope.launch {
                    delay(2000)
                    firstBackPress = false
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            var isLoading by remember {
                mutableStateOf(false)
            }
            var contents by remember {
                mutableStateOf(listOf<Content>())
            }

            LaunchedEffect(Unit){
                contentViewModel.initializeContentList()
            }

            contentViewModel.contentState.observeForever {
                when(it){
                    is ContentViewModel.ContentState.Loading ->{
                        isLoading = true

                    }
                    is ContentViewModel.ContentState.ListSuccess -> {
                        isLoading = false
                        contents = it.contents
                    }
                    is ContentViewModel.ContentState.Error -> {
                        isLoading = false
                        it.message
                    }
                    is ContentViewModel.ContentState.Initial -> {

                    }
                    else -> Unit
                }
            }
            if (isLoading){
                showLoadingDialog(message = "Loading...") {
                    isLoading = false
                }
            }

            ListContainer(listItems = contents, navController = navController, contentViewModel)
        }

    }
}

