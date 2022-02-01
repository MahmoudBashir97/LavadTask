package com.mahmoudbashir.lavadtask.repository

import com.mahmoudbashir.lavadtask.retrofit.RetrofitInstance

class NewsRepository(private val retroInstance:RetrofitInstance):IRepository {
    override suspend fun getBreakingNews(pageNo:Int) = retroInstance.api.getBreakingNews(pageNumber = pageNo)


}