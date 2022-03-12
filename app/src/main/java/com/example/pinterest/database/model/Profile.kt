package com.example.pinterest.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Saved")
data class Profile(
    @PrimaryKey()
    val saveId: String,
    val url: String,
    val description: String,
)
