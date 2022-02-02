package com.mahmoudbashir.lavadtask.epoxyModel

import android.content.Context
import com.bumptech.glide.Glide
import com.mahmoudbashir.lavadtask.MyApp
import com.mahmoudbashir.lavadtask.R
import com.mahmoudbashir.lavadtask.databinding.SingleItemLayoutBinding
import com.mahmoudbashir.lavadtask.pojo.Article

data class EpoxyModelItem(
    val context:Context,
    val article:Article,
    val onClicked : (Article) -> Unit
):ViewBindingKotlinModel<SingleItemLayoutBinding>(R.layout.single_item_layout){
    override fun SingleItemLayoutBinding.bind() {


        tvArticleTitle.text = article.title
        tvArticleDesc.text = article.description
        tvArticleDate.text = article.publishedAt

        Glide.with(context).load(article.urlToImage).placeholder(R.drawable.ic_launcher_background).into(imgArticleNews)


        root.setOnClickListener {
            onClicked(article)
        }
    }

}
