package com.girlmaker.create.avatar.creator.model

import com.girlmaker.create.avatar.creator.model.ColorModel

data class LayerModel(
    val image: String,
    val isMoreColors: Boolean = false,
    var listColor: ArrayList<ColorModel> = arrayListOf()
)