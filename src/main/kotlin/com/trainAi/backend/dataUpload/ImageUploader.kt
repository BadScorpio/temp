package com.trainAi.backend.dataUpload

import com.trainAi.backend.dataUpload.responseHandlers.JsonParsingService
import com.trainAi.backend.dataUpload.responseHandlers.UserUpload
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.springframework.stereotype.Service
import java.io.File

@Service
class ImageUploader(
    private val jsonParsingService: JsonParsingService,
    private val tokenIssuer: TokenIssuer
) {
    fun upload(filePath: String, label: String, groupId: String): UserUpload = runBlocking {
        val client = OkHttpClient()
        val mediaType = "multipart/form-data".toMediaType()
        val body = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart(
                "file", label,
                File(filePath).asRequestBody("application/octet-stream".toMediaType())
            )
            .addFormDataPart("name", label)
            .addFormDataPart("group_id", groupId)
            .build()
        val request = Request.Builder()
            .url("https://uploads.pinata.cloud/v3/files")
            .post(body)
            .addHeader("Authorization", "Bearer ${tokenIssuer.token}")
            .addHeader("Content-Type", "multipart/form-data")
            .build()
        val response = async { client.newCall(request).execute() }
        val result = response.await().body.string()
        val metadata = jsonParsingService.parseJson(result)
        return@runBlocking metadata
    }

    fun getImageURL(cid: String): String = runBlocking {
        val url = "https://api.pinata.cloud/v3/files/sign"
        val token = tokenIssuer.token
        val currentTime = System.currentTimeMillis() / 1000.also { println(it) }

        // JSON payload
        val json = """
        {
            "url": "https://lavender-efficient-wren-990.mypinata.cloud/files/$cid",
            "expires": 172800,
            "date": $currentTime,
            "method": "GET"
        }
    """.trimIndent()

        // Create a request body with JSON payload
        val mediaType = "application/json".toMediaType()
        val requestBody = json.toRequestBody(mediaType)

        val client = OkHttpClient()

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .addHeader("Authorization", "Bearer $token")
            .addHeader("Content-Type", "application/json")
            .build()
        val response = async { client.newCall(request).execute() }
        val result = response.await().body.string()
        val mapper = jacksonObjectMapper()
        val root = mapper.readTree(result)
        return@runBlocking root["data"].asText()
    }

    fun listFiles(groupId: String):List<UserUpload> = runBlocking {
        val url = "https://api.pinata.cloud/v3/files?group=$groupId"
        val token = tokenIssuer.token

        val client = OkHttpClient()

        val request = Request.Builder()
            .url(url)
            .get()
            .addHeader("Authorization", "Bearer $token")
            .build()
        val resp = async{ client.newCall(request).execute() }
        val result = resp.await().body.string()
        val mapper = jacksonObjectMapper()
        val rootNode = mapper.readTree(result)

        val filesNode = rootNode.path("data").path("files")
        val userUploads = filesNode.map { fileNode ->
            UserUpload(
                id = fileNode["id"].asText(),
                label = fileNode["name"].asText(),
                cid = fileNode["cid"].asText(),
                created_at = fileNode["created_at"].asText(),
                group_id = fileNode["group_id"].asText()
            )
        }
        return@runBlocking userUploads
    }
}
