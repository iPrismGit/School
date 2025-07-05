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
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.parentapp.base.BaseActivity
import com.iprism.school.R
import com.iprism.school.activities.ClassesActivity
import com.iprism.school.activities.StaffAttendanceActivity
import com.iprism.school.activities.classes.CreateClassActivity
import com.iprism.school.adapters.ClassesAdapter
import com.iprism.school.adapters.StaffAttendancesAdapter
import com.iprism.school.databinding.ActivityCreateMealBinding
import com.iprism.school.model.Request.ClassListReq
import com.iprism.school.model.Request.CreateMealReq
import com.iprism.school.model.Request.MealPlanListReq
import com.iprism.school.model.Request.StaffAttandanceReq
import com.iprism.school.model.Response.ClassListResponse
import com.iprism.school.model.Response.MealPlanListResponse
import com.iprism.school.model.Response.StaffAttandanceResponse
import com.iprism.school.model.Response.SuccessResponsePojo
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class CreateMealActivity : BaseActivity() {

    private lateinit var binding: ActivityCreateMealBinding

    private var tag: String = ""
    private var teacherId: String = ""
    private var auth_token: String = ""
    private var scl_id: String = ""

    private val dateFormat = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
    private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private var calendar = Calendar.getInstance()
    private lateinit var crossImage: ImageView
    private lateinit var okBtn: Button
    private lateinit var cancelBtn: Button

    private var selectedDate: String = ""
    private var selected_Type: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        teacherId = userDetails[User.Companion.ID].toString()
        auth_token = userDetails[User.Companion.AUTH_TOKEN].toString()
        scl_id = userDetails[User.Companion.SCHOOL_ID].toString()

        setDate()
        handleBack()
        handleSubmitBtn()
        handleFoodTypeLo()
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

        Log.d("selectedDate", selectedDate)
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


    private fun handleFoodTypeLo() {
        binding.foodTypeLo.setOnClickListener(View.OnClickListener {
//            showMealTypeBottomSheet()
        })
    }

    private fun handleSubmitBtn() {
        binding.submitBtn.setOnClickListener(View.OnClickListener {
            if (binding.etMealName.text.toString() == ""||binding.etMealName.text.toString() == null){
                showToast("Enter Meal Name")
            }else if (selectedDate == ""||selectedDate == null){
                showToast("Select Date")
            } else if (selected_Type == ""||selected_Type == null){
                showToast("Select Type")
            } else if (binding.remarksEt.text.toString() == ""||binding.remarksEt.text.toString() == null){
                showToast("Enter Remarks")
            }else{
                createMeal()
            }
        })
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun createMeal() {
        showProgress()
        var apiRequest = CreateMealReq(auth_token,selectedDate,binding.etMealName.text.toString(),
            selected_Type,binding.remarksEt.text.toString(),scl_id,teacherId)
        Log.d("createMeal", apiRequest.toString())
        val call: Call<SuccessResponsePojo> = parentApiService!!.createMeal(apiRequest)
        call.enqueue(object : Callback<SuccessResponsePojo> {
            override fun onResponse(call: Call<SuccessResponsePojo>, response: Response<SuccessResponsePojo>) {
                if (response.isSuccessful) {
                    hideProgress()
                    val loginApiResponse = response.body()
                    if (loginApiResponse!!.status == true){
                        hideProgress()

                        val intent = Intent(this@CreateMealActivity,MealPlannerActivity::class.java)
                        startActivity(intent)
                        finish()

                    }else{
                        hideProgress()
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@CreateMealActivity, response.message())
                }
            }
            override fun onFailure(call: Call<SuccessResponsePojo>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@CreateMealActivity, t.message.toString())
            }
        })
    }




}