package com.mahmoudbashir.lavadtask.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mahmoudbashir.lavadtask.R
import com.mahmoudbashir.lavadtask.pojo.Article

class NewsFeedAdapter(val onClickedItemI:OnItemClickedInterface): RecyclerView.Adapter<NewsFeedAdapter.ViewHolder>() {


    private val differCallback = object : DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,differCallback)

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tv_article_title= itemView.findViewById<TextView>(R.id.tv_article_title)
        val tv_article_desc= itemView.findViewById<TextView>(R.id.tv_article_desc)
        val tv_article_date= itemView.findViewById<TextView>(R.id.tv_article_date)
        val img_article= itemView.findViewById<ImageView>(R.id.img_article_news)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.single_item_layout,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = differ.currentList[position]

        holder.itemView.apply {

            holder.tv_article_title.text = article.title
            holder.tv_article_desc.text = article.description
            holder.tv_article_date.text = article.publishedAt

            Glide.with(this).load(article.urlToImage).placeholder(R.drawable.ic_launcher_background).into(holder.img_article)

            setOnClickListener {
                onClickedItemI.onClickItem(position,article)
            }

        }


    }

    override fun getItemCount(): Int= differ.currentList.size

    interface OnItemClickedInterface{
        fun onClickItem(position:Int,article:Article)
    }
}