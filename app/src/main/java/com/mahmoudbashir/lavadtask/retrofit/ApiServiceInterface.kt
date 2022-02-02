package com.mahmoudbashir.lavadtask.retrofit

import com.mahmoudbashir.lavadtask.pojo.NewsResponse
import com.mahmoudbashir.lavadtask.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServiceInterface {
    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country")
        countryCode : String ="us",
        @Query("page")
        pageNumber : Int=1,
        @Query("apiKey")
        apiKey:String = Constants.API_KEY
    ): Response<NewsResponse>
}