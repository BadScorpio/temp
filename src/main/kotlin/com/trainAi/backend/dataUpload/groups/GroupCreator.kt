package com.trainAi.backend.dataUpload.groups

import com.trainAi.backend.dataUpload.TokenIssuer
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.springframework.stereotype.Service

@Service
class GroupCreator(
    private val groupParser: GroupParser,
    private val tokenIssuer: TokenIssuer,
){

    fun createNewGroup(groupName:String): GroupData = runBlocking{
        val client = OkHttpClient()

        val mediaType = "application/json".toMediaType()

        val requestBody = """{
        "name": "$groupName",
        "is_public": true
    }""".toRequestBody(mediaType)

        val request = Request.Builder()
            .url("https://api.pinata.cloud/v3/files/groups")
            .post(requestBody)
            .addHeader("Authorization", "Bearer ${tokenIssuer.token}")
            .addHeader("Content-Type", "application/json")
            .build()

        val response = async { client.newCall(request).execute() }
        val result = response.await().body.string()
        return@runBlocking groupParser.parseJson(result)
    }
}