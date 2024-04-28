package edu.miu.cs489.insightHub.view.content

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavHostController
import edu.miu.cs489.insightHub.data.Category
import edu.miu.cs489.insightHub.data.Content
import edu.miu.cs489.insightHub.view.util.Screen
import edu.miu.cs489.insightHub.view.util.userId
import edu.miu.cs489.insightHub.viewmodel.CategoryViewModel
import edu.miu.cs489.insightHub.viewmodel.ContentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddContentScreen(
    navController: NavHostController,
    categoryViewModel: CategoryViewModel,
    contentViewModel: ContentViewModel
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Add Content") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            var title by remember {
                mutableStateOf(TextFieldValue(""))
            }
            var body by remember {
                mutableStateOf(TextFieldValue(""))
            }
            var expanded by remember { mutableStateOf(false) }
            var selectedCategory by remember { mutableStateOf("Select Category") }
            var selectedCategoryId by remember {
                mutableStateOf(0)
            }
            val context = LocalContext.current
            var categories by remember {
                mutableStateOf(listOf<Category>())
            }
            LaunchedEffect(Unit){
                categoryViewModel.getCategories()
            }
            categoryViewModel.categoryState.observeForever {
                when (it) {
                    is CategoryViewModel.CategoryState.Loading -> {

                    }

                    is CategoryViewModel.CategoryState.Success -> {
                        categories = it.categoryList
                    }

                    is CategoryViewModel.CategoryState.Error -> {
                        it.message
                    }

                    else -> Unit
                }
            }

            contentViewModel.contentState.observeForever {
                when (it) {
                    is ContentViewModel.ContentState.Loading -> {
                    }

                    is ContentViewModel.ContentState.Success -> {
                        contentViewModel.clearContentState()
                        navController.navigate(Screen.HomeScreen.route)
                    }

                    is ContentViewModel.ContentState.Error -> {
                        contentViewModel.clearContentState()
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .clickable { expanded = !expanded }
                    .border(
                        width = 1.dp,
                        color = Color.Gray, // Border color
                        shape = RoundedCornerShape(4.dp) // Rounded corner shape
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = selectedCategory,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
                Surface(
                    color = Color.Transparent,
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.onPrimary),
                    modifier = Modifier
                        .widthIn(240.dp)
                        .padding(4.dp)
                ) {
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        properties = PopupProperties(focusable = true),
                        modifier = Modifier
                            .widthIn(240.dp)
                            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                    ) {
                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = category.categoryName,
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.padding(
                                            vertical = 8.dp,
                                            horizontal = 16.dp
                                        )
                                    )
                                },
                                onClick = {
                                    selectedCategory = category.categoryName
                                    selectedCategoryId = category.categoryId
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            OutlinedTextField(
                value = title,
                onValueChange = { newValue -> title = newValue },
                label = { Text("Title") },
                textStyle = TextStyle(fontWeight = FontWeight.Bold),
                modifier = Modifier.fillMaxWidth()
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                OutlinedTextField(
                    value = body,
                    onValueChange = { newValue -> body = newValue },
                    label = { Text("Write your story") },
                    textStyle = TextStyle(
                        color = Color.Black,
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize
                    ),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                )
            }

            Button(
                onClick = {
                    val errors = mutableListOf<String>()
                    if (title.text.isBlank()) errors.add("Content Title is required")
                    if (body.text.isBlank()) errors.add("Content Body is required")
                    if (selectedCategory == "Select Category") errors.add("Please select category")
                    if (errors.isEmpty()) {
                        contentViewModel.addContent(
                            Content(
                                contentId = 0,
                                contentTitle = title.text,
                                contentDescription = body.text,
                                categoryId = selectedCategoryId,
                                userId = userId
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
                    .padding(top = 16.dp)
            ) {
                Text(text = "Publish")
            }
        }
    }
}