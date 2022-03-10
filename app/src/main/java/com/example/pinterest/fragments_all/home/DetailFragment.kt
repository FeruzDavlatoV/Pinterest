package com.example.pinterest.fragments_all.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.pinterest.R
import com.example.pinterest.model.GetDetailsInfo1


class   DetailFragment : Fragment() {
    lateinit var textView: TextView
    lateinit var imageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail, container, false)

        textView = view.findViewById(R.id.text_view)
        imageView = view.findViewById(R.id.image_view)

        textView.text = GetDetailsInfo1.title

        Glide.with(view)
            .load(GetDetailsInfo1.links)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(imageView)

        return view
    }
}