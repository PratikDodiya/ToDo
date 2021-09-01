package com.pd.todo.data.repository

import com.pd.todo.data.entities.ToDo
import com.pd.todo.data.local.ToDoDao
import com.pd.todo.data.remote.LoginRemoteDataSource
import com.pd.todo.utils.performGetOperation
import com.pd.todo.utils.performPostOperation
import okhttp3.RequestBody
import javax.inject.Inject

class LoginRepository  @Inject constructor(
    private val remoteDataSource: LoginRemoteDataSource,
    private val localDataSource: ToDoDao
) {
    fun login(requestBody: RequestBody) = performPostOperation(
        networkCall = { remoteDataSource.login(requestBody) }
    )

    /*fun getToDos() = performGetOperation(
        databaseQuery = { localDataSource.getAllToDos() },
        saveCallResult = { localDataSource.insert(it[0]) }
    )*/

    /*fun getAllToDos() = performGetOperation(
        databaseQuery = { localDataSource.getAllToDos() }
    )

    suspend fun insert(toDo: ToDo) {
        localDataSource.insert(toDo)
    }*/
}