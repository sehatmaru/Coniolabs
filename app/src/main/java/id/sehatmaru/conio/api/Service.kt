package id.sehatmaru.conio.api

import id.sehatmaru.conio.model.request.LoginRequest
import id.sehatmaru.conio.model.response.LoginResponse
import retrofit2.Call
import retrofit2.http.*

interface Service {
    @POST("api/v1/auth/signin")
    fun login(
        @Body request: LoginRequest
    ): Call<LoginResponse>
}