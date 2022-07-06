package com.example.pinterest.fragments_all.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.pinterest.R
import com.example.pinterest.adapter.PhotosAdapter
import com.example.pinterest.adapter.SavedPhotoAdapter
import com.example.pinterest.database.SavedDatabase
import com.example.pinterest.database.model.Profile
import com.example.pinterest.model.profile.User
import com.example.pinterest.networking.ApiClient
import com.example.pinterest.networking.ApiService
import com.google.android.material.imageview.ShapeableImageView
import com.mirkamol.retrofitexample.model.ResponceItem
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment(R.layout.fragment_profile){

    private lateinit var apiService: ApiService
    private lateinit var ivProfile: ShapeableImageView
    private lateinit var tvFullName: TextView
    private lateinit var tvUsername: TextView
    private lateinit var tvFollow: TextView

    private lateinit var rvSaved: RecyclerView
    private lateinit var staggeredGridLayoutManager: StaggeredGridLayoutManager
    private lateinit var savedPhotoAdapter: SavedPhotoAdapter

    private lateinit var savedDatabase: SavedDatabase
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        apiService = ApiClient.createServiceWithAuth(ApiService::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
    }

    private fun initViews(view: View) {


        ivProfile = view.findViewById(R.id.ivProfile)

        getUser()
        tvFullName = view.findViewById(R.id.tv_main_profile)
        tvUsername = view.findViewById(R.id.tv_main_email)
        tvFollow = view.findViewById(R.id.tv_count1)
        rvSaved = view.findViewById(R.id.recyclerViewProfile)

        staggeredGridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        rvSaved.layoutManager = staggeredGridLayoutManager
        savedPhotoAdapter = SavedPhotoAdapter()

//        navController = Navigation.findNavController(view)

        getSaved()
//        controlClick()
    }

    private fun getSaved() {
        savedDatabase = SavedDatabase.getDatabase(requireContext())


        savedPhotoAdapter.submitData(savedDatabase.savedDao().getSaved())
        rvSaved.adapter = savedPhotoAdapter
    }

//    private fun controlClick() {
//        savedPhotoAdapter.photoClick = {
//            navController.navigate(
//                R.id.detailFragment,
//                bundleOf(
//                    "photoID" to it.saveId,
//                    "photoUrl" to it.url,
//                    "description" to it.description
//                )
//            )
//        }
//    }

    fun getUser() {
        apiService.getUser("feruz_davlatov").enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                var user = response.body()
                if (response.body()!!.profileImage != null) {
                    Picasso.get()
                        .load(user!!.profileImage.large)
                        .into(ivProfile)

                    tvFullName.text = user.name
                    tvUsername.text = "@${user.username}"
                    tvFollow.text = "${user.followers_count} followers · ${user.following_count} following"
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
            }

        })
    }
}