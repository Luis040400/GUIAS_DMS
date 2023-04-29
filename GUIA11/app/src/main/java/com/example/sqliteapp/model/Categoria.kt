package com.example.sqliteapp.model

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.sqliteapp.db.HelperDB

class Categoria(context: Context?) {

    private var helper: HelperDB? = null
    var db: SQLiteDatabase? = null

    init {
        helper = HelperDB(context)
        db = helper!!.writableDatabase
    }

    companion object {
        val TABLE_NAME_CATEGORIA = "categoria"
        val COL_ID = "idcategoria"
        val COL_NOMBRE = "nombre"

        val CREATE_TABLE_CATEGORIA = (
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_CATEGORIA + "("
                        + COL_ID + " integer primary key autoincrement,"
                        + COL_NOMBRE + " varchar(50) NOT NULL);"
                )
    }

    fun generarContentValues(
        nombre: String?
    ): ContentValues? {
        val valores = ContentValues()
        valores.put(COL_NOMBRE, nombre)
        return valores
    }

    fun insertValuesDefault() {
        val categories = arrayOf(
            "Abarrotes",
            "Carnes",
            "Embutidos",
            "Mariscos",
            "Pescado",
            "Bebidas",
            "Verduras",
            "Frutas",
            "Bebidas Carbonatadas",
            "Bebidas no carbonatadas"
        )
        val columns = arrayOf(COL_ID, COL_NOMBRE)
        var cursor: Cursor? =
            db!!.query(TABLE_NAME_CATEGORIA, columns, null, null, null, null, null)

        if (cursor == null || cursor!!.count <= 0) {
            for (item in categories) {
                db!!.insert(TABLE_NAME_CATEGORIA, null, generarContentValues(item))
            }
        }
    }

    fun showAllCategoria(): Cursor? {
        val columns = arrayOf(COL_ID, COL_NOMBRE)
        return db!!.query(TABLE_NAME_CATEGORIA, columns, null, null, null, null, "$COL_NOMBRE ASC")
    }
    fun searchID(nombre:String?): Int?{
        val columns = arrayOf(COL_ID, COL_NOMBRE)
        val cursor: Cursor? = db!!.query(
            TABLE_NAME_CATEGORIA, columns,
            "$COL_NOMBRE=?", arrayOf(nombre.toString()),null,null,null
        )
        cursor!!.moveToFirst()
        return cursor!!.getInt(0)
    }
    fun searchNombre(id: Int): String?{
        val columns = arrayOf(COL_ID, COL_NOMBRE)
        var cursor: Cursor? = db!!.query(
            TABLE_NAME_CATEGORIA,columns,
            "$COL_ID=?", arrayOf(id.toString()),null,null,null
        )
        cursor!!.moveToFirst()
        return cursor!!.getString(1)
    }
}