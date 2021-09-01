package com.pd.todo.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.pd.todo.utils.DateConverter
import java.util.*

@Entity(tableName = "todo")
data class ToDo(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val description: String,
    val time: String,
    val date: String,
    val types: Int,
    @TypeConverters(DateConverter::class)
    val date_time: Date,
    val created: String
)
