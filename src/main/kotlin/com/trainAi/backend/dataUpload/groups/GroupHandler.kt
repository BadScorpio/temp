package com.trainAi.backend.dataUpload.groups

import com.trainAi.backend.dataUpload.TokenIssuer
import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.stereotype.Service

@Service
class GroupHandler(
    private val groupParser: GroupParser,
    private val objectMapper: ObjectMapper,
    private val tokenIssuer: TokenIssuer
) {
    fun listAllGroups():List<GroupData> = runBlocking{
        val url = "https://api.pinata.cloud/v3/files/groups"
        val token =
            "${tokenIssuer.token}"
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .get()
            .addHeader("Authorization", "Bearer $token")
            .build()
        val response = async { client.newCall(request).execute() }
        val result = response.await().body.string()
        val root = objectMapper.readTree(result)
        val groupList = root["data"]["groups"]
        val groups = groupList.map { groupNode ->
            GroupData(
                id = groupNode["id"].asText(),
                createdAt = groupNode["created_at"].asText(),
                groupName = groupNode["name"].asText()
            )
        }
        return@runBlocking groups
    }

    fun getGroupById(id:String): GroupData = runBlocking{
        val url = "https://api.pinata.cloud/v3/files/groups/$id"
        val token = "${tokenIssuer.token}"
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(url)
            .get()
            .addHeader("Authorization", "Bearer $token")
            .build()

        val response = async { client.newCall(request).execute() }
        val result = response.await().body.string()
        val group = groupParser.parseJson(result)
        return@runBlocking group
    }

    fun deleteGroup(id:String):String = runBlocking{
        val url = "https://api.pinata.cloud/v3/files/groups/$id"
        val token = "${tokenIssuer.token}"

        val client = OkHttpClient()

        val request = Request.Builder()
            .url(url)
            .delete()
            .addHeader("Authorization", "Bearer $token")
            .build()

        val response = async { client.newCall(request).execute() }
        val result = response.await().body.string()
        try {
            return@runBlocking "Successfully deleted group:$id"
        }catch (e:Exception){return@runBlocking "Error deleting group"}

    }
}