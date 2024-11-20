package com.trainAi.backend.dataUpload.groups

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service

@Service
class GroupParser(private val objectMapper: ObjectMapper) {
    fun parseJson(jsonString: String): GroupData {
        val rootNode = objectMapper.readTree(jsonString)
        val dataNode = rootNode.get("data")
        val id = dataNode.path("id").asText()
        val groupName = dataNode.path("name").asText()
        val createdAt = dataNode.path("created_at").asText()
        return GroupData(id,createdAt,groupName)
    }
}

data class GroupData(
    val id:String,
    val createdAt:String,
    val groupName:String,
)