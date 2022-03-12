package com.example.pinterest.fragments_all.home

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pinterest.R
import android.util.Log
import android.widget.Toast

import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.comix.overwatch.HiveProgressView
import com.example.pinterest.adapter.PhotosAdapter
import com.example.pinterest.networking.ApiClient
import com.example.pinterest.networking.ApiService
import com.mirkamol.retrofitexample.model.ResponceItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class WallPaperFragment : Fragment() {

    var photos = ArrayList<ResponceItem>()
    lateinit var recyclerView2: RecyclerView
    private lateinit var apiService: ApiService
    lateinit var adapter: PhotosAdapter
    lateinit var swipe: SwipeRefreshLayout
    private lateinit var list: ArrayList<ResponceItem>
//    lateinit var progressBar2: HiveProgressView
    var page = 1
    var per_page =10

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_wall_paper, container, false)

        recyclerView2 = view!!.findViewById(R.id.recyclerView2)
        swipe = view!!.findViewById(R.id.swipe_refresh2)

        initViews()
        return view
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun initViews() {


//        progressBar2 = view.findViewById(R.id.progress_bar2)

        list = ArrayList()
        apiService = ApiClient.createServiceWithAuth(ApiService::class.java)
        adapter = PhotosAdapter(requireContext(),list)

        apiPhotosList()



        swipe.setOnRefreshListener {
            swipe.isRefreshing = false
            photos.clear()
            apiPhotosList()
        }

        recyclerView2.setHasFixedSize(true)
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        recyclerView2.layoutManager = layoutManager
    }

    fun apiPhotosList(){
        swipe.isRefreshing = true
        apiService.listPhotos2().enqueue(object : Callback<ArrayList<ResponceItem>> {
            override fun onResponse(
                call: Call<ArrayList<ResponceItem>>,
                response: Response<ArrayList<ResponceItem>>
            ) {
                photos.clear()
                if (response.body() != null)
                    photos.addAll(response.body()!!)
                else
                swipe.isRefreshing = false
//                progressBar2.visibility = View.GONE
                refreshAdapter(photos)
            }

            override fun onFailure(call: Call<ArrayList<ResponceItem>>, t: Throwable) {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
//                progressBar2.visibility = View.GONE
            }

        })
    }


    fun refreshAdapter(photos: ArrayList<ResponceItem>){
        val homeTwoAdapter = PhotosAdapter(requireContext(),photos)
        recyclerView2.adapter = homeTwoAdapter

        //adapterdan fragmentga intent qilish
        homeTwoAdapter.itemCLick = {

            findNavController().navigate(R.id.detailFragment)
        }
    }

}