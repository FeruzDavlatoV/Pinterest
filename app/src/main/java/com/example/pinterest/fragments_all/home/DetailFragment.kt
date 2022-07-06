package com.example.pinterest.fragments_all.home

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.pinterest.R
import com.example.pinterest.adapter.PhotosAdapter
import com.example.pinterest.database.SavedDatabase
import com.example.pinterest.database.model.Profile
import com.example.pinterest.networking.ApiClient
import com.example.pinterest.networking.ApiService
import com.example.pinterest.util.GetDetailsInfo
import com.example.pinterest.util.RandomColor
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.imageview.ShapeableImageView
import com.mirkamol.retrofitexample.model.ResponceItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


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
    lateinit var textSave:TextView
    var per_page = 10
    var page = 1
    private lateinit var savedDatabase: SavedDatabase

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
        textSave = root.findViewById(R.id.saveTv)

        savedDatabase = SavedDatabase.getDatabase(requireContext())

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
            .placeholder(R.drawable.img)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(imageView)

        Glide.with(root)
            .load(responceItem.urls.small)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(iv_profile)

        textSave.setOnClickListener {
            showBottomSheet()
        }

        adapter.itemCLick={
            findNavController().navigate(R.id.detailFragment)
        }

    }
    private fun showBottomSheet() {
        val view: View = layoutInflater.inflate(R.layout.bottom_sheet, null)
        val textGallery: TextView = view.findViewById(R.id.textGallery)
        val textProfile: TextView = view.findViewById(R.id.textProfile)
        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(view)

        textGallery.setOnClickListener {
            saveToGallery(imageView)
            dialog.dismiss()
        }
        textProfile.setOnClickListener {
            saveToDatabase()
            dialog.dismiss()
        }
        dialog.show()
    }
    private fun saveToDatabase() {
        if (GetDetailsInfo.title.isNullOrEmpty()) {
            savedDatabase.savedDao()
                .saveProduct(
                    Profile(
                        GetDetailsInfo.id!!,
                        GetDetailsInfo.links!!,
                        GetDetailsInfo.title!!
                    )
                )
        } else {
            savedDatabase.savedDao()
                .saveProduct(
                    Profile(
                        GetDetailsInfo.id!!,
                        GetDetailsInfo.links!!,
                        ""
                    )
                )
        }
        Toast.makeText(context,"Saved To Profile", Toast.LENGTH_SHORT).show()
    }

    private fun getScreenShotFromView(v: View): Bitmap? {
        var screenshot: Bitmap? = null

        try {
            screenshot =
                Bitmap.createBitmap(v.measuredWidth, v.measuredHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(screenshot)
            v.draw(canvas)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return screenshot
    }

    private fun saveToGallery(iv_image: ImageView) {
        val bitmap = getScreenShotFromView(iv_image)

        if (bitmap != null) {
            saveMediaToStorage(bitmap)
        }
    }

    private fun saveMediaToStorage(bitmap: Bitmap) {
        val filename = "${System.currentTimeMillis()}.jpg"

        var fos: OutputStream? = null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            context?.contentResolver?.also { resolver ->

                val contentValues = ContentValues().apply {

                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }

                val imageUri: Uri? =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

                fos = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else {
            val imagesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
        }

        fos?.use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
        }
    }

    fun apiPhotosList() {

        var id = GetDetailsInfo.id
        ApiClient.getApiInterface().getImage(id.toString())
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
            override fun onResponse(
                call: Call<List<ResponceItem>>,
                response: Response<List<ResponceItem>>
            ) {
                //adapter.itemInserted(response.body() as ArrayList<ResponceItem>)
                list.addAll(response.body() as ArrayList<ResponceItem>)
                adapter.notifyDataSetChanged()

            }
            override fun onFailure(call: Call<List<ResponceItem>>, t: Throwable) {
            }
        })
    }
}