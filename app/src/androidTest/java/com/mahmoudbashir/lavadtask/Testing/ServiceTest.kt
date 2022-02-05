package com.mahmoudbashir.lavadtask.Testing

import com.mahmoudbashir.lavadtask.retrofit.ApiServiceInterface
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.mahmoudbashir.lavadtask.pojo.NewsResponse
import okhttp3.OkHttpClient
import retrofit2.Call
import strikt.api.expectThat

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ServiceTest {

    @Mock
    lateinit var apiService : ApiServiceInterface

    @Before
    fun setUp(){
        MockitoAnnotations.initMocks(this)
        val retrofit  = Retrofit.Builder()
            .baseUrl("https://newsapi.org")
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient())
            .build()
        apiService = retrofit.create(ApiServiceInterface::class.java)
    }

    @Test
    fun getDataTest() = runBlockingTest{

        val call :Call<NewsResponse> = apiService.getBreakingNewsTest()
        expectThat(call.request()){

            assertThat("is GET method"){
                it.method == "GET"
            }

            assertThat("GET method"){
                it.body == NewsResponse::class.java
            }
        }

    }

}