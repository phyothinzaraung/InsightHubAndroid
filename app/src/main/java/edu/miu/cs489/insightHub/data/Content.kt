package edu.miu.cs489.insightHub.data

data class Content(
    val contentId: Int = 0,
    val contentTitle: String ="",
    val contentDescription: String="",
    val categoryId: Int =0,
    val userId: Int=0
)
