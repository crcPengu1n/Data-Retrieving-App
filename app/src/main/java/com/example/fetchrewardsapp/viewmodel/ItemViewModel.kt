package com.example.fetchrewardsapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.fetchrewardsapp.model.Item
import com.example.fetchrewardsapp.network.RetrofitClient
import kotlinx.coroutines.launch

class ItemViewModel : ViewModel() {
    private val _items = MutableLiveData<List<Item>>()
    val items: LiveData<List<Item>> get() = _items

    init {
        fetchItems()
    }

    private fun fetchItems() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getItems()
                _items.value = response
                    .filter { it.name?.isNotBlank() == true } // Filter for empty data
                    .sortedWith(compareBy<Item> { it.listId } // sort first time by id
                        .thenBy { it.name?.filter { it.isDigit() }?.toIntOrNull() ?: Int.MAX_VALUE }) // sort first time by name
            } catch (e: Exception) {
                _items.value = emptyList() // Avoiding Collapse
            }
        }
    }
}