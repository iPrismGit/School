package com.iprism.school.repositories

import android.util.Log
import com.iprism.school.network.RetrofitClient
import androidx.lifecycle.MutableLiveData
import com.iprism.school.model.Request.CalenderDeleteReq
import com.iprism.school.model.Request.CalenderImgDeleteReq
import com.iprism.school.model.Request.CalenderImgUpdateReq
import com.iprism.school.model.Request.CalenderUpdateReq
import com.iprism.school.model.Request.ConsentImgDeleteReq
import com.iprism.school.model.Request.ConsentImgUpdateReq
import com.iprism.school.model.Request.ConsentUpdateReq
import com.iprism.school.model.Request.ConsentsListReq
import com.iprism.school.model.Request.CreateConsentsReq
import com.iprism.school.model.Request.DairyStudentUpdateReq
import com.iprism.school.model.Request.DairyStudentsReq
import com.iprism.school.model.Request.SchoolStaffReq
import com.iprism.school.model.Request.SingleConsentViewReq
import com.iprism.school.model.Request.StudentAttandanceUpdateReq
import com.iprism.school.model.Request.TeacherCalederDetailsReq
import com.iprism.school.model.Request.TeacherCalenderlistReq
import com.iprism.school.model.Request.TeacherCreateCalenderReq
import com.iprism.school.model.Response.Class_studentResponse
import com.iprism.school.model.Response.ConsentsListResponse
import com.iprism.school.model.Response.CreateCalenderResponse
import com.iprism.school.model.Response.DairyUpdateResponse
import com.iprism.school.model.Response.DayCareViewListResponse
import com.iprism.school.model.Response.GroupsResponse
import com.iprism.school.model.Response.SingleConsentViewResponse
import com.iprism.school.model.Response.SuccessResponsePojo
import com.iprism.school.model.Response.TeacherCalenderDetailsResponse
import com.iprism.school.model.Response.TeacherCalenderListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Scl_Repository {

    private val apiService = RetrofitClient.apiService

    fun getStudentRemarks(request: DairyStudentsReq): MutableLiveData<Class_studentResponse?> {
        val liveData = MutableLiveData<Class_studentResponse?>()
        apiService.class_studentsnew(request).enqueue(object : Callback<Class_studentResponse> {
            override fun onResponse(call: Call<Class_studentResponse>, response: Response<Class_studentResponse>) {
                if (response.isSuccessful) {
                    Log.d("isSuccessful",response.message())
                    liveData.postValue(response.body())
                } else {
                    liveData.postValue(null)
                }
            }
            override fun onFailure(call: Call<Class_studentResponse>, t: Throwable) {
                liveData.postValue(null)
                Log.d("failmessage",t.message.toString())
            }
        })
        return liveData
    }


    fun getDairyStudentsupdate(request: DairyStudentUpdateReq): MutableLiveData<DairyUpdateResponse?> {
        val liveData = MutableLiveData<DairyUpdateResponse?>()
        apiService.dairyStudentUpdate(request).enqueue(object : Callback<DairyUpdateResponse> {
            override fun onResponse(call: Call<DairyUpdateResponse>, response: Response<DairyUpdateResponse>) {
                if (response.isSuccessful) {
                    Log.d("isSuccessful",response.message())
                    liveData.postValue(response.body())
                } else {
                    liveData.postValue(null)
                }
            }
            override fun onFailure(call: Call<DairyUpdateResponse>, t: Throwable) {
                liveData.postValue(null)
                Log.d("failmessage",t.message.toString())
            }
        })
        return liveData
    }

    fun getStudentAttandanceupdate(request: StudentAttandanceUpdateReq): MutableLiveData<DairyUpdateResponse?> {
        val liveData = MutableLiveData<DairyUpdateResponse?>()
        apiService.studentAttandanceUpdate(request).enqueue(object : Callback<DairyUpdateResponse> {
            override fun onResponse(call: Call<DairyUpdateResponse>, response: Response<DairyUpdateResponse>) {
                if (response.isSuccessful) {
                    Log.d("isSuccessful",response.message())
                    liveData.postValue(response.body())
                } else {
                    liveData.postValue(null)
                }
            }
            override fun onFailure(call: Call<DairyUpdateResponse>, t: Throwable) {
                liveData.postValue(null)
                Log.d("failmessage",t.message.toString())
            }
        })
        return liveData
    }
    fun getteacherCreateCalender(request: TeacherCreateCalenderReq): MutableLiveData<CreateCalenderResponse?> {
        val liveData = MutableLiveData<CreateCalenderResponse?>()
        apiService.teacherCreate_Calender(request).enqueue(object : Callback<CreateCalenderResponse> {
            override fun onResponse(call: Call<CreateCalenderResponse>, response: Response<CreateCalenderResponse>) {
                if (response.isSuccessful) {
                    Log.d("isSuccessful",response.message())
                    liveData.postValue(response.body())
                } else {
                    liveData.postValue(null)
                }
            }
            override fun onFailure(call: Call<CreateCalenderResponse>, t: Throwable) {
                liveData.postValue(null)
                Log.d("failmessage",t.message.toString())
            }
        })
        return liveData
    }


    fun getteacherCalenderList(request: TeacherCalenderlistReq): MutableLiveData<TeacherCalenderListResponse?> {
        val liveData = MutableLiveData<TeacherCalenderListResponse?>()
        apiService.teacherCalenderList(request).enqueue(object : Callback<TeacherCalenderListResponse> {
            override fun onResponse(call: Call<TeacherCalenderListResponse>, response: Response<TeacherCalenderListResponse>) {
                if (response.isSuccessful) {
                    Log.d("isSuccessful",response.message())
                    liveData.postValue(response.body())
                } else {
                    liveData.postValue(null)
                }
            }
            override fun onFailure(call: Call<TeacherCalenderListResponse>, t: Throwable) {
                liveData.postValue(null)
                Log.d("failmessage",t.message.toString())
            }
        })
        return liveData
    }

    fun getteacherCalenderDetails(request: TeacherCalederDetailsReq): MutableLiveData<TeacherCalenderDetailsResponse?> {
        val liveData = MutableLiveData<TeacherCalenderDetailsResponse?>()
        apiService.teacherCalenderDetails(request).enqueue(object : Callback<TeacherCalenderDetailsResponse> {
            override fun onResponse(call: Call<TeacherCalenderDetailsResponse>, response: Response<TeacherCalenderDetailsResponse>) {
                if (response.isSuccessful) {
                    Log.d("isSuccessful",response.message())
                    liveData.postValue(response.body())
                } else {
                    liveData.postValue(null)
                }
            }
            override fun onFailure(call: Call<TeacherCalenderDetailsResponse>, t: Throwable) {
                liveData.postValue(null)
                Log.d("failmessage",t.message.toString())
            }
        })
        return liveData
    }


    fun calenderDeleteImgRepo(request: CalenderImgDeleteReq): MutableLiveData<SuccessResponsePojo?> {
        val liveData = MutableLiveData<SuccessResponsePojo?>()
        apiService.calenderImgDelete(request).enqueue(object : Callback<SuccessResponsePojo> {
            override fun onResponse(call: Call<SuccessResponsePojo>, response: Response<SuccessResponsePojo>) {
                if (response.isSuccessful) {
                    Log.d("isSuccessful",response.message())
                    liveData.postValue(response.body())
                } else {
                    liveData.postValue(null)
                }
            }
            override fun onFailure(call: Call<SuccessResponsePojo>, t: Throwable) {
                liveData.postValue(null)
                Log.d("failmessage",t.message.toString())
            }
        })
        return liveData
    }
    fun calenderImgUpdate(request: CalenderImgUpdateReq): MutableLiveData<SuccessResponsePojo?> {
        val liveData = MutableLiveData<SuccessResponsePojo?>()
        apiService.calenderImgUpdate(request).enqueue(object : Callback<SuccessResponsePojo> {
            override fun onResponse(call: Call<SuccessResponsePojo>, response: Response<SuccessResponsePojo>) {
                if (response.isSuccessful) {
                    Log.d("isSuccessful",response.message())
                    liveData.postValue(response.body())
                } else {
                    liveData.postValue(null)
                }
            }
            override fun onFailure(call: Call<SuccessResponsePojo>, t: Throwable) {
                liveData.postValue(null)
                Log.d("failmessage",t.message.toString())
            }
        })
        return liveData
    }

    fun calenderDeleteRepo(request: CalenderDeleteReq): MutableLiveData<SuccessResponsePojo?> {
        val liveData = MutableLiveData<SuccessResponsePojo?>()
        apiService.calenderDelete(request).enqueue(object : Callback<SuccessResponsePojo> {
            override fun onResponse(call: Call<SuccessResponsePojo>, response: Response<SuccessResponsePojo>) {
                if (response.isSuccessful) {
                    Log.d("isSuccessful",response.message())
                    liveData.postValue(response.body())
                } else {
                    liveData.postValue(null)
                }
            }
            override fun onFailure(call: Call<SuccessResponsePojo>, t: Throwable) {
                liveData.postValue(null)
                Log.d("failmessage",t.message.toString())
            }
        })
        return liveData
    }

    fun calenderUpdateRepo(request: CalenderUpdateReq): MutableLiveData<SuccessResponsePojo?> {
        val liveData = MutableLiveData<SuccessResponsePojo?>()
        apiService.calenderUpdate(request).enqueue(object : Callback<SuccessResponsePojo> {
            override fun onResponse(call: Call<SuccessResponsePojo>, response: Response<SuccessResponsePojo>) {
                if (response.isSuccessful) {
                    Log.d("isSuccessful",response.message())
                    liveData.postValue(response.body())
                } else {
                    liveData.postValue(null)
                }
            }
            override fun onFailure(call: Call<SuccessResponsePojo>, t: Throwable) {
                liveData.postValue(null)
                Log.d("failmessage",t.message.toString())
            }
        })
        return liveData
    }

    fun createConsent(request: CreateConsentsReq): MutableLiveData<SuccessResponsePojo?> {
        val liveData = MutableLiveData<SuccessResponsePojo?>()
        apiService.createConsent(request).enqueue(object : Callback<SuccessResponsePojo> {
            override fun onResponse(call: Call<SuccessResponsePojo>, response: Response<SuccessResponsePojo>) {
                if (response.isSuccessful) {
                    Log.d("createConsent",response.message())
                    liveData.postValue(response.body())
                } else {
                    liveData.postValue(null)
                }
            }
            override fun onFailure(call: Call<SuccessResponsePojo>, t: Throwable) {
                liveData.postValue(null)
                Log.d("failmessage",t.message.toString())
            }
        })
        return liveData
    }

    fun consentList(request: ConsentsListReq): MutableLiveData<ConsentsListResponse?> {
        val liveData = MutableLiveData<ConsentsListResponse?>()
        apiService.consentList(request).enqueue(object : Callback<ConsentsListResponse> {
            override fun onResponse(call: Call<ConsentsListResponse>, response: Response<ConsentsListResponse>) {
                if (response.isSuccessful) {
                    Log.d("consentList",response.message())
                    liveData.postValue(response.body())
                } else {
                    liveData.postValue(null)
                }
            }
            override fun onFailure(call: Call<ConsentsListResponse>, t: Throwable) {
                liveData.postValue(null)
                Log.d("failmessage",t.message.toString())
            }
        })
        return liveData
    }

    fun singleConsentView(request: SingleConsentViewReq): MutableLiveData<SingleConsentViewResponse?> {
        val liveData = MutableLiveData<SingleConsentViewResponse?>()
        apiService.consentSingleView(request).enqueue(object : Callback<SingleConsentViewResponse> {
            override fun onResponse(call: Call<SingleConsentViewResponse>, response: Response<SingleConsentViewResponse>) {
                if (response.isSuccessful) {
                    Log.d("consentList",response.message())
                    liveData.postValue(response.body())
                } else {
                    liveData.postValue(null)
                }
            }
            override fun onFailure(call: Call<SingleConsentViewResponse>, t: Throwable) {
                liveData.postValue(null)
                Log.d("failmessage",t.message.toString())
            }
        })
        return liveData
    }


    fun consentDelete(request: SingleConsentViewReq): MutableLiveData<SuccessResponsePojo?> {
        val liveData = MutableLiveData<SuccessResponsePojo?>()
        apiService.consentDelete(request).enqueue(object : Callback<SuccessResponsePojo> {
            override fun onResponse(call: Call<SuccessResponsePojo>, response: Response<SuccessResponsePojo>) {
                if (response.isSuccessful) {
                    Log.d("consentList",response.message())
                    liveData.postValue(response.body())
                } else {
                    liveData.postValue(null)
                }
            }
            override fun onFailure(call: Call<SuccessResponsePojo>, t: Throwable) {
                liveData.postValue(null)
                Log.d("failmessage",t.message.toString())
            }
        })
        return liveData
    }

    fun consentImgDelete(request: ConsentImgDeleteReq): MutableLiveData<SuccessResponsePojo?> {
        val liveData = MutableLiveData<SuccessResponsePojo?>()
        apiService.consentImgDelete(request).enqueue(object : Callback<SuccessResponsePojo> {
            override fun onResponse(call: Call<SuccessResponsePojo>, response: Response<SuccessResponsePojo>) {
                if (response.isSuccessful) {
                    Log.d("consentList",response.message())
                    liveData.postValue(response.body())
                } else {
                    liveData.postValue(null)
                }
            }
            override fun onFailure(call: Call<SuccessResponsePojo>, t: Throwable) {
                liveData.postValue(null)
                Log.d("failmessage",t.message.toString())
            }
        })
        return liveData
    }

    fun consentImgUpdate(request: ConsentImgUpdateReq): MutableLiveData<SuccessResponsePojo?> {
        val liveData = MutableLiveData<SuccessResponsePojo?>()
        apiService.consentImgUpdate(request).enqueue(object : Callback<SuccessResponsePojo> {
            override fun onResponse(call: Call<SuccessResponsePojo>, response: Response<SuccessResponsePojo>) {
                if (response.isSuccessful) {
                    Log.d("consentList",response.message())
                    liveData.postValue(response.body())
                } else {
                    liveData.postValue(null)
                }
            }
            override fun onFailure(call: Call<SuccessResponsePojo>, t: Throwable) {
                liveData.postValue(null)
                Log.d("failmessage",t.message.toString())
            }
        })
        return liveData
    }

    fun consentUpdate(request: ConsentUpdateReq): MutableLiveData<SuccessResponsePojo?> {
        val liveData = MutableLiveData<SuccessResponsePojo?>()
        apiService.consentUpdate(request).enqueue(object : Callback<SuccessResponsePojo> {
            override fun onResponse(call: Call<SuccessResponsePojo>, response: Response<SuccessResponsePojo>) {
                if (response.isSuccessful) {
                    Log.d("consentList",response.message())
                    liveData.postValue(response.body())
                } else {
                    liveData.postValue(null)
                }
            }
            override fun onFailure(call: Call<SuccessResponsePojo>, t: Throwable) {
                liveData.postValue(null)
                Log.d("failmessage",t.message.toString())
            }
        })
        return liveData
    }
    fun daycareViewList(request: SchoolStaffReq): MutableLiveData<DayCareViewListResponse?> {
        val liveData = MutableLiveData<DayCareViewListResponse?>()
        apiService.daycareViewList(request).enqueue(object : Callback<DayCareViewListResponse> {
            override fun onResponse(call: Call<DayCareViewListResponse>, response: Response<DayCareViewListResponse>) {
                if (response.isSuccessful) {
                    Log.d("daycareList",response.message())
                    liveData.postValue(response.body())
                } else {
                    liveData.postValue(null)
                }
            }
            override fun onFailure(call: Call<DayCareViewListResponse>, t: Throwable) {
                liveData.postValue(null)
                Log.d("failmessage",t.message.toString())
            }
        })
        return liveData
    }


}