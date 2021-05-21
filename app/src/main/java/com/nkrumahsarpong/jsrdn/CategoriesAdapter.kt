package com.nkrumahsarpong.jsrdn

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CategoriesAdapter(
    private val categories: List<Cat>,
    private val context: Context,
    private val listener: ShowClickListener
):RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.categorylayout, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        return holder.bind(categories[position])

    }

    override fun getItemCount(): Int {
        return categories.size
    }


    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
        private val rvShows: RecyclerView = itemView.findViewById(R.id.rvShows)

        var currentCat: Cat? = null

        init {
            itemView.setOnClickListener {
                currentCat?.let {
                    Toast.makeText(context, it.title, Toast.LENGTH_LONG).show()
                }
            }
        }

        fun bind(category: Cat) {
            tvCategory.text = category.title

            rvShows.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = ShowsAdapter(category.shows, listener)

            }

        }
    }
}