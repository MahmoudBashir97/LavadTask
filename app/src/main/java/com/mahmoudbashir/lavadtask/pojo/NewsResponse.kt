package com.mahmoudbashir.lavadtask.pojo

data class NewsResponse(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)