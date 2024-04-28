package edu.miu.cs489.insightHub.view.bookmark

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import edu.miu.cs489.insightHub.data.Content
import edu.miu.cs489.insightHub.view.util.BottomNavigationBar
import edu.miu.cs489.insightHub.view.util.ListContainer
import edu.miu.cs489.insightHub.viewmodel.ContentViewModel

@Composable
fun BookmarkScreen(navController: NavHostController, contentViewModel: ContentViewModel) {
    
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    //val contents by contentViewModel.contents.collectAsState()
    val contents by remember {
        mutableStateOf(listOf<Content>())
    }
    
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController, currentRoute = currentRoute)
        }
    ) {innerPadding -> 
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)) {
            ListContainer(listItems = contents, navController = navController, contentViewModel)
        }
    }
    
}