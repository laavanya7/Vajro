package com.example.vajro.model

data class CartProducts (var CartProduct :ArrayList<CartProduct> )

data class CartProduct (
    var name: String,
    var id: String ="0",
    var product_id: String,
    var image: String,
    var quantity: String,
    var price: String
    )
