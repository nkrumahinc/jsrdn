package com.nkrumahsarpong.jsrdn

import retrofit2.http.GET
import retrofit2.Call

interface Endpoint {
    @GET("interview.json")
    fun get(): Call<Received>
}