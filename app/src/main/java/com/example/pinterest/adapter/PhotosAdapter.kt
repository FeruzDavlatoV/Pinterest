package com.example.pinterest.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.pinterest.R
import com.example.pinterest.util.GetDetailsInfo
import com.mirkamol.retrofitexample.model.ResponceItem

class PhotosAdapter(var context: Context, private val list: ArrayList<ResponceItem>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var itemCLick: ((ResponceItem) -> Unit)

    companion object{
        private const val TAG = "PhotosAdapter"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemNotesBinding =  LayoutInflater.from(parent.context).inflate(R.layout.home_recycler_item, parent, false)
        return NoteViewHolder(itemNotesBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val note = list[position]


        if (holder is NoteViewHolder) {
            val tv_title = holder.tv_title
            val iv_photo = holder.photo
            val cardView = holder.card_view
            var photo = holder.photo

            Glide.with(holder.itemView.context)
                .load(note.urls.thumb)
                .placeholder(ColorDrawable(Color.parseColor(note.color)))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iv_photo)



            tv_title.text = note.description


            cardView.setOnClickListener {
//                GetDetailsInfo1.title = note.description.toString()
//                GetDetailsInfo1.links = note.urls.regular.toString()
                GetDetailsInfo.id   = note.id.toString()
                GetDetailsInfo.title = note.description.toString()
                GetDetailsInfo.links = note.urls.small.toString()
                itemCLick.invoke(note)
            }


        }
    }


    override fun getItemCount() = list.size

    private class NoteViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {
        var tv_title = view.findViewById<TextView>(R.id.tv_title)
        val photo = view.findViewById<ImageView>(R.id.item_images1)
        val card_view = view.findViewById<CardView>(R.id.card_view1)
    }

}