package edu.miu.cs489.insightHub.view.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import edu.miu.cs489.insightHub.data.Content
import edu.miu.cs489.insightHub.view.util.BottomNavigationBar
import edu.miu.cs489.insightHub.view.util.ListContainer
import edu.miu.cs489.insightHub.view.util.Screen
import edu.miu.cs489.insightHub.viewmodel.ContentViewModel

@Composable
fun SearchScreen(navController: NavHostController, contentViewModel: ContentViewModel) {
    
    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController, currentRoute = Screen.SearchScreen.route) }
    ) {innerPadding -> 
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ){
            var searchQuery by remember { mutableStateOf("") }

            var contents by remember {
                mutableStateOf(listOf<Content>())
            }

            LaunchedEffect(Unit){
                contentViewModel.initializeContentList()
            }

            contentViewModel.contentState.observeForever {
                when(it){
                    is ContentViewModel.ContentState.Loading ->{

                    }
                    is ContentViewModel.ContentState.ListSuccess -> {

                        contents = it.contents
                    }
                    is ContentViewModel.ContentState.Error -> {
                        it.message
                    }
                    is ContentViewModel.ContentState.Initial -> {

                    }
                    else -> Unit
                }
            }

            SearchBar(queryString = searchQuery,
                onQueryChange = {
                    newQuery-> searchQuery = newQuery
                })

            ListContainer(listItems = contents.filter {
                                                      it.contentTitle.contains(searchQuery, ignoreCase = true) ||
                                                              it.contentDescription.contains(searchQuery, ignoreCase = true)
            }, navController = navController, contentViewModel)
        }

    }
    
}