package com.example.vajro.adapter

import android.content.Context
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.vajro.DBHelper.MyDBOpenHelper
import com.example.vajro.R
import com.example.vajro.RetrofitService.inflate
import com.example.vajro.RetrofitService.toast
import com.example.vajro.model.CartProduct
import com.example.vajro.model.Product
import com.google.gson.Gson
import kotlinx.android.synthetic.main.item_product_list.view.*

class ProductAdapter(var list : ArrayList<Product>, var listener:Listener?= null,var context: Context)
    :RecyclerView.Adapter<ProductAdapter.ProductHolder>() {

    var orderedList = HashSet<String>()
    var DBHelper: MyDBOpenHelper? = null
    var cartList = ArrayList<CartProduct>()
    init {
        data()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        return ProductHolder(parent.inflate(R.layout.item_product))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        holder.bindHolder(list[position])

    }

    interface Listener{
        fun orderedProducts(orderList :HashSet<String>)
    }

    fun data(){
        DBHelper =  MyDBOpenHelper(context)
        DBHelper!!.getDataBase()
         cartList  = DBHelper?.getAllData()!!
    }

    inner class ProductHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        init {
            setIsRecyclable(true)
        }


        fun bindHolder(product : Product){

            itemView.product_name.text  = product.name
            itemView.price.text = product.price

            Glide.with(itemView.context).applyDefaultRequestOptions(RequestOptions().error(R.mipmap.ic_launcher))
                .load(product.image)
                .into(itemView.image)


            fun addButton(enable : Boolean){
                if (enable){
                    itemView.add.visibility= VISIBLE
                    itemView.minus.visibility= VISIBLE
                    itemView.qty.visibility = VISIBLE
                    itemView.add_button.visibility= GONE
                }else{
                    itemView.add.visibility= GONE
                    itemView.minus.visibility= GONE
                    itemView.qty.visibility = GONE
                    itemView.add_button.visibility= VISIBLE
                }

            }

            fun UpdateCart(){
                if(DBHelper?.isAutho(productid = product.product_id)!!){
                    if (itemView.qty.text.toString().equals("0") ){
                        DBHelper?.deleteData(product.id)
                        orderedList.remove(product.id)
                        listener?.orderedProducts(orderedList)

                    }else{
                        DBHelper?.updateData(product.id,itemView.qty.text.toString())

                    }
                }else{
                    DBHelper?.insertProductData(CartProduct(name = product.name,image = product.image,
                        product_id = product.product_id,quantity = product.quantity,price = product.price))
                    orderedList.add(product.product_id)
                    listener?.orderedProducts(orderedList)

                }

            }

            fun resumeCart(cartValue: Product, cart: ArrayList<CartProduct>){

                cart.forEach {
                    if (it.product_id.equals(cartValue.product_id)){
                        itemView.qty.text = it.quantity
                        addButton(true)
                        orderedList.add(it.product_id)
                        listener?.orderedProducts(orderedList)

                    }
                }


            }

            itemView.add_button.setOnClickListener {
                addButton(true)
                itemView.qty.text = product.quantity
                UpdateCart()
            }

            itemView.add.setOnClickListener {
                if (!itemView.qty.text.toString().equals("0")){
                    addButton(true)
                    itemView.qty.text = (Integer.parseInt(itemView.qty.text.toString()) + 1).toString()
                    UpdateCart()

                }else{

                    addButton(false)
                    "Products Not Available".toast(itemView.context)
                }
            }

            itemView.minus.setOnClickListener {
                if (itemView.qty.text.toString().equals("1")){
                   addButton(false)
                }else{
                    addButton(true)
                }
                itemView.qty.text = (Integer.parseInt(itemView.qty.text.toString()) - 1).toString()
                UpdateCart()

            }

            if(cartList.size >= 0){
                resumeCart(product, cartList)
            }


        }

    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


}