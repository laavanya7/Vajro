package com.example.vajro.DBHelper

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import com.example.vajro.RetrofitService.toast
import com.example.vajro.model.CartProduct


class MyDBOpenHelper(context: Context) : SQLiteOpenHelper(context,DB_NAME, null, DB_VERSION) {

    var product : CartProduct?= null

    companion object {
        const val DB_NAME = "VajroDB"
        const val DB_VERSION = 11
        const val productTable = "TABLE_PRODUCT"
        const val id = "ID"
        const val product_id = "PRODUCT_ID"
        const val name = "NAME"
        const val image = "IMAGE"
        const val quantity = "QUANTITY"
        const val price = "PRICE"
    }

    var TAG = MyDBOpenHelper::class.java.simpleName
    var DBHelper: MyDBOpenHelper? = null
    var db: SQLiteDatabase? = null
    var context: Context

    override fun onCreate(db: SQLiteDatabase) {
        val createTable =
            "create table $productTable(ID integer primary key autoincrement,$product_id,$name,$image,$quantity,$price);"
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        val dropTable = "drop table if exists $productTable"
        db.execSQL(dropTable)
        onCreate(db)
    }


    fun getDataBase(){
        DBHelper = MyDBOpenHelper(context)
        db = DBHelper!!.writableDatabase
    }

    fun insertProductData(product :CartProduct) {

    if (db!!.isOpen) {

        val cv = ContentValues().also {
            it.put(product_id, product.product_id)
            it.put(name, product.name)
            it.put(image, product.image)
            it.put(quantity, product.quantity)
            it.put(price, product.price)
        }
        val response = db!!.insert(productTable, null, cv)
        Log.d(TAG, "----------> Response: $response")

     }


}

    fun updateData(id: String,quantity: String?){

        if (db!!.isOpen()) {
            val cv = ContentValues()
            cv.put(Companion.quantity, quantity)
            // update data based on id
            val response = db!!.update(
                productTable,
                cv,
                product_id + "=?",
                arrayOf(java.lang.String.valueOf(id))
            )
        }

    }

    fun deleteData(id: String) {
        if (db!!.isOpen) {
            // delete data based on id
            val response = db!!.delete(
                productTable,
                product_id + "=?",
                arrayOf(id)
            ).toLong()
        }
    }

    fun clearTables(){
        if (db!!.isOpen){
            db?.delete(productTable,null,null)
        }
    }

    fun getAllData(): ArrayList<CartProduct>? {
        val TABLE_NAME = "TABLE_PRODUCT"
        val selectQuery = "SELECT  * FROM $TABLE_NAME"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        var productList = ArrayList<CartProduct>()
        cursor.moveToFirst()
        while (!cursor.isAfterLast){
            productList.add(CartProduct(
                name =cursor.getString(cursor.getColumnIndex("NAME")),
               id = cursor.getString(cursor.getColumnIndex("ID")),
              product_id =   cursor.getString(cursor.getColumnIndex("PRODUCT_ID")),
              image =  cursor.getString(cursor.getColumnIndex("IMAGE")),
                quantity =  cursor.getString(cursor.getColumnIndex("QUANTITY")),
                price =  cursor.getString(cursor.getColumnIndex("PRICE"))
            ))
            cursor.moveToNext()
        }
        return productList
    }

    // Check Id
    fun isAutho(productid: String): Boolean {
        Log.d(TAG, "----------> isAvailable $product_id")
        var cursor: Cursor? = null
        if (db!!.isOpen) {
            val getTableQuery = "select * from $productTable"
            Log.d(TAG, "----------> getTableQuery: $getTableQuery")
            cursor = db!!.rawQuery(getTableQuery, null)
            Log.d(TAG, "----------> Cursor count: " + cursor.count)
            if (cursor.count == 0) {
                return false
            }
            if (cursor.moveToFirst()) {
                do {
                    val tmpProductID=
                        cursor.getString(cursor.getColumnIndex(product_id))
                    if (tmpProductID.equals(productid, ignoreCase = true)) {
                        return true
                    }
                } while (cursor.moveToNext())
            }
        }
        return false
    }

    init {
        Log.d(TAG, "----------> MyDBOpenHelper")
        this.context = context
    }
}




