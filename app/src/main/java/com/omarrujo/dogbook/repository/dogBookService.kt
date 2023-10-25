package com.omarrujo.dogbook.repository

import DogData
import com.omarrujo.dogbook.utils.Constants
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface dogBookService {
    @GET(Constants.ONE_CALL)
    fun getDog(@Query(Constants.APP_ID_PARAM) appId: String,): Call<List<DogData>>
}