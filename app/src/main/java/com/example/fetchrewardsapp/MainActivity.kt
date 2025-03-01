package com.example.fetchrewardsapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fetchrewardsapp.viewmodel.ItemViewModel
import com.example.fetchrewardsapp.adapter.ItemAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var adapter: ItemAdapter
    private val viewModel: ItemViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        searchView = findViewById(R.id.searchView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        viewModel.items.observe(this, Observer { items ->
            adapter = ItemAdapter(items)
            recyclerView.adapter = adapter
        })

        // setup SearchView monitoring input
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchItem(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    // search for item name and display listId
    private fun searchItem(query: String?) {
        if (query.isNullOrBlank()) return

        val foundItem = viewModel.items.value?.find { item ->
            val nameLower = item.name?.lowercase() ?: ""   // make names lowercase
            val queryLower = query.lowercase() // make query also lowercase

            val nameDigits = item.name?.filter { it.isDigit() } ?: ""  // get number from item

            // support multiple types of searching ways
            // eg1. whole name ("Item 823")
            // eg2. only number ("823")
            // eg3. lowercase and number ("item 823")
            nameLower.contains(queryLower) || nameDigits == queryLower
        }
        if (foundItem != null) {
            Toast.makeText(this, "List ID: ${foundItem.listId}", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Item not found", Toast.LENGTH_SHORT).show()
        }
    }
}
