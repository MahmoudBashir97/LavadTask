package com.mahmoudbashir.lavadtask.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mahmoudbashir.lavadtask.R
import com.mahmoudbashir.lavadtask.adapters.NewsFeedAdapter
import com.mahmoudbashir.lavadtask.databinding.FragmentHomeNewsBinding
import com.mahmoudbashir.lavadtask.epoxyModel.NewsFeedEpoxyController
import com.mahmoudbashir.lavadtask.pojo.Article
import com.mahmoudbashir.lavadtask.ui.MainActivity
import com.mahmoudbashir.lavadtask.utils.Constants
import com.mahmoudbashir.lavadtask.utils.Resource
import com.mahmoudbashir.lavadtask.viewModel.NewsViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeNewsFragment : Fragment(R.layout.fragment_home_news){

    private var _binding : FragmentHomeNewsBinding?=null
    private val homeBinding get() = _binding!!


    lateinit var viewModel: NewsViewModel
    lateinit var newsEpoxyController: NewsFeedEpoxyController
    lateinit var mlist:List<Article>


    override fun onAttach(context: Context) {
        super.onAttach(context)

        //Here we do initializing for ViewModel
        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        _binding = FragmentHomeNewsBinding.inflate(inflater,container,false)
        return homeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setUpEpoxyRecyclerView()
        getNewsFeedArticles()

    }


    //todo in this fun. we setup EpoxyRecyclerview and EpoxyController ,,,
    // also you can see how is it easy to navigate to another screen fragment with EpoxyController and NavController
    private fun setUpEpoxyRecyclerView() {
        mlist = ArrayList()

        newsEpoxyController = NewsFeedEpoxyController((activity as MainActivity)) { article ->
            findNavController().navigate(HomeNewsFragmentDirections.actionHomeNewsFragmentToDetailsNewsFragment(article))
        }

        homeBinding.recNewsFeed.apply {
            setController(newsEpoxyController)
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@HomeNewsFragment.scrollListener)
        }
    }

    //todo here we get data from api(endpoints) by using ViewModel and observe on this data
    // , then pass it to list and epoxyController
    private fun getNewsFeedArticles() {
        newsEpoxyController.isLoading = true

        viewModel.breakingNews.observe(viewLifecycleOwner,{
            result ->
            when(result){
                is Resource.Success ->{
                    result.data?.let {
                        newsResponse->
                        hideProgressBar()

                        mlist = newsResponse.articles
                        Log.d("idResults : "," id: ${mlist.size}")

                        GlobalScope.launch {
                            delay(500)
                        }

                        newsEpoxyController.articlesList = mlist

                        val totalPages = newsResponse.totalResults / Constants.QUERY_PAGE_SIZE +2
                        isLastPage = viewModel.breakinNewsPage == totalPages
                        if (isLastPage){
                            homeBinding.recNewsFeed.setPadding(0,0,0,0)
                        }
                    }
                }
                is Resource.Error->{
                    result.message?.let {
                        hideProgressBar()
                        showToastMessage(it)
                    }
                }
                is Resource.Loading->{
                    showProgressBar()
                }
            }
        })
    }


    //todo this fun. to hide progress bar after data is already loaded and added to list , attached to views
    private fun hideProgressBar(){
        homeBinding.paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
        newsEpoxyController.isLoading = false

    }
    //todo this fun. to show progress bar when data is loading before attach it to views
    private fun showProgressBar(){
        homeBinding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
        newsEpoxyController.isLoading =true
    }

    // todo this fun. only to show message toast if error happens
    private fun showToastMessage(message: String) {
        Toast.makeText(activity,"Error Occured: $message",Toast.LENGTH_SHORT).show()
    }


    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    //todo we do addOnScrollListener so we can listen scrolling on
    // RecyclerView to load new data to be viewed on Screen (add more)
    // applying Pagination with number of pages
    val scrollListener = object : RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount



            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling

            if (shouldPaginate){
                viewModel.getBreakingNews()
                isScrolling = false
            }
        }
    }

}