package com.trainAi.backend.dataUpload.responseHandlers

data class UserUpload(
    val id: String,
    val label: String,
    val cid: String,
    val created_at: String,
    val group_id: String,
)
