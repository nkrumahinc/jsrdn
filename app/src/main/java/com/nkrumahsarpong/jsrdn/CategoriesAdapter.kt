package com.nkrumahsarpong.jsrdn

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CategoriesAdapter (val categories: List<Cat>, context:Context):RecyclerView.Adapter<CategoryViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.categorylayout, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        return holder.bind(categories[position])

    }

    override fun getItemCount(): Int {
        return categories.size
    }
}

class CategoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    val tvCategory:TextView = itemView.findViewById(R.id.tvCategory)
    val rvShows:RecyclerView = itemView.findViewById(R.id.rvShows)

    fun bind(category:Cat){
        tvCategory.text = category.title

        rvShows.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = ShowsAdapter(category.shows)
        }

    }
}