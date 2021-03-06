package com.mahmoudbashir.lavadtask.viewModel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mahmoudbashir.lavadtask.MyApp
import com.mahmoudbashir.lavadtask.pojo.Article
import com.mahmoudbashir.lavadtask.pojo.NewsResponse
import com.mahmoudbashir.lavadtask.repository.NewsRepository
import com.mahmoudbashir.lavadtask.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NewsViewModel(val app : Application,val repo : NewsRepository): AndroidViewModel(app) {

    val articles:MutableLiveData<List<Article>> = MutableLiveData()

    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakinNewsPage = 1
    var breakingNewsResponse : NewsResponse? = null


    init {
        getBreakingNews()
    }


    //todo here we receive response data coming from api
    // , increase number of page with one each data request
    // , pass old and new articles to its objects and to MutableList<Article>
    // , return success message (response with Resource class Success Method that carries data (News Response)
    // applying Pagination with number of pages
    private fun handleBreakingNewsResponse(response: Response<NewsResponse>):Resource<NewsResponse>{
        if(response.isSuccessful){
            response.body()?.let {resultResponse ->
                resultResponse.articles.forEach{
                    Log.d("idResults : "," id: ${it.title}")

                }

                breakinNewsPage++
                if (breakingNewsResponse == null)
                {
                    breakingNewsResponse = resultResponse
                }else{
                    val oldArticles = breakingNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(breakingNewsResponse?:resultResponse) }
        }
        return Resource.Error(response.message())
    }

    // todo here we get breaking News (News Feed) by using repository object
    //  , pass received data to mutableLiveData (breakingNews)
    //  , check internet connection so can show no connection message other or exception message with Resource class (Error Method)
    fun getBreakingNews() = viewModelScope.launch {
        breakingNews.postValue(Resource.Loading())

        try {
            if (hasInternetConnection()) {
                val response = repo.getBreakingNews(breakinNewsPage)
                breakingNews.postValue(handleBreakingNewsResponse(response))
            } else {
                breakingNews.postValue(Resource.Error("No Internet Connection"))
            }
        }catch (t : Throwable){
                when(t){
                    is IOException -> breakingNews.postValue(Resource.Error("Network Failure"))
                    else -> breakingNews.postValue(Resource.Error("Conversion Error"))
                }
        }
    }


    // todo here we handle internet connection ,
    //  so if there is no connection will show a toast with no Internet Connection
    private fun hasInternetConnection():Boolean{
        val connectivityManager = getApplication<MyApp>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork)?: return false
            return when{
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }else{
            connectivityManager.activeNetworkInfo?.run {
                return when(type){
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}