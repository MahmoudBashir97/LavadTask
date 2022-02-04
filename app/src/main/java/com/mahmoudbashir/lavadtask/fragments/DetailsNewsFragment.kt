package com.mahmoudbashir.lavadtask.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.mahmoudbashir.lavadtask.R
import com.mahmoudbashir.lavadtask.databinding.FragmentDetailsNewsBinding
import com.mahmoudbashir.lavadtask.pojo.Article
import com.mahmoudbashir.lavadtask.ui.MainActivity

class DetailsNewsFragment : Fragment(R.layout.fragment_details_news) {

    private var _binding : FragmentDetailsNewsBinding? = null
    private val detailBinding get() = _binding!!

    val args:DetailsNewsFragmentArgs by navArgs()
    lateinit var article: Article


    override fun onAttach(context: Context) {
        super.onAttach(context)
        // todo here we are receiving an article by using navArgs for this Fragment
        article = args.articleDetails

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailsNewsBinding.inflate(inflater, container,false)
        return detailBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setUpViewsDetails()
    }

    //todo Here we are attaching article details to views
    private fun setUpViewsDetails() {

        detailBinding.apply {

            txtArticleSource.text = article.source?.name
            txtArticleDate.text = article.publishedAt
            txtArticleTitle.text = article.title
            txtArticleContent.text = article.content
            txtArticleDescription.text = article.description

            txtArticleUrl.apply {
                text = article.url

                setOnClickListener{
                    val i = Intent(Intent.ACTION_VIEW,Uri.parse(article.url))
                    startActivity(i)
                }
            }

            Glide.with((activity as MainActivity)).load(article.urlToImage).placeholder(R.drawable.ic_launcher_background).into(imgUrl)


            // todo here we navigate up to previous screen using NavController
            backBtn.setOnClickListener {
                findNavController().navigateUp()
            }

        }

    }

}