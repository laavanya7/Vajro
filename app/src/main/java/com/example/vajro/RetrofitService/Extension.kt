package com.example.vajro.RetrofitService

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

fun ViewGroup.inflate(@LayoutRes layout: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layout, this, attachToRoot)
}

fun RecyclerView.setLayoutManager(context: Context, horizontal:Boolean=false, reverse:Boolean=false){
    if(horizontal){
        this.layoutManager= LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,reverse)
    }else{
        this.layoutManager=LinearLayoutManager(context)
    }
}

fun RecyclerView.setLayoutManager(horizontal:Boolean=false,reverse:Boolean=false){
    if(horizontal){
        this.layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,reverse)
    }else{
        this.layoutManager=LinearLayoutManager(context)
    }
}

fun RecyclerView.addDivider(horizontal:Boolean=false){

    if(!horizontal){
        this.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }else{
        this.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL))
    }
}

fun Any.toast(context: Context) {
    Toast.makeText(context, this.toString(), Toast.LENGTH_SHORT).show()
}

fun Any.toast(context: Context,vararg args: String) {
    String.format(Locale.getDefault(),this.toString(), args.joinToString(",")).toast(context)
}

fun Any.toast(fragment: Fragment, vararg args: String) {
    String.format(Locale.getDefault(),this.toString(), args.joinToString(",")).toast(fragment.context!!)
}

fun Any.toast(fragment: Fragment) {
    String.format(Locale.getDefault(),this.toString()).toast(fragment.context!!)
}