package com.example.pinterest.fragments_all.search

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.pinterest.R
import com.example.pinterest.adapter.PhotosAdapter
import com.example.pinterest.model.SearchModel
import com.example.pinterest.networking.ApiClient
import com.example.pinterest.networking.ApiService
import com.mirkamol.retrofitexample.model.ResponceItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    private lateinit var apiService: ApiService
    lateinit var adapter: PhotosAdapter
    private lateinit var list: ArrayList<ResponceItem>
    private lateinit var   et_search :EditText
    var per_page = 15
    lateinit var root: View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
         root = inflater.inflate(R.layout.fragment_search, container, false)



        initViews()

        return root
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun initViews() {
        recyclerView = root.findViewById(R.id.recyclerViewSearch)
        et_search = root.findViewById(R.id.et_search)
        list = ArrayList()
        adapter = PhotosAdapter(requireContext(), list)

        apiService = ApiClient.createServiceWithAuth(ApiService::class.java)

        et_search.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard(requireActivity(),et_search)
                searchData(et_search.text.toString())
                return@OnEditorActionListener true
            }
            false
        })

        adapter.itemCLick = {
            findNavController().navigate(R.id.detailFragment, bundleOf("id" to it.id))
        }


    }

    override fun onResume() {
        super.onResume()
        et_search.post {
            et_search.requestFocus()
            val imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(et_search, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun hideKeyboard(activity: Activity, viewToHide: View) {
        val imm = activity
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(viewToHide.windowToken, 0)
    }


    private fun searchData(query: String) {
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        apiService.searchImages(query,per_page)
            .enqueue(object : Callback<SearchModel> {
                override fun onResponse(call: Call<SearchModel>, response: Response<SearchModel>) {
                    list.clear()
                    if (response.body() != null) {
                        list.addAll((response.body()!!.results))
                        Log.d("########", response.body().toString())
                        recyclerView.layoutManager = staggeredGridLayoutManager
                        recyclerView.adapter = adapter
                    }

                    else {
                        Toast.makeText(context, "Limit has ended", Toast.LENGTH_SHORT).show()
                    }

                }

                override fun onFailure(call: Call<SearchModel>, t: Throwable) {

                }

            })
    }

}