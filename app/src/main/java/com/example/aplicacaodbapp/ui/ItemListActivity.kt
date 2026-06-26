package com.example.aplicacaodbapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplicacaodbapp.R
import com.example.aplicacaodbapp.database.DatabaseHelper
import com.example.aplicacaodbapp.databinding.ActivityItemListBinding
import com.example.aplicacaodbapp.ui.adapter.ItemAdapter
import com.google.android.material.snackbar.Snackbar

class ItemListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityItemListBinding
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var itemAdapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = getString(R.string.item_list_title)

        databaseHelper = DatabaseHelper(this)

        itemAdapter = ItemAdapter(
            onEditClick = { item ->
                startActivity(
                    Intent(this, EditItemActivity::class.java)
                        .putExtra(EXTRA_ITEM_ID, item.id)
                )
            },
            onDeleteClick = { item ->
                startActivity(
                    Intent(this, DeleteItemActivity::class.java)
                        .putExtra(EXTRA_ITEM_ID, item.id)
                )
            }
        )

        binding.recyclerViewItems.apply {
            layoutManager = LinearLayoutManager(this@ItemListActivity)
            adapter = itemAdapter
        }

        binding.buttonRestoreSamples.setOnClickListener {
            val restored = databaseHelper.restoreSampleItemsIfEmpty()
            loadItems()

            val messageRes = if (restored) {
                R.string.samples_restored
            } else {
                R.string.samples_not_restored
            }

            Snackbar.make(binding.root, messageRes, Snackbar.LENGTH_LONG).show()
        }

        loadItems()
    }

    override fun onResume() {
        super.onResume()
        loadItems()
    }

    private fun loadItems() {
        val items = databaseHelper.getAllItems()
        itemAdapter.submitList(items)

        binding.recyclerViewItems.isVisible = items.isNotEmpty()
        binding.textEmptyState.isVisible = items.isEmpty()
        binding.textTotalItems.text = getString(R.string.total_items_format, items.size)
    }

    companion object {
        const val EXTRA_ITEM_ID = "extra_item_id"
    }
}

