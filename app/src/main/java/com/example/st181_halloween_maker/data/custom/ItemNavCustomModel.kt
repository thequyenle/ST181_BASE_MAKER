package com.example.st181_halloween_maker.data.custom

data class ItemNavCustomModel(
    val path: String,
    var isSelected: Boolean= false,
    val listImageColor: ArrayList<ItemColorImageModel> = arrayListOf()
)