package com.example.shoppinglistapp.presentation
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglistapp.data.AppRepository
import com.example.shoppinglistapp.domain.ShoppingItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
class ShoppingViewModel(
    private val repository: AppRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ShoppingUiState())
    val uiState: StateFlow<ShoppingUiState> = _uiState.asStateFlow()
    init {
        observeData()
    }
    private fun observeData() {
        viewModelScope.launch {
            launch {
                repository.isDarkThemeFlow.collectLatest { isDark ->
                    println("Theme from DataStore: $isDark")
                    _uiState.value = _uiState.value.copy(isDarkTheme = isDark)
                }
            }
            launch {
                repository.shoppingItemsFlow.collectLatest { items ->
                    _uiState.value = _uiState.value.copy(items = items)
                }
            }
        }
    }
    fun toggleTheme() {
        val newState = !_uiState.value.isDarkTheme
        println("Toggle theme: $newState")
        viewModelScope.launch {
            repository.toggleTheme(newState)
        }
    }
    fun addItem(name: String) {
        if (name.isBlank()) return
        val currentItems = _uiState.value.items
        val newItems = currentItems + ShoppingItem(name = name)
        viewModelScope.launch {
            repository.saveItems(newItems)
        }
    }
    fun toggleItemCheck(itemId: String) {
        val currentItems = _uiState.value.items
        val newItems = currentItems.map {
            if (it.id == itemId) it.copy(isChecked = !it.isChecked) else it
        }
        viewModelScope.launch {
            repository.saveItems(newItems)
        }
    }
    fun deleteItem(itemId: String) {
        val currentItems = _uiState.value.items
        val newItems = currentItems.filter { it.id != itemId }
        viewModelScope.launch {
            repository.saveItems(newItems)
        }
    }
}
data class ShoppingUiState(
    val isDarkTheme: Boolean = false,
    val items: List<ShoppingItem> = emptyList(),
    val inputText: String = ""
)