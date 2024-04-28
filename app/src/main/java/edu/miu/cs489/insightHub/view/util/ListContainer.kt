package edu.miu.cs489.insightHub.view.util

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import edu.miu.cs489.insightHub.R
import edu.miu.cs489.insightHub.data.Content
import edu.miu.cs489.insightHub.viewmodel.ContentViewModel

@Composable
fun ListContainer(listItems: List<Content>, navController: NavHostController, contentViewModel: ContentViewModel) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(listItems) { item ->
            // Call the ListItem composable function for each item
            ListItem(
                title = item.contentTitle,
                body = item.contentDescription,
                imageResource = R.drawable.insight,
                onClick = {
                    contentViewModel.setContent(item)
                    navController.navigate(Screen.ContentDetailsScreen.route)
                }
            )
        }
    }
}