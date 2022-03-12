package com.example.pinterest.fragments_all.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.pinterest.R
import com.example.pinterest.adapter.PhotosAdapter
import com.example.pinterest.model.GetDetailsInfo1
import com.example.pinterest.networking.ApiClient
import com.example.pinterest.networking.ApiService
import com.google.android.material.imageview.ShapeableImageView
import com.mirkamol.retrofitexample.model.ResponceItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailFragment : Fragment() {
    lateinit var imageView: ImageView
    private lateinit var apiService: ApiService
    lateinit var root: View
    lateinit var adapter: PhotosAdapter
    lateinit var iv_profile: ShapeableImageView
    lateinit var recyclerView: RecyclerView
    private lateinit var list: ArrayList<ResponceItem>
    var isvalid: Boolean = true
    lateinit var tv_profilename: TextView
    lateinit var tv_followers: TextView
    lateinit var text_bio: TextView
    var per_page = 10
    var page = 1

    var id: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        id = arguments?.get("id").toString()
        Log.d("TAG", "onCreate: $id")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.fragment_detail, container, false)

        text_bio = root.findViewById(R.id.text_bio)
        imageView = root.findViewById(R.id.image_view)
        iv_profile = root.findViewById(R.id.iv_profile)
        tv_profilename = root.findViewById(R.id.tv_profilename)
        tv_followers = root.findViewById(R.id.tv_followers)
        text_bio = root.findViewById(R.id.text_bio)
        recyclerView = root.findViewById(R.id.recyclerViewLikeImage)


        apiService = ApiClient.createServiceWithAuth(ApiService::class.java)
        list = ArrayList()


        adapter = PhotosAdapter(requireContext(), list)
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        recyclerView.layoutManager = staggeredGridLayoutManager
        recyclerView.adapter = adapter
        apiPhotosLists()
        apiPhotosList()

        return root
    }



    fun initViews(responceItem: ResponceItem) {
        text_bio.text = responceItem.user.bio
        tv_profilename.text = responceItem.user.username
        tv_followers.text = responceItem.likes.toString()


        Glide.with(root)
            .load(responceItem.urls.small)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(imageView)

        Glide.with(root)
            .load(responceItem.urls.small)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(iv_profile)

    }

    fun apiPhotosList() {

        var id = GetDetailsInfo1.id
        ApiClient.getApiInterface().getImage(id)
            .enqueue(object : Callback<ResponceItem> {
                override fun onResponse(
                    call: Call<ResponceItem>,
                    response: Response<ResponceItem>
                ) {
                    isvalid = false
                    initViews(response.body()!!)
                }

                override fun onFailure(call: Call<ResponceItem>, t: Throwable) {

                }

            })

    }

    fun apiPhotosLists() {
        apiService.getPhotos(page++, per_page).enqueue(object : Callback<List<ResponceItem>> {
            override fun onResponse(call: Call<List<ResponceItem>>, response: Response<List<ResponceItem>>) {
                //adapter.itemInserted(response.body() as ArrayList<ResponceItem>)
                list.addAll(response.body() as ArrayList<ResponceItem>)
                adapter.notifyDataSetChanged()

            }

            override fun onFailure(call: Call<List<ResponceItem>>, t: Throwable) {

            }
        })
    }
}