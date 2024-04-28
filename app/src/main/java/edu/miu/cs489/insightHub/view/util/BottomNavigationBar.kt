package edu.miu.cs489.insightHub.view.util

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import edu.miu.cs489.insightHub.data.BottomNavItem

@Composable
fun BottomNavigationBar(navController: NavHostController, currentRoute: String?) {

    val bottomNavItem = listOf(
        BottomNavItem(
            route = Screen.HomeScreen.route,
            icon = Icons.Default.Home,
            label = "Home"
        ),
        BottomNavItem(
            route = Screen.SearchScreen.route,
            icon = Icons.Default.Search,
            label = "Search"
        ),
//        BottomNavItem(
//            route = Screen.BookmarkScreen.route,
//            icon = Icons.Default.FavoriteBorder,
//            label = "Bookmark"
//        ),
        BottomNavItem(
            route = Screen.ProfileScreen.route,
            icon = Icons.Default.Person,
            label = "Profile"
        )
    )

    BottomAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            bottomNavItem.forEach{bottomNavItem ->
                NavigationBarItem(selected = currentRoute == bottomNavItem.route,
                    onClick = { navController.navigate(bottomNavItem.route){
                        popUpTo(navController.graph.startDestinationId){
                            saveState = true
                        }
                        launchSingleTop = true
                    } },
                    icon = { Icon(imageVector = bottomNavItem.icon, contentDescription = null)},
                    label = { Text(text = bottomNavItem.label)}
                )
            }
        }
    }
}
