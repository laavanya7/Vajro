package com.example.vajro.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.vajro.R
import com.example.vajro.RetrofitService.inflate
import com.example.vajro.RetrofitService.toast
import com.example.vajro.model.CartProduct
import kotlinx.android.synthetic.main.item_card_cart.view.*
import kotlinx.android.synthetic.main.item_cart.view.*
import java.text.NumberFormat


class CartAdapter( var list : ArrayList<CartProduct> ,var listener: CartAdapter.Listener?= null): RecyclerView.Adapter<CartAdapter.CartHolder>() {

    var rupee = "\u20B9"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartHolder {
        return CartHolder(parent.inflate(R.layout.item_card_cart))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CartHolder, position: Int) {
        holder.bindHolder(list[position])
    }

    interface Listener{
        fun delete(position: Int )
    }



    inner class CartHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bindHolder(product : CartProduct){
            itemView.product_name.text  = product.name
            itemView.price.text = product.price
            itemView.qty.text = product.quantity
            Glide.with(itemView.context).applyDefaultRequestOptions(RequestOptions().error(R.mipmap.ic_launcher))
                .load(product.image)
                .into(itemView.image)
             var number = product.price.replace("[^0-9]".toRegex(), "")

           var cal = Integer.parseInt(number)*Integer.parseInt(itemView.qty.text.toString())
            val grand_toatl = cal.toString()


            itemView.total.text =  (rupee + grand_toatl)
            itemView.setOnClickListener {
                listener?.delete(layoutPosition)
            }

        }

    }

}
