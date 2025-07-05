package com.iprism.school.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
import com.iprism.school.repositories.Scl_Repository

class Scl_ViewModel : ViewModel() {
    private val repository = Scl_Repository()

    fun fetchStudentRemarks(request: DairyStudentsReq): MutableLiveData<Class_studentResponse?> {
        return repository.getStudentRemarks(request)
    }

    fun dairyStudentUpdate(request: DairyStudentUpdateReq): MutableLiveData<DairyUpdateResponse?> {
        return repository.getDairyStudentsupdate(request)
    }

    fun studentAttandanceUpdate(request: StudentAttandanceUpdateReq): MutableLiveData<DairyUpdateResponse?> {
        return repository.getStudentAttandanceupdate(request)
    }

    fun teacherCreateCalender(request: TeacherCreateCalenderReq): MutableLiveData<CreateCalenderResponse?> {
        return repository.getteacherCreateCalender(request)
    }

    fun teacherCalenderList(request: TeacherCalenderlistReq): MutableLiveData<TeacherCalenderListResponse?> {
        return repository.getteacherCalenderList(request)
    }

    fun teacherCalenderDetials(request: TeacherCalederDetailsReq): MutableLiveData<TeacherCalenderDetailsResponse?> {
        return repository.getteacherCalenderDetails(request)
    }

    fun calenderImgDelete(calenderImgDeleteReq: CalenderImgDeleteReq): MutableLiveData<SuccessResponsePojo?> {
        return repository.calenderDeleteImgRepo(calenderImgDeleteReq)
    }

    fun calenderDelete(calenderDeleteReq: CalenderDeleteReq): MutableLiveData<SuccessResponsePojo?> {
        return repository.calenderDeleteRepo(calenderDeleteReq)
    }

    fun calenderUpdate(calenderDeleteReq: CalenderUpdateReq): MutableLiveData<SuccessResponsePojo?> {
        return repository.calenderUpdateRepo(calenderDeleteReq)
    }

    fun createConsents(createConsentsReq: CreateConsentsReq): MutableLiveData<SuccessResponsePojo?> {
        return repository.createConsent(createConsentsReq)
    }

    fun consentList(consentsListReq: ConsentsListReq): MutableLiveData<ConsentsListResponse?> {
        return repository.consentList(consentsListReq)
    }

    fun singleConsentView(singleConsentViewReq: SingleConsentViewReq): MutableLiveData<SingleConsentViewResponse?> {
        return repository.singleConsentView(singleConsentViewReq)
    }

    fun consentDelete(singleConsentViewReq: SingleConsentViewReq): MutableLiveData<SuccessResponsePojo?> {
        return repository.consentDelete(singleConsentViewReq)
    }
    fun calenderImgUpdate(calenderImgUpdateReq: CalenderImgUpdateReq): MutableLiveData<SuccessResponsePojo?> {
        return repository.calenderImgUpdate(calenderImgUpdateReq)
    }

    fun consentImgDelete(consentImgDeleteReq: ConsentImgDeleteReq): MutableLiveData<SuccessResponsePojo?> {
        return repository.consentImgDelete(consentImgDeleteReq)
    }

    fun consentImgUpdate(consentImgDeleteReq: ConsentImgUpdateReq): MutableLiveData<SuccessResponsePojo?> {
        return repository.consentImgUpdate(consentImgDeleteReq)
    }

    fun consentUpdate(consentUpdateReq: ConsentUpdateReq): MutableLiveData<SuccessResponsePojo?> {
        return repository.consentUpdate(consentUpdateReq)
    }

    fun daycareViewList(schoolStaffReq:SchoolStaffReq): MutableLiveData<DayCareViewListResponse?> {
        return repository.daycareViewList(schoolStaffReq)
    }

}