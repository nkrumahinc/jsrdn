package com.nkrumahsarpong.jsrdn

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ShowsAdapter(
    private val shows: List<Show>,
    private val listener: ShowClickListener
):RecyclerView.Adapter<ShowsAdapter.ShowViewHolder>() {

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

    inner class ShowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        private val ivThumbnail: ImageView = itemView.findViewById(R.id.ivThumbnail)
        private var currentShow: Show? = null

        init {
            // using an interface
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
//            listener.onItemClick(currentShow)
            listener.onShowClick(currentShow!!)
        }

        fun bind(show: Show) {
            val thumbnailSplit = show.content.thumbnail.split(":").toTypedArray()
            val thumbnail = "https:${thumbnailSplit[1]}"
            currentShow = show

            Glide.with(itemView.context)
                .load(thumbnail)
                .into(ivThumbnail)
        }
    }
}