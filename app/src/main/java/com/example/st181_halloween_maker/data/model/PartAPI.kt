package com.girlmaker.create.avatar.creator.model

data class PartAPI(
    val position: String,
    val parts: String,
    val colorArray: String,
    val quantity: Int
)

data class Dream(
        val girlName: String, val partsGirl: List<PartAPI>
)