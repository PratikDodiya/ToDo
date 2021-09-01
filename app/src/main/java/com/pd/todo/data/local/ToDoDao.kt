package com.pd.todo.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.pd.todo.data.entities.ToDo

@Dao
interface ToDoDao {

    @Query("SELECT * FROM todo")
    fun getAllToDos() : LiveData<List<ToDo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todo: ToDo)

    @Delete
    suspend fun delete(todo: ToDo)

}

