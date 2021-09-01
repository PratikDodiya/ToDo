package com.pd.todo.data.repository

import com.pd.todo.data.entities.ToDo
import com.pd.todo.data.local.ToDoDao
import com.pd.todo.utils.performGetOperation
import javax.inject.Inject

class ToDoRepository  @Inject constructor(
    private val localDataSource: ToDoDao
) {

    fun getAllToDos() = performGetOperation(
        databaseQuery = { localDataSource.getAllToDos() }
    )

    suspend fun insert(toDo: ToDo) {
        localDataSource.insert(toDo)
    }

    suspend fun delete(toDo: ToDo) {
        localDataSource.delete(toDo)
    }
}