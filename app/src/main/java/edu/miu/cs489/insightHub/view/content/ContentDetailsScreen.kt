package edu.miu.cs489.insightHub.view.content

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import edu.miu.cs489.insightHub.data.Category
import edu.miu.cs489.insightHub.view.util.Screen
import edu.miu.cs489.insightHub.view.util.userId
import edu.miu.cs489.insightHub.viewmodel.CategoryViewModel
import edu.miu.cs489.insightHub.viewmodel.ContentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentDetailsScreen(
    navController: NavHostController,
    contentViewModel: ContentViewModel,
    categoryViewModel: CategoryViewModel
) {

    val contentReceived = contentViewModel.content.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Content Details") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (contentReceived.value.userId == userId) {
                        IconButton(
                            onClick = {
                                contentViewModel.setContent(contentReceived.value)
                                navController.navigate(Screen.EditContentScreen.route)
                                contentViewModel.clearContentState()
                            }
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                        }

                        IconButton(
                            onClick = {
                                contentViewModel.deleteContent(contentReceived.value.contentId)
                            }
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                        }
                    }
                }
            )

        },
        content = { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                item {
                    var categories by remember {
                        mutableStateOf(listOf<Category>())
                    }
                    LaunchedEffect(Unit) {
                        categoryViewModel.getCategories()
                    }
                    categoryViewModel.categoryState.observeForever {
                        when (it) {
                            is CategoryViewModel.CategoryState.Success -> {
                                categories = it.categoryList
                            }
                            is CategoryViewModel.CategoryState.Error -> {
                                it.message
                            }
                            else -> Unit
                        }
                    }

                    val category = categories.find { it.categoryId == contentReceived.value.categoryId }

                    Text(
                        text = category?.categoryName ?: "Unknown",
                        style = MaterialTheme.typography.bodyMedium
                            .copy(color = Color.Blue, fontWeight = FontWeight.Bold, fontSize = 18.sp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    )

                    Text(
                        text = contentReceived.value.contentTitle,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    Text(
                        text = contentReceived.value.contentDescription,
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    )
                }

                item {
                    contentViewModel.contentState.observeForever {
                        when (it) {
                            is ContentViewModel.ContentState.Loading -> {
                                contentViewModel.clearContentState()
                            }
                            is ContentViewModel.ContentState.DeleteSuccess -> {
                                contentViewModel.clearContentState()
                                Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show()
                                navController.navigate(Screen.HomeScreen.route)
                            }
                            is ContentViewModel.ContentState.Error -> {
                                contentViewModel.clearContentState()
                                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                            }
                            else -> Unit
                        }
                    }
                }
            }
        }
    )
}


