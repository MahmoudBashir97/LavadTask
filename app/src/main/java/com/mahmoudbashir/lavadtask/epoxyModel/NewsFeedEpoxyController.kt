package com.mahmoudbashir.lavadtask.epoxyModel

import android.content.Context
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.airbnb.epoxy.EpoxyController
import com.mahmoudbashir.lavadtask.pojo.Article

class NewsFeedEpoxyController (val context: Context,private val onClickedCallback:(Article) -> Unit): EpoxyController(){



    var isLoading: Boolean = false
   /* set(value) {
        field = value
        if (field){
            requestModelBuild()
        }
    }*/

    //todo is a list of articles
    var articlesList:List<Article> = ArrayList()

    set(value) {
        field = value
        isLoading = false
        requestModelBuild()
    }


    override fun buildModels() {
        if (isLoading){
            //todo you can show loading state
            return
        }
        if (articlesList.isEmpty()){
            //todo show empty state
            return
        }

        //todo fetching article list and pass all its data to EpoxyModelItem to be attached to views
        articlesList.forEach { article ->
            EpoxyModelItem(context,article,onClickedCallback).id(article.title).addTo(this)
        }
    }

}