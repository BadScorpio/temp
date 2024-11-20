package com.trainAi.backend.controller

import com.trainAi.backend.dataUpload.ImageUploader
import com.trainAi.backend.dataUpload.groups.GroupCreator
import com.trainAi.backend.dataUpload.groups.GroupData
import com.trainAi.backend.dataUpload.groups.GroupHandler
import com.trainAi.backend.dataUpload.responseHandlers.UserUpload
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/data")
class UploadController(
    private val groupCreator: GroupCreator,
    private val groupHandler: GroupHandler,
    private val imageUploader: ImageUploader,
) {
    //tested
    @PostMapping("/createGroup")
    fun createGroup(@RequestParam name:String): GroupData = groupCreator.createNewGroup(name)

    //tested
    @GetMapping("/groups")
    fun listGroups():List<GroupData> = groupHandler.listAllGroups()

    //tested
    @GetMapping("/groups/{id}")
    fun getGroupById(@PathVariable id:String): GroupData = groupHandler.getGroupById(id)

    //tested
    @DeleteMapping("/delete/{id}")
    fun removeGroup(@PathVariable id:String):String = groupHandler.deleteGroup(id)

    //tested
    @PostMapping("/upload")
    fun uploadLabelledData(@RequestBody upload: Upload): UserUpload = imageUploader.upload(upload.filePath,upload.label,upload.groupId)

    //tested
    @GetMapping("/link/{cid}")
    fun getImageURL(@PathVariable cid:String):String = imageUploader.getImageURL(cid)

    //tested
    @GetMapping("/files/{groupId}")
    fun getFilesByGid(@PathVariable groupId: String):List<UserUpload> = imageUploader.listFiles(groupId)
}

data class Upload(
    val filePath:String,
    val label:String,
    val groupId:String,
)
