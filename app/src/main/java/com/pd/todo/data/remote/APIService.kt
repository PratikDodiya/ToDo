package com.pd.todo.data.remote

import com.pd.todo.data.entities.LoginResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface APIService {

    @Headers("Content-Type: application/json")
    @POST("api/login ")
    suspend fun login(@Body requestBody: RequestBody): Response<LoginResponse>
}