package com.example.vajro.RetrofitService

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.Transition
import android.transition.TransitionManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.animation.AnticipateOvershootInterpolator
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintSet
import com.example.vajro.DBHelper.MyDBOpenHelper
import com.example.vajro.R
import com.example.vajro.adapter.CartAdapter
import com.example.vajro.model.CartProduct
import com.example.vajro.model.Products
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.activity_cart.recyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_card_cart.*
import kotlinx.android.synthetic.main.layout_cart_total.*
import kotlinx.android.synthetic.main.layout_cart_total.view.*

class CartActivity : AppCompatActivity() {

    var DBHelper: MyDBOpenHelper? = null
    private lateinit var cartAdapter : CartAdapter
     var productList = ArrayList<CartProduct>()
    var rupee = "\u20B9"
    var totalPrice = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        DBHelper =  MyDBOpenHelper(applicationContext)
        DBHelper!!.getDataBase()

        recyclerView.setLayoutManager(this)
        getData()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.clear_cart_menu, menu)

        return true
    }





    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.clear_cart -> {
                clearCart()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun clearCart(){
        DBHelper?.clearTables()
        getData()
    }



    private fun getData(){
         productList = ArrayList<CartProduct>()
         productList = DBHelper?.getAllData()!!

        var callback = object : CartAdapter.Listener{
            override fun delete(position: Int) {
                bottomSheet(position)
            }

        }
         cartAdapter = CartAdapter(list = productList!!,listener = callback)
        recyclerView.adapter = cartAdapter
        totalPrice = 0

        for (cart : CartProduct in productList ){
            var number = cart.price.replace("[^0-9]".toRegex(), "")
            totalPrice += number.toInt() * cart.quantity.toInt()
        }
        grand_total.setText( rupee + totalPrice)
        if (totalPrice == 0){
            main_lay.visibility = GONE
        }else{
            main_lay.visibility = VISIBLE
        }



    }

    fun bottomSheet(position: Int){
        var  dialog = BottomSheetDialog(this)
        val bottomSheet = layoutInflater.inflate(R.layout.layout_cart_total, null)

        bottomSheet.delete_lay.setOnClickListener {
            DBHelper?.deleteData(productList.get(position).product_id)
            getData()
            dialog.dismiss()
        }

        dialog.setContentView(bottomSheet)
        dialog.show()

    }


}