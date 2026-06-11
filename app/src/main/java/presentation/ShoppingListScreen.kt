package com.example.shoppinglistapp.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListScreen(
    uiState: ShoppingUiState,
    onToggleTheme: () -> Unit,
    onAddItem: (String) -> Unit,
    onToggleItem: (String) -> Unit,
    onDeleteItem: (String) -> Unit
) {
    var inputText by remember { mutableStateOf("") }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Список покупок", style = MaterialTheme.typography.headlineMedium,color = MaterialTheme.colorScheme.onBackground)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(if (uiState.isDarkTheme) "Темная" else "Светлая", color = MaterialTheme.colorScheme.onBackground)
                    Switch(
                        checked = uiState.isDarkTheme,
                        onCheckedChange = { onToggleTheme() }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Новый товар",color = MaterialTheme.colorScheme.onSurfaceVariant) },
                    singleLine = true
                )
                IconButton(onClick = {
                    if (inputText.isNotBlank()) {
                        onAddItem(inputText)
                        inputText = ""
                    }
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Добавить")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(uiState.items) { item ->
                    ShoppingItemRow(
                        item = item,
                        onToggle = { onToggleItem(item.id) },
                        onDelete = { onDeleteItem(item.id) }
                    )
                }
            }
        }
    }
}
@Composable
fun ShoppingItemRow(
    item: com.example.shoppinglistapp.domain.ShoppingItem,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onToggle() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = item.isChecked,
            onCheckedChange = { onToggle() }
        )
        Text(
            text = item.name,
            modifier = Modifier.weight(1f).padding(start = 8.dp),
            textDecoration = if (item.isChecked) TextDecoration.LineThrough else TextDecoration.None,
            color = if (item.isChecked)
                MaterialTheme.colorScheme.onSurfaceVariant
            else
                MaterialTheme.colorScheme.onSurface
        )
        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Delete, contentDescription = "Удалить", tint = MaterialTheme.colorScheme.error)
        }
    }
}