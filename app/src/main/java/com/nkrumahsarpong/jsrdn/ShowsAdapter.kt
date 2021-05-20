package com.nkrumahsarpong.jsrdn

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ShowsAdapter(val shows: List<Show>):RecyclerView.Adapter<ShowViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.showlayout, parent, false)
        return ShowViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
        return holder.bind(shows[position])
    }

    override fun getItemCount(): Int {
        return shows.size
    }
}

class ShowViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){
    private val ivThumbnail:ImageView = itemView.findViewById(R.id.ivThumbnail)

    fun bind(show:Show){
        Glide.with(itemView.context)
            .load(show.content.thumbnail)
            .into(ivThumbnail)
    }
}