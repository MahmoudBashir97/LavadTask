package com.mahmoudbashir.lavadtask.fragments

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

class HomeNewsFragment : Fragment(R.layout.fragment_home_news) ,NewsFeedAdapter.OnItemClickedInterface{

    private var _binding : FragmentHomeNewsBinding?=null
    private val homeBinding get() = _binding!!


    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter : NewsFeedAdapter
    lateinit var newsEpoxyController: NewsFeedEpoxyController
    lateinit var mlist:List<Article>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeNewsBinding.inflate(inflater,container,false)


        //Here we do initializing for ViewModel
        viewModel = (activity as MainActivity).viewModel


        return homeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        setUpEpoxyRecyclerView()
//        setUpRecyclerView()
        getNewsFeedArticles()

    }


    private fun setUpEpoxyRecyclerView() {
        mlist = ArrayList()

        newsEpoxyController = NewsFeedEpoxyController((activity as MainActivity)) { article ->
            findNavController().navigate(HomeNewsFragmentDirections.actionHomeNewsFragmentToDetailsNewsFragment())
        }

        homeBinding.recNewsFeed.apply {
            setController(newsEpoxyController)
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@HomeNewsFragment.scrollListener)
        }
    }

    private fun getNewsFeedArticles() {
        //homeBinding.isLoading = true
        newsEpoxyController.isLoading = true

        viewModel.breakingNews.observe(viewLifecycleOwner,{
            result ->
            when(result){
                is Resource.Success ->{
                    result.data?.let {
                        newsResponse->
                        hideProgressBar()
                       // newsAdapter.differ.submitList(newsResponse.articles)

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

    private fun hideProgressBar(){
        homeBinding.paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
        newsEpoxyController.isLoading = false

    }
    private fun showProgressBar(){
        homeBinding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
        newsEpoxyController.isLoading =true
    }

    private fun showToastMessage(message: String) {
        Toast.makeText(activity,"Error Occured: $message",Toast.LENGTH_SHORT).show()

    }

    private fun setUpRecyclerView() {
        newsAdapter = NewsFeedAdapter(this)
        homeBinding.recNewsFeed.apply {
            setHasFixedSize(true)
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@HomeNewsFragment.scrollListener)
        }
    }

    override fun onClickItem(position: Int, article: Article) {
       findNavController().navigate(HomeNewsFragmentDirections.actionHomeNewsFragmentToDetailsNewsFragment())
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

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