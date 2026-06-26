package com.example.aplicacaodbapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aplicacaodbapp.R
import com.example.aplicacaodbapp.data.Item
import com.example.aplicacaodbapp.databinding.ItemListEntryBinding
import java.text.NumberFormat
import java.util.Locale

class ItemAdapter(
    private val onEditClick: (Item) -> Unit,
    private val onDeleteClick: (Item) -> Unit
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    private val items = mutableListOf<Item>()

    fun submitList(newItems: List<Item>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemListEntryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class ItemViewHolder(
        private val binding: ItemListEntryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Item) {
            val formatter = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

            binding.textItemId.text = binding.root.context.getString(R.string.item_id_format, item.id)
            binding.textItemNome.text =
                binding.root.context.getString(R.string.item_name_format, item.nome)
            binding.textItemDescricao.text =
                binding.root.context.getString(R.string.item_description_format, item.descricao)
            binding.textItemQuantidade.text =
                binding.root.context.getString(R.string.item_quantity_format, item.quantidade)
            binding.textItemValor.text = binding.root.context.getString(
                R.string.item_value_format,
                formatter.format(item.valor)
            )

            binding.buttonEdit.setOnClickListener { onEditClick(item) }
            binding.buttonDelete.setOnClickListener { onDeleteClick(item) }
        }
    }
}

