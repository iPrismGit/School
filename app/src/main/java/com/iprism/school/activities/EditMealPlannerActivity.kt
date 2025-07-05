package com.iprism.school.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.iprism.parentapp.base.BaseActivity
import com.iprism.school.R
import com.iprism.school.activities.CreateMealActivity
import com.iprism.school.databinding.ActivityEditMealPlannerBinding
import com.iprism.school.databinding.ActivityMealPlannerBinding
import com.iprism.school.model.Request.CreateMealReq
import com.iprism.school.model.Request.MealUpdateReq
import com.iprism.school.model.Response.SuccessResponsePojo
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditMealPlannerActivity : BaseActivity() {

    private lateinit var binding: ActivityEditMealPlannerBinding
    private var foodId: String = ""
    private var foodName: String = ""
    private var remarks: String = ""

    private var tag: String = ""
    private var teacherId: String = ""
    private var auth_token: String = ""
    private var scl_id: String = ""

    private var selectedDate: String = ""
    private var selected_Type: String = ""

    private var mealId: String = ""
    private var mealtype: String = ""
    private var mealname: String = ""
    private var mealremarks: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditMealPlannerBinding.inflate(layoutInflater)
        setContentView(binding.root)


        teacherId = userDetails[User.Companion.ID].toString()
        auth_token = userDetails[User.Companion.AUTH_TOKEN].toString()
        scl_id = userDetails[User.Companion.SCHOOL_ID].toString()

        mealId = intent.getStringExtra("mealId").toString()
        mealtype = intent.getStringExtra("mealtype").toString()
        mealname = intent.getStringExtra("mealname").toString()
        mealremarks = intent.getStringExtra("mealremarks").toString()

        binding.nameMealEt.setText(mealname.toString())
        binding.remarkMealEt.setText(mealremarks)

        handleBack()
        handleUpdate()

    }

    private fun handleUpdate() {
        binding.updateBtn.setOnClickListener(View.OnClickListener {
            if (binding.nameMealEt.text.toString() == ""||binding.nameMealEt.text.toString() == null){
                showToast("Enter Meal Name")
            }else if (binding.remarkMealEt.text.toString() == ""||binding.remarkMealEt.text.toString() == null){
                showToast("Enter Remark")
            }else{
                updateMeal()
            }
        })
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun updateMeal() {
        showProgress()
        var apiRequest = MealUpdateReq(auth_token,binding.nameMealEt.text.toString(),mealId,
            binding.remarkMealEt.text.toString(),scl_id,teacherId)
        Log.d("updateMeal", apiRequest.toString())
        val call: Call<SuccessResponsePojo> = parentApiService!!.updateMeal(apiRequest)
        call.enqueue(object : Callback<SuccessResponsePojo> {
            override fun onResponse(call: Call<SuccessResponsePojo>, response: Response<SuccessResponsePojo>) {
                if (response.isSuccessful) {
                    hideProgress()
                    val loginApiResponse = response.body()
                    if (loginApiResponse!!.status == true){
                        hideProgress()

                        val intent = Intent(this@EditMealPlannerActivity,MealPlannerActivity::class.java)
                        startActivity(intent)
                        finish()

                    }else{
                        hideProgress()
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@EditMealPlannerActivity, response.message())
                }
            }
            override fun onFailure(call: Call<SuccessResponsePojo>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@EditMealPlannerActivity, t.message.toString())
            }
        })
    }

}