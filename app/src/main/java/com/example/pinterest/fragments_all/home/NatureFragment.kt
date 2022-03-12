package com.example.pinterest.fragments_all.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.pinterest.R
import com.example.pinterest.adapter.PhotosAdapter
import com.example.pinterest.networking.ApiClient
import com.example.pinterest.networking.ApiService
import com.mirkamol.retrofitexample.model.ResponceItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NatureFragment : Fragment() {


    var photos = ArrayList<ResponceItem>()
    lateinit var recyclerView: RecyclerView
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
        val view = inflater.inflate(R.layout.fragment_nature, container, false)

        recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewNature)

        swipe = view.findViewById(R.id.swipe_refresh_nature)



        initViews()

        return view
    }

    private fun initViews() {

        list = ArrayList()
        apiService = ApiClient.createServiceWithAuth(ApiService::class.java)
        adapter = PhotosAdapter(requireContext(),list)

        apiPhotosList()



        swipe.setOnRefreshListener {
            swipe.isRefreshing = false
            photos.clear()
            apiPhotosList()
        }

        recyclerView.setHasFixedSize(true)
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        recyclerView.layoutManager = layoutManager


    }

    fun apiPhotosList(){
        swipe.isRefreshing = true
        apiService.listPhotos1(page++,per_page).enqueue(object : Callback<ArrayList<ResponceItem>> {
            override fun onResponse(
                call: Call<ArrayList<ResponceItem>>,
                response: Response<ArrayList<ResponceItem>>
            ) {
                photos.clear()
                if (response.body() != null)
                    photos.addAll(response.body()!!)
                else
                    Toast.makeText(context, "Limit has ended", Toast.LENGTH_SHORT).show()
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
        recyclerView.adapter = homeTwoAdapter

        //adapterdan fragmentga intent qilish
        homeTwoAdapter.itemCLick = {

            findNavController().navigate(R.id.detailFragment)
        }
    }

}