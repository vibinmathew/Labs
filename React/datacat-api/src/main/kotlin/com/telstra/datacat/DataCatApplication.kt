package com.telstra.datacat

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class DataCatApplication

fun main(args: Array<String>) {
    SpringApplication.run(DataCatApplication::class.java, *args)
}
