package com.example.shoppinglistapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.shoppinglistapp.domain.ShoppingItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_settings")
class AppRepository(private val context: Context) {
    private val THEME_KEY = booleanPreferencesKey("is_dark_theme")
    private val ITEMS_KEY = stringPreferencesKey("shopping_items")
    val isDarkThemeFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[THEME_KEY] ?: false
        }
    suspend fun toggleTheme(isDark: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[THEME_KEY] = isDark
        }
    }
    val shoppingItemsFlow: Flow<List<ShoppingItem>> = context.dataStore.data
        .map { preferences ->
            val savedString = preferences[ITEMS_KEY] ?: ""
            if (savedString.isEmpty()) emptyList()
            else {
                savedString.split(",").mapNotNull { itemStr ->
                    val parts = itemStr.split("|")
                    if (parts.size == 3) {
                        ShoppingItem(parts[0], parts[1], parts[2].toBoolean())
                    } else null
                }
            }
        }

    suspend fun saveItems(items: List<ShoppingItem>) {
        val serialized = items.joinToString(",") { "${it.id}|${it.name}|${it.isChecked}" }
        context.dataStore.edit { preferences ->
            preferences[ITEMS_KEY] = serialized
        }
    }
}