package com.iprism.school.activities

import android.app.AlertDialog
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.parentapp.base.BaseActivity
import com.iprism.school.R
import com.iprism.school.activities.CreateMealActivity
import com.iprism.school.activities.classes.CreateClassActivity
import com.iprism.school.adapters.ClassesAdapter
import com.iprism.school.adapters.FoodItemsAdapter
import com.iprism.school.adapters.FoodTypesAdapter
import com.iprism.school.databinding.ActivityMealPlannerBinding
import com.iprism.school.interfaces.OnFoodClickListener
import com.iprism.school.model.Request.MealPlanListReq
import com.iprism.school.model.Response.MealPlanListResponse
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class MealPlannerActivity : BaseActivity() {

    private lateinit var binding: ActivityMealPlannerBinding
    private val dateFormat = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
    private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private var calendar = Calendar.getInstance()
    private lateinit var crossIv: ImageView
    private lateinit var remarkstxt: TextView
    private lateinit var okBtn: Button
    private var foodType: String = ""

    private var tag: String = ""
    private var teacherId: String = ""
    private var auth_token: String = ""
    private var scl_id: String = ""

    private var selectedDate: String = ""
    private var selected_Type: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealPlannerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        teacherId = userDetails[User.Companion.ID].toString()
        auth_token = userDetails[User.Companion.AUTH_TOKEN].toString()
        scl_id = userDetails[User.Companion.SCHOOL_ID].toString()

        setDate()
        handleBack()
        handleAddBtn()
        hanldeLeftBtn()
        handleRightBtn()

        val genderoptions = arrayOf("Break fast", "Meal","Snacks","Lunch","Evening Snacks")
        binding.foodTypeLo.setOnClickListener {
            // Track the selected option
            var selectedOption = ""
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Choose an Option")
            builder.setSingleChoiceItems(genderoptions, -1) { dialog, which ->
                selectedOption = genderoptions[which] // Capture the selected option
            }
            builder.setPositiveButton("OK") { dialog, _ ->
                if (selectedOption.isNotEmpty()) {
                    selected_Type = selectedOption.toString()
                    binding.foodTypeTxt.text = selectedOption.toString()
                    callMealPlanList()
//                    Toast.makeText(this, "You selected: $selectedOption", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "No option selected", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            builder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            builder.create().show()
        }

        callMealPlanList()

    }

    private fun setDate() {
        val calendar: java.util.Calendar = java.util.Calendar.getInstance()
        val sdf = java.text.SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
        val sdfString = java.text.SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate: String = sdf.format(calendar.time)
        val formattedDateString: String = sdfString.format(calendar.time)
        Log.d("dateFormatString", formattedDateString)
        binding.dateTxt.text = formattedDate

        selectedDate = formattedDateString.toString()
    }

    private fun hanldeLeftBtn() {
        binding.leftArrowIv.setOnClickListener(View.OnClickListener {
            changeDate(-1)
        })
    }

    private fun handleRightBtn() {
        binding.rightArrowIv.setOnClickListener(View.OnClickListener {
            changeDate(1)
        })
    }

    private fun changeDate(days: Int) {
        calendar.add(Calendar.DAY_OF_MONTH, days)
        binding.dateTxt.text = dateFormat.format(calendar.time)
        var dateFormatString = simpleDateFormat.format(calendar.time)
        Log.d("dateFormatString", dateFormatString)
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            val intent  = Intent(this@MealPlannerActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        })
    }

    private fun handleAddBtn() {
        binding.addFoodBtn.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, CreateMealActivity::class.java))
        })
    }

    private fun callMealPlanList() {
        showProgress()
        var apiRequest = MealPlanListReq(auth_token,selectedDate,selected_Type,scl_id,teacherId)
        Log.d("class_ListReq", apiRequest.toString())
        val call: Call<MealPlanListResponse> = parentApiService!!.viewMeal(apiRequest)
        call.enqueue(object : Callback<MealPlanListResponse> {
            override fun onResponse(call: Call<MealPlanListResponse>, response: Response<MealPlanListResponse>) {
                if (response.isSuccessful) {
                    hideProgress()
                    val loginApiResponse = response.body()
                    if (loginApiResponse!!.status == true){

                        binding.nodata.visibility = View.GONE
                        binding.foodsRv.visibility = View.VISIBLE

                        val adap1 = FoodItemsAdapter(this@MealPlannerActivity, loginApiResponse.response.mealplanner)
                        binding.foodsRv.layoutManager = LinearLayoutManager(this@MealPlannerActivity, LinearLayoutManager.VERTICAL, false)
                        binding.foodsRv.adapter = adap1
                        adap1.notifyDataSetChanged()

                        adap1.OnItemCallBack = {
                                mydata ->
                            val mealId = mydata.id.toString()
                            val mealname = mydata.meal_name.toString()
                            val mealtype = mydata.meal_type.toString()
                            val mealremarks = mydata.remarks.toString()

                            val intent = Intent(this@MealPlannerActivity, EditMealPlannerActivity::class.java)
                            intent.putExtra("mealId",mealId)
                            intent.putExtra("mealname",mealname)
                            intent.putExtra("mealtype",mealtype)
                            intent.putExtra("mealremarks",mealremarks)
                            intent.putExtra("tag","edit")
                            startActivity(intent)
                        }
                    }else{
                        binding.nodata.visibility = View.VISIBLE
                        binding.foodsRv.visibility = View.GONE
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@MealPlannerActivity, response.message())
                }
            }
            override fun onFailure(call: Call<MealPlanListResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@MealPlannerActivity, t.message.toString())
            }
        })
    }



    override fun onBackPressed() {
        super.onBackPressed()
        val intent  = Intent(this@MealPlannerActivity, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

}