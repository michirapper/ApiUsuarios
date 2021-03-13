package com.example.ejemplosebas.pojo

import kotlinx.datetime.LocalDateTime

data class User (
    val id: Int,
    val name: String,
    val birthdate: LocalDateTime
)
