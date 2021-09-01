package com.pd.todo.data.remote

import okhttp3.RequestBody
import javax.inject.Inject

class LoginRemoteDataSource  @Inject constructor(
    private val toDoService: APIService
): BaseDataSource() {

    suspend fun login(requestBody: RequestBody) = getResult { toDoService.login(requestBody) }
}