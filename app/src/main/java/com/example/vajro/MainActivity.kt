package com.example.vajro

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.vajro.DBHelper.MyDBOpenHelper
import com.example.vajro.RetrofitService.CartActivity
import com.example.vajro.RetrofitService.client
import com.example.vajro.adapter.ProductAdapter
import com.example.vajro.model.CartProduct
import com.example.vajro.model.Product
import com.example.vajro.model.Products
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private val disposable = CompositeDisposable()
    var textCartItemCount : TextView ? = null
    var DBHelper: MyDBOpenHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        DBHelper =  MyDBOpenHelper(applicationContext)
        DBHelper!!.getDataBase()


        recyclerView.layoutManager = GridLayoutManager(this,2)
        getProducts()
        initCallBacks()


    }

    private fun initCallBacks(){
        go_to_cart.setOnClickListener {

            startActivity(Intent(this@MainActivity,CartActivity::class.java))

        }
    }

    override fun onResume() {
        super.onResume()
        getProducts()
        setCart()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.item_menu, menu)
        val menuItem: MenuItem = menu.findItem(R.id.action_cart)
        val actionView: View = menuItem.getActionView()
        textCartItemCount = actionView.findViewById(R.id.cart_badge) as TextView

        actionView.setOnClickListener {

            onOptionsItemSelected(menuItem)
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.action_cart -> {
                startActivity(Intent(this@MainActivity,CartActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupBadge(mCartItemCount:Int) {

        if (mCartItemCount == 0) {

            textCartItemCount?.visibility = GONE
            go_to_cart.visibility = GONE


        } else {
            textCartItemCount?.setText(mCartItemCount.toString())
            textCartItemCount?.visibility = VISIBLE
            go_to_cart.visibility = VISIBLE

        }
    }

    fun setCart(){
        var cartList = DBHelper?.getAllData()!!
        setupBadge(cartList.size)
    }

    private fun getProducts(){
            var callBack = object :ProductAdapter.Listener{

                override fun orderedProducts(orderList: HashSet<String>) {
                    setupBadge(orderList.size)
                }

            }
            fun observer(): DisposableSingleObserver<Products> {
                return object : DisposableSingleObserver<Products>() {
                    override fun onSuccess(t: Products) {
                        val productAdapter = ProductAdapter(t.products,callBack,applicationContext)
                        recyclerView.adapter = productAdapter
                    }

                    override fun onError(e: Throwable) {
                        FailureHandler.processFailure(this@MainActivity, e)
                    }

                }
            }

            disposable.add(client.apiInterface.getProducts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer())
)
        }

}