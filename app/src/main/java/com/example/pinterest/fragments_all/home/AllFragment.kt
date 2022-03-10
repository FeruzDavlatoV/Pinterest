package com.example.pinterest.fragments_all.home


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.pinterest.R
import com.example.pinterest.adapter.PhotosAdapter
import com.example.pinterest.networking.ApiClient
import com.example.pinterest.networking.ApiService
import com.example.pinterestdemo.listener.EndlessRecyclerViewScrollListener
import com.mirkamol.retrofitexample.model.ResponceItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AllFragment : Fragment(),SwipeRefreshLayout.OnRefreshListener {

    private lateinit var apiService: ApiService
    lateinit var adapter: PhotosAdapter
    lateinit var swipe: SwipeRefreshLayout
    lateinit var recyclerView: RecyclerView
    private lateinit var list: ArrayList<ResponceItem>
    var per_page = 10
    var page = 1


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_all, container, false)


        recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView1)

        swipe = view.findViewById(R.id.swipe_refresh)

        swipe.setOnRefreshListener(this)



        initViews()

        return view

    }

    override fun onRefresh() {
        initViews()
    }
    fun initViews() {
        list = ArrayList()

        apiService = ApiClient.createServiceWithAuth(ApiService::class.java)

        adapter = PhotosAdapter(requireContext(),list)
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        recyclerView.layoutManager = staggeredGridLayoutManager
        recyclerView.adapter = adapter

        apiPhotosList()

        val scrollListener = object : EndlessRecyclerViewScrollListener(staggeredGridLayoutManager){
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                Toast.makeText(requireContext(), page.toString(), Toast.LENGTH_SHORT).show()
                apiPhotosList()
            }
        }


        recyclerView.addOnScrollListener(scrollListener)

        adapter.itemCLick = {
            findNavController().navigate(R.id.detailFragment)
        }

//        val decoration = SpaceItemDecoration(10)
//        recyclerView.addItemDecoration(decoration)
//        recyclerView.adapter = adapter
    }
    fun apiPhotosList(){
        swipe.isRefreshing = true
        apiService.getPhotos(page++, per_page).enqueue(object : Callback<List<ResponceItem>> {
            override fun onResponse(
                call: Call<List<ResponceItem>>,
                response: Response<List<ResponceItem>>
            ) {
                swipe.isRefreshing = false
                //adapter.itemInserted(response.body() as ArrayList<ResponceItem>)
                list.addAll(response.body() as ArrayList<ResponceItem>)
                adapter.notifyDataSetChanged()

            }

            override fun onFailure(call: Call<List<ResponceItem>>, t: Throwable) {
                swipe.isRefreshing = false
                Log.d("error",t.localizedMessage)
            }

        })
    }




}