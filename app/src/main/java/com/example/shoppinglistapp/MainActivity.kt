package com.example.shoppinglistapp
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglistapp.data.AppRepository
import com.example.shoppinglistapp.presentation.ShoppingListScreen
import com.example.shoppinglistapp.presentation.ShoppingViewModel
import com.example.shoppinglistapp.ui.theme.ShoppingListAppTheme
class ShoppingViewModelFactory(private val repository: AppRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShoppingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ShoppingViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = AppRepository(applicationContext)

        val viewModel: ShoppingViewModel = ViewModelProvider(
            this,
            ShoppingViewModelFactory(repository)
        )[ShoppingViewModel::class.java]
        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            println(" MainActivity recomposed! Theme = ${uiState.isDarkTheme}")
            ShoppingListAppTheme(isDarkTheme = uiState.isDarkTheme) {
                ShoppingListScreen(
                    uiState = uiState,
                    onToggleTheme = { viewModel.toggleTheme() },
                    onAddItem = { viewModel.addItem(it) },
                    onToggleItem = { viewModel.toggleItemCheck(it) },
                    onDeleteItem = { viewModel.deleteItem(it) }
                )
            }
        }
    }
}