package com.example.vajro.model

data class Products(var products : ArrayList<Product>)

data class Product(
    var name: String,
    var id: String,
    var product_id: String,
    var sku: String,
    var image: String,
    var thumb: String,
    var zoom_thumb: String,
    var options: ArrayList<String>? = ArrayList(),
    var description: String,
    var href: String,
    var quantity: String,
    var images: ArrayList<String>? = ArrayList(),
    var price: String,
    var special: String
                    )

