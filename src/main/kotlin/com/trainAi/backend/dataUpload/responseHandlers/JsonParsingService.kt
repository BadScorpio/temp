package com.trainAi.backend.dataUpload.responseHandlers

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service

@Service
class JsonParsingService(private val objectMapper: ObjectMapper) {

    fun parseJson(jsonString: String): UserUpload {
        val rootNode = objectMapper.readTree(jsonString)
        val dataNode = rootNode.get("data")
        val cid = dataNode.path("cid").asText()
        val id = dataNode.path("id").asText()
        val label = dataNode.path("name").asText()
        val created_at = dataNode.path("created_at").asText()
        val group_id = dataNode.path("group_id").asText()
        return UserUpload(id, label, cid, created_at, group_id)
    }
}
