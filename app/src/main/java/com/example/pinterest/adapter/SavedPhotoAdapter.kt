package com.example.pinterest.adapter


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pinterest.R
import com.example.pinterest.database.model.Profile
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class SavedPhotoAdapter() :
    RecyclerView.Adapter<SavedPhotoAdapter.SavedPhotoVH>() {

    private var photos: ArrayList<Profile> = ArrayList()
    lateinit var photoClick: ((Profile) -> Unit)

    inner class SavedPhotoVH(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(photo: Profile) {
            val ivHomePhoto: ImageView = view.findViewById(R.id.item_images1)
            val tvHomePhotoTitle: TextView = view.findViewById(R.id.tv_home_title)

            Picasso.get()
                .load(photo.url)
                .into(ivHomePhoto, object : Callback {
                    override fun onSuccess() {
                        if (photo.description != null) {
                            tvHomePhotoTitle.text = photo.description
                        }
                    }

                    override fun onError(e: Exception?) {

                    }
                })

            ivHomePhoto.setOnClickListener {
                photoClick.invoke(photo)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedPhotoVH {
        return SavedPhotoVH(
            LayoutInflater.from(parent.context).inflate(R.layout.home_recycler_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SavedPhotoVH, position: Int) {
        holder.bind(getItem(position))

    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitData(list: List<Profile>) {
        photos.addAll(list)
        notifyDataSetChanged()
    }

    private fun getItem(position: Int): Profile = photos[position]

    override fun getItemCount(): Int = photos.size
}