package com.girlmaker.create.avatar.creator.model

data class ItemNavCustomModel(
    val path: String,
    var isSelected: Boolean= false,
    val listImageColor: ArrayList<ItemColorImageModel> = arrayListOf()
)