package com.example.aplicacaodbapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.aplicacaodbapp.R
import com.example.aplicacaodbapp.database.DatabaseHelper
import com.example.aplicacaodbapp.databinding.ActivityDeleteItemBinding
import com.google.android.material.snackbar.Snackbar
import java.text.NumberFormat
import java.util.Locale

class DeleteItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDeleteItemBinding
    private lateinit var databaseHelper: DatabaseHelper
    private var itemId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeleteItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = getString(R.string.delete_item_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        databaseHelper = DatabaseHelper(this)
        itemId = intent.getIntExtra(ItemListActivity.EXTRA_ITEM_ID, -1)

        if (itemId == -1) {
            Snackbar.make(binding.root, R.string.item_not_found, Snackbar.LENGTH_LONG).show()
            finish()
            return
        }

        loadItem()

        binding.buttonConfirmDelete.setOnClickListener {
            deleteItem()
        }

        binding.buttonCancelDelete.setOnClickListener {
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun loadItem() {
        val item = databaseHelper.getItemById(itemId)

        if (item == null) {
            Snackbar.make(binding.root, R.string.item_not_found, Snackbar.LENGTH_LONG).show()
            finish()
            return
        }

        val formatter = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

        binding.textDeleteId.text = getString(R.string.item_id_format, item.id)
        binding.textDeleteNome.text = getString(R.string.item_name_format, item.nome)
        binding.textDeleteDescricao.text = getString(R.string.item_description_format, item.descricao)
        binding.textDeleteQuantidade.text =
            getString(R.string.item_quantity_format, item.quantidade)
        binding.textDeleteValor.text = getString(
            R.string.item_value_format,
            formatter.format(item.valor)
        )
    }

    private fun deleteItem() {
        val deleted = databaseHelper.deleteItem(itemId)

        if (deleted) {
            Snackbar.make(binding.root, R.string.item_deleted_success, Snackbar.LENGTH_LONG).show()
            binding.root.postDelayed({ finish() }, 600)
        } else {
            Snackbar.make(binding.root, R.string.item_delete_error, Snackbar.LENGTH_LONG).show()
        }
    }
}

