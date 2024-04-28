package edu.miu.cs489.insightHub.view.search

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SearchBar(
    queryString: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier) {

    TextField(
        value = queryString,
        onValueChange = onQueryChange,
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        label = { Text(text = "Search")},
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = Color.Gray)
        }
    )
}