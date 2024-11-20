package com.trainAi.backend

import com.trainAi.backend.dataUpload.TokenIssuer
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication


@SpringBootApplication
@EnableConfigurationProperties(TokenIssuer::class)
class DataCollectionApplication

fun main(args: Array<String>) {
	runApplication<DataCollectionApplication>(*args)
}
