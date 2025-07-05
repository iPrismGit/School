package com.iprism.school.activities.album

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.iprism.parentapp.base.BaseActivity
import com.iprism.school.activities.HomeActivity
import com.iprism.school.adapters.AlbumsAdapter
import com.iprism.school.databinding.ActivityAlbumsBinding
import com.iprism.school.model.Request.SchoolStaffReq
import com.iprism.school.model.Response.AlbumsListResponse
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AlbumsActivity : BaseActivity() {

    private lateinit var binding: ActivityAlbumsBinding
    private var teacherId: String = ""
    private var auth_token: String = ""
    private var scl_id: String = ""
    private var emp_designation: String = ""
    private var emp_name: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlbumsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(Manifest.permission.READ_MEDIA_VIDEO), 100)
        } else {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 100)
        }

        teacherId = userDetails[User.Companion.ID].toString()
        auth_token = userDetails[User.Companion.AUTH_TOKEN].toString()
        scl_id = userDetails[User.Companion.SCHOOL_ID].toString()

        emp_name = userDetails[User.Companion.EMP_NAME].toString()
        emp_designation = userDetails[User.Companion.EMP_DESIGNATION].toString()

        allAlbum()
        handleAddBtn()

        binding.backIv.setOnClickListener {
            val intent = Intent(this@AlbumsActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

    }


    private fun handleAddBtn() {
        binding.addBtn.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, CreateAlbumsActivity::class.java))
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@AlbumsActivity, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun allAlbum() {
        showProgress()
        var apiRequest = SchoolStaffReq(auth_token,scl_id,teacherId)
        Log.d("uploadAlbum_Req", apiRequest.toString())
        val call: Call<AlbumsListResponse> = parentApiService!!.albumList(apiRequest)
        call.enqueue(object : Callback<AlbumsListResponse> {
            override fun onResponse(call: Call<AlbumsListResponse>, response: Response<AlbumsListResponse>) {
                if (response.isSuccessful) {
                    hideProgress()
                    val loginApiResponse = response.body()

                    if (loginApiResponse!!.status == true){
                        binding.nodataTv.visibility = View.GONE
                        binding.albumsRv.visibility = View.VISIBLE

                        var albumsAdapter = AlbumsAdapter(this@AlbumsActivity, loginApiResponse.response.album_details)
                        binding.albumsRv.adapter = albumsAdapter
                        var linearLayoutManager = GridLayoutManager(this@AlbumsActivity, 2)
                        binding.albumsRv.layoutManager = linearLayoutManager

                        albumsAdapter.OnItemBtn = {
                                mydata ->
                            val studentId = mydata.id.toString()
                            val intent = Intent(this@AlbumsActivity, AlbumDetailsActivity::class.java)
                            intent.putExtra("studentId",studentId)
                            intent.putExtra("albumId",studentId)
                            startActivity(intent)
                        }
                    }else{
                        binding.nodataTv.visibility = View.VISIBLE
                        binding.albumsRv.visibility = View.GONE
                    }
                } else {
                    binding.nodataTv.visibility = View.VISIBLE
                    binding.albumsRv.visibility = View.GONE

                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@AlbumsActivity, response.message())
                }
            }
            override fun onFailure(call: Call<AlbumsListResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@AlbumsActivity, t.message.toString())
            }
        })
    }

}