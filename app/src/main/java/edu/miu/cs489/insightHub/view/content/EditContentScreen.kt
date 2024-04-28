package edu.miu.cs489.insightHub.view.content

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import edu.miu.cs489.insightHub.data.ContentUpdate
import edu.miu.cs489.insightHub.view.util.Screen
import edu.miu.cs489.insightHub.viewmodel.ContentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditContentScreen(
    navController: NavHostController,
    contentViewModel: ContentViewModel
) {

    val content = contentViewModel.content.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Edit Content") },
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

            var title by remember { mutableStateOf(TextFieldValue(content.value.contentTitle)) }
            var body by remember { mutableStateOf(TextFieldValue(content.value.contentDescription)) }

            TextField(
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
                    .padding(top = 16.dp)
            ) {
                BasicTextField(
                    value = body,
                    onValueChange = { newValue -> body = newValue },
                    textStyle = TextStyle(color = Color.Black, fontSize = MaterialTheme.typography.bodyMedium.fontSize),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                )

                if (body.text.isEmpty()) {
                    Text(
                        text = "Write your story...",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                    )
                }
            }

            Button(
                onClick = {
                    contentViewModel.updateContent(
                        ContentUpdate(
                            contentId = content.value.contentId,
                            contentTitle = title.text,
                            contentDescription = body.text
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text(text = "Update Content")
            }

            contentViewModel.contentState.observeForever {
                when(it){
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
        }
    }
}

