package com.mahmoudbashir.lavadtask.repository

import com.mahmoudbashir.lavadtask.pojo.NewsResponse
import retrofit2.Response

interface IRepository {
    suspend fun getBreakingNews():Response<NewsResponse>
}