package com.example.aplicacaodbapp.database

import android.content.ContentValues
import android.content.Context
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.aplicacaodbapp.data.Item

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE $TABLE_ITEM (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NOME TEXT NOT NULL,
                $COLUMN_DESCRICAO TEXT NOT NULL,
                $COLUMN_QUANTIDADE INTEGER NOT NULL,
                $COLUMN_VALOR REAL NOT NULL
            )
            """.trimIndent()
        )

        seedInitialData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ITEM")
        onCreate(db)
    }

    fun getAllItems(): List<Item> {
        val items = mutableListOf<Item>()
        val cursor = readableDatabase.query(
            TABLE_ITEM,
            null,
            null,
            null,
            null,
            null,
            "$COLUMN_ID ASC"
        )

        cursor.use {
            while (it.moveToNext()) {
                items.add(cursorToItem(it))
            }
        }

        return items
    }

    fun getItemById(id: Int): Item? {
        val cursor = readableDatabase.query(
            TABLE_ITEM,
            null,
            "$COLUMN_ID = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        cursor.use {
            return if (it.moveToFirst()) cursorToItem(it) else null
        }
    }

    fun updateItem(item: Item): Boolean {
        val values = ContentValues().apply {
            put(COLUMN_NOME, item.nome)
            put(COLUMN_DESCRICAO, item.descricao)
            put(COLUMN_QUANTIDADE, item.quantidade)
            put(COLUMN_VALOR, item.valor)
        }

        return writableDatabase.update(
            TABLE_ITEM,
            values,
            "$COLUMN_ID = ?",
            arrayOf(item.id.toString())
        ) > 0
    }

    fun deleteItem(id: Int): Boolean {
        return writableDatabase.delete(
            TABLE_ITEM,
            "$COLUMN_ID = ?",
            arrayOf(id.toString())
        ) > 0
    }

    fun restoreSampleItemsIfEmpty(): Boolean {
        val isEmpty = DatabaseUtils.queryNumEntries(readableDatabase, TABLE_ITEM) == 0L
        if (isEmpty) {
            seedInitialData(writableDatabase)
            return true
        }
        return false
    }

    private fun seedInitialData(db: SQLiteDatabase) {
        val sampleItems = listOf(
            Item(0, "Caderno", "Caderno universitario com 10 materias", 12, 24.90),
            Item(0, "Caneta", "Caneta esferografica azul", 30, 2.50),
            Item(0, "Mochila", "Mochila escolar resistente", 5, 119.90),
            Item(0, "Estojo", "Estojo com divisorias internas", 8, 19.75)
        )

        sampleItems.forEach { item ->
            db.insert(TABLE_ITEM, null, ContentValues().apply {
                put(COLUMN_NOME, item.nome)
                put(COLUMN_DESCRICAO, item.descricao)
                put(COLUMN_QUANTIDADE, item.quantidade)
                put(COLUMN_VALOR, item.valor)
            })
        }
    }

    private fun cursorToItem(cursor: android.database.Cursor): Item {
        return Item(
            id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
            nome = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOME)),
            descricao = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRICAO)),
            quantidade = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTIDADE)),
            valor = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_VALOR))
        )
    }

    companion object {
        private const val DATABASE_NAME = "aplicacaodb"
        private const val DATABASE_VERSION = 1

        private const val TABLE_ITEM = "item"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NOME = "nome"
        private const val COLUMN_DESCRICAO = "descricao"
        private const val COLUMN_QUANTIDADE = "quantidade"
        private const val COLUMN_VALOR = "valor"
    }
}

