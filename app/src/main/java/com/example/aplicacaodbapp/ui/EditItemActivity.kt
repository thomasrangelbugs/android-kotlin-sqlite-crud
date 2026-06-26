package com.example.aplicacaodbapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.aplicacaodbapp.R
import com.example.aplicacaodbapp.data.Item
import com.example.aplicacaodbapp.database.DatabaseHelper
import com.example.aplicacaodbapp.databinding.ActivityEditItemBinding
import com.google.android.material.snackbar.Snackbar

class EditItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditItemBinding
    private lateinit var databaseHelper: DatabaseHelper
    private var itemId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = getString(R.string.edit_item_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        databaseHelper = DatabaseHelper(this)
        itemId = intent.getIntExtra(ItemListActivity.EXTRA_ITEM_ID, -1)

        if (itemId == -1) {
            Snackbar.make(binding.root, R.string.item_not_found, Snackbar.LENGTH_LONG).show()
            finish()
            return
        }

        loadItem()

        binding.buttonSaveItem.setOnClickListener {
            saveItem()
        }

        binding.buttonCancelEdit.setOnClickListener {
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

        binding.textItemIdValue.text = getString(R.string.item_id_format, item.id)
        binding.editTextNome.setText(item.nome)
        binding.editTextDescricao.setText(item.descricao)
        binding.editTextQuantidade.setText(item.quantidade.toString())
        binding.editTextValor.setText(item.valor.toString())
    }

    private fun saveItem() {
        val nome = binding.editTextNome.text?.toString()?.trim().orEmpty()
        val descricao = binding.editTextDescricao.text?.toString()?.trim().orEmpty()
        val quantidadeText = binding.editTextQuantidade.text?.toString()?.trim().orEmpty()
        val valorText = binding.editTextValor.text?.toString()?.trim().orEmpty().replace(',', '.')

        binding.layoutNome.error = null
        binding.layoutDescricao.error = null
        binding.layoutQuantidade.error = null
        binding.layoutValor.error = null

        var hasError = false

        if (nome.isBlank()) {
            binding.layoutNome.error = getString(R.string.required_field)
            hasError = true
        }

        if (descricao.isBlank()) {
            binding.layoutDescricao.error = getString(R.string.required_field)
            hasError = true
        }

        val quantidade = quantidadeText.toIntOrNull()
        if (quantidade == null || quantidade < 0) {
            binding.layoutQuantidade.error = getString(R.string.invalid_quantity)
            hasError = true
        }

        val valor = valorText.toDoubleOrNull()
        if (valor == null || valor < 0.0) {
            binding.layoutValor.error = getString(R.string.invalid_value)
            hasError = true
        }

        if (hasError || quantidade == null || valor == null) {
            return
        }

        val updated = databaseHelper.updateItem(
            Item(
                id = itemId,
                nome = nome,
                descricao = descricao,
                quantidade = quantidade,
                valor = valor
            )
        )

        if (updated) {
            Snackbar.make(binding.root, R.string.item_updated_success, Snackbar.LENGTH_LONG).show()
            binding.root.postDelayed({ finish() }, 600)
        } else {
            Snackbar.make(binding.root, R.string.item_update_error, Snackbar.LENGTH_LONG).show()
        }
    }
}

