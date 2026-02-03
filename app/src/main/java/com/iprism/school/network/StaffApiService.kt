package com.iprism.school.network

import com.iprism.school.model.Request.AlbumDetailsReq
import com.iprism.school.model.Request.CLass_StudentsReq
import com.iprism.school.model.Request.CalenderDeleteReq
import com.iprism.school.model.Request.CalenderImgDeleteReq
import com.iprism.school.model.Request.CalenderImgUpdateReq
import com.iprism.school.model.Request.CalenderUpdateReq
import com.iprism.school.model.Request.ConsentImgDeleteReq
import com.iprism.school.model.Request.ConsentImgUpdateReq
import com.iprism.school.model.Request.ConsentUpdateReq
import com.iprism.school.model.Request.ConsentsListReq
import com.iprism.school.model.Request.CreateAlbumReq
import com.iprism.school.model.Request.CreateConsentsReq
import com.iprism.school.model.Request.CreateNewMsgReq
import com.iprism.school.model.Request.DairyStudentUpdateReq
import com.iprism.school.model.Request.LoginReq
import com.iprism.school.model.Request.OtpReq
import com.iprism.school.model.Request.DairyStudentsReq
import com.iprism.school.model.Request.DeleteAlbumReq
import com.iprism.school.model.Request.InboxMessageReplyReq
import com.iprism.school.model.Request.InboxMessagesReq
import com.iprism.school.model.Request.InboxSingleMsgReq
import com.iprism.school.model.Request.MarkAllReadReq
import com.iprism.school.model.Request.MessagesTypeResponse
import com.iprism.school.model.Request.MessagesTypesReq
import com.iprism.school.model.Request.SchoolStaffReq
import com.iprism.school.model.Request.SingleAlbumAddReq
import com.iprism.school.model.Request.SingleConsentViewReq
import com.iprism.school.model.Request.SingleDeleteAlbumReq
import com.iprism.school.model.Request.SingleMsgDetailsReq
import com.iprism.school.model.Request.StudentAttandanceUpdateReq
import com.iprism.school.model.Request.TeacherAccessReq
import com.iprism.school.model.Request.TeacherCalederDetailsReq
import com.iprism.school.model.Request.TeacherCalenderlistReq
import com.iprism.school.model.Request.TeacherCreateCalenderReq
import com.iprism.school.model.Request.Update
import com.iprism.school.model.Response.AboutUsResponse
import com.iprism.school.model.Response.AlbumDeleteResponse
import com.iprism.school.model.Response.AlbumDetailsResponse
import com.iprism.school.model.Response.AlbumUploadResponse
import com.iprism.school.model.Response.AlbumsListResponse
import com.iprism.school.model.Response.AllCabsResponse
import com.iprism.school.model.Response.AttendanceUpdatedResponse
import com.iprism.school.model.Response.ClassResponse
import com.iprism.school.model.Response.Class_studentResponse
import com.iprism.school.model.Response.ConsentsListResponse
import com.iprism.school.model.Response.CreateCalenderResponse
import com.iprism.school.model.Response.DairyUpdateResponse
import com.iprism.school.model.Response.DayCareViewListResponse
import com.iprism.school.model.Response.GroupsResponse
import com.iprism.school.model.Response.InboxMessagesResponse
import com.iprism.school.model.Response.InboxSingleMsgResponse
import com.iprism.school.model.Response.LoginResponse
import com.iprism.school.model.Response.OtpResponse
import com.iprism.school.model.Response.PrivacyResponse
import com.iprism.school.model.Response.SchoolStaffResponse
import com.iprism.school.model.Response.SingleConsentViewResponse
import com.iprism.school.model.Response.SingleMsgDetailsResponse
import com.iprism.school.model.Response.SuccessResponsePojo
import com.iprism.school.model.Response.TeacherAccessResponse
import com.iprism.school.model.Response.TeacherCalenderDetailsResponse
import com.iprism.school.model.Response.TeacherCalenderListResponse
import com.iprism.school.model.Response.TermsandConditionResponse
import com.iprism.school.model.albums.AlbumCoverImagesApiRequest
import com.iprism.school.model.albums.AlbumCoverImagesApiResponse
import com.iprism.school.model.albums.AlbumsGalleryApiResponse
import com.iprism.school.model.albums.DayCareAlbumsApiRequest
import com.iprism.school.model.applyforleavemodel.ApplyForLeaveApiRequest
import com.iprism.school.model.applyforleavemodel.ApplyForLeaveApiResponse
import com.iprism.school.model.authmodel.LoginApiRequest
import com.iprism.school.model.authmodel.LoginApiResponse
import com.iprism.school.model.circularmodels.CircularApiRequest
import com.iprism.school.model.circularmodels.CircularApiResponse
import com.iprism.school.model.classteachermodel.AttendanceStudentsApiRequest
import com.iprism.school.model.classteachermodel.AttendanceStudentsApiResponse
import com.iprism.school.model.classteachermodel.ClassTeacherApiRequest
import com.iprism.school.model.classteachermodel.ClassTeacherApiResponse
import com.iprism.school.model.dairy.DiaryApiRequest
import com.iprism.school.model.dairy.DiaryApiResponse
import com.iprism.school.model.daycare.DayCareApiRequest
import com.iprism.school.model.daycare.DayCareApiResponse
import com.iprism.school.model.daycare.DayCareStatusApiRequest
import com.iprism.school.model.daycare.DayCareStatusApiResponse
import com.iprism.school.model.eventsmodel.EventsApiRequest
import com.iprism.school.model.eventsmodel.EventsApiResponse
import com.iprism.school.model.holidaysmodel.HolidaysApiRequest
import com.iprism.school.model.holidaysmodel.HolidaysApiResponse
import com.iprism.school.model.plannersandresources.PlannersAndResourcesApiRequest
import com.iprism.school.model.plannersandresources.PlannersAndResourcesApiResponse
import com.iprism.school.model.refreshtokenmodel.RefreshTokenApiRequest
import com.iprism.school.model.refreshtokenmodel.RefreshTokenApiResponse
import com.iprism.school.model.staffattendacemodel.StaffAttendanceApiRequest
import com.iprism.school.model.staffattendacemodel.StaffAttendanceApiResponse
import com.iprism.school.model.studentsmodel.StudentsApiRequest
import com.iprism.school.model.studentsmodel.StudentsApiResponse
import com.iprism.school.utils.Constants
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface StaffApiService {

    @POST(Constants.LOGIN_ENDPOINT)
    suspend fun userLogin(@Body loginApiRequest: LoginApiRequest): LoginApiResponse

    @POST(Constants.RESENDOTP_ENDPOINT)
    fun reSendOtp(@Body resendOtpApiRequest: OtpReq): Call<OtpResponse>

    @POST(Constants.LOGIN_ENDPOINT)
    fun loginUser(@Body loginApiRequest: LoginReq): Call<LoginResponse>

    @POST(Constants.TEACHER_ACCESS)
    fun teacherAccess(@Body resendOtpApiRequest: TeacherAccessReq): Call<TeacherAccessResponse>

    @POST(Constants.CLASSES)
    fun classes(@Body resendOtpApiRequest: TeacherAccessReq): Call<ClassResponse>

    @POST(Constants.TEACHER_STUDENTS)
    fun teacherStudents(@Body resendOtpApiRequest: CLass_StudentsReq): Call<Class_studentResponse>

    @Headers("Content-Type: application/json")
    @POST(Constants.CLASS_STUDENTSNEW)
    fun class_studentsnew(@Body studnetsnewApiRequest: DairyStudentsReq): Call<Class_studentResponse>

    @POST(Constants.DAIRY_UPDATE)
    fun dairyStudentUpdate(@Body studnetsnewApiRequest: DairyStudentUpdateReq): Call<DairyUpdateResponse>

    @POST(Constants.TEACHER_ATTANDANCE_UPDATE)
    fun studentAttandanceUpdate(@Body studentAttandanceUpdateReq: StudentAttandanceUpdateReq): Call<DairyUpdateResponse>

    @POST(Constants.TEACHERCREATECALENDER)
    fun teacherCreate_Calender(@Body teacherCreateCalenderReq: TeacherCreateCalenderReq): Call<CreateCalenderResponse>

    @POST(Constants.TEACHERCALENDERLIST)
    fun teacherCalenderList(@Body teacherCreateCalenderReq: TeacherCalenderlistReq): Call<TeacherCalenderListResponse>

    @POST(Constants.TEACHERCALENDERDETAILS)
    fun teacherCalenderDetails(@Body teacherCreateCalenderReq: TeacherCalederDetailsReq): Call<TeacherCalenderDetailsResponse>

    @POST(Constants.SCHOOL_STAFF)
    fun schoolStaff(@Body schoolStaffReq: SchoolStaffReq): Call<SchoolStaffResponse>

    @POST(Constants.TEACHERVIEWGROUPS)
    fun teacherViewGroups(@Body schoolStaffReq: SchoolStaffReq): Call<GroupsResponse>

    @POST(Constants.CALENDER_UPDATE)
    fun calenderUpdate(@Body teacherCreateCalenderReq: CalenderUpdateReq): Call<SuccessResponsePojo>

    @POST(Constants.CALENDER_DELETE)
    fun calenderDelete(@Body calenderDeleteReq: CalenderDeleteReq): Call<SuccessResponsePojo>

    @POST(Constants.CALENDER_IMG_DELETE)
    fun calenderImgDelete(@Body calenderImgDeleteReq: CalenderImgDeleteReq): Call<SuccessResponsePojo>

    @POST(Constants.CALENDER_IMG_UPDATE)
    fun calenderImgUpdate(@Body calenderImgUpdateReq: CalenderImgUpdateReq): Call<SuccessResponsePojo>

    @POST(Constants.CREATE_CONSENT)
    fun createConsent(@Body createConsentsReq: CreateConsentsReq): Call<SuccessResponsePojo>

    @POST(Constants.CONSENT_LIST)
    fun consentList(@Body consentsListReq: ConsentsListReq): Call<ConsentsListResponse>

    @POST(Constants.CONSENT_SINGLE_VIEW)
    fun consentSingleView(@Body singleConsentViewReq: SingleConsentViewReq): Call<SingleConsentViewResponse>

    @POST(Constants.CONSENT_UPDATE)
    fun consentUpdate(@Body schoolStaffReq: ConsentUpdateReq): Call<SuccessResponsePojo>

    @POST(Constants.CONSENT_DELETE)
    fun consentDelete(@Body singleConsentViewReq: SingleConsentViewReq): Call<SuccessResponsePojo>

    @POST(Constants.CONSENT_IMG_UPDATE)
    fun consentImgUpdate(@Body consentImgDeleteReq: ConsentImgUpdateReq): Call<SuccessResponsePojo>

    @POST(Constants.CONSENT_IMG_DELETE)
    fun consentImgDelete(@Body consentImgDeleteReq: ConsentImgDeleteReq): Call<SuccessResponsePojo>

    @POST(Constants.DAYCAREVIEWLIST)
    fun daycareViewList(@Body consentImgDeleteReq: SchoolStaffReq): Call<DayCareViewListResponse>

    @POST(Constants.CREATENEW_MSG)
    fun createNewMsg(@Body createNewMsgReq: CreateNewMsgReq): Call<AttendanceUpdatedResponse>

    @POST(Constants.INBOX_MESSAGES)
    fun inbox_messages(@Body inboxMessagesReq: InboxMessagesReq): Call<InboxMessagesResponse>

    @POST(Constants.INBOX_SINGLE_MESSAGES)
    fun inboxSingleMsg(@Body inboxSingleMsgReq: InboxSingleMsgReq): Call<InboxSingleMsgResponse>

    @POST(Constants.REPLAY_INBOX_MESSAGES)
    fun replayInboxMsg(@Body inboxMessageReplyReq: InboxMessageReplyReq): Call<SuccessResponsePojo>

    @POST(Constants.MARK_ALL_MESSAGES)
    fun markALlMessages(@Body markAllReadReq: MarkAllReadReq): Call<SuccessResponsePojo>

    @POST(Constants.MSG_UPDATE)
    fun msgUpdate(@Body update: Update): Call<SuccessResponsePojo>

    @POST(Constants.SEND_ARCHIVED)
    fun msgSend_Archived(@Body messagesTypesReq: MessagesTypesReq): Call<MessagesTypeResponse>

    @POST(Constants.SINGLEVIEW_MSG)
    fun singleMsgView(@Body singleMsgDetailsReq: SingleMsgDetailsReq): Call<SingleMsgDetailsResponse>

    @POST(Constants.ABOUT_US)
    fun aboutUs(@Body schoolStaffReq: SchoolStaffReq): Call<AboutUsResponse>

    @POST(Constants.PRIVACY)
    fun privacy(@Body schoolStaffReq: SchoolStaffReq): Call<PrivacyResponse>

    @POST(Constants.TC)
    fun termsandcondition(@Body schoolStaffReq: SchoolStaffReq): Call<TermsandConditionResponse>

    @POST(Constants.ALLCABS)
    fun allCabsList(@Body schoolStaffReq: SchoolStaffReq): Call<AllCabsResponse>

    @POST(Constants.CLASS_TEACHER_ENDPOINT)
    suspend fun getYearClassAndSection(@Body request: ClassTeacherApiRequest): ClassTeacherApiResponse

    @POST(Constants.ATTENDANCE_ENDPOINT)
    suspend fun getStudents(@Body request: AttendanceStudentsApiRequest): AttendanceStudentsApiResponse

    @POST(Constants.REFRESH_TOKEN_ENDPOINT)
    suspend fun refreshToken(@Body request: RefreshTokenApiRequest): RefreshTokenApiResponse

    @POST(Constants.EVENTS_ENDPOINT)
    suspend fun fetchEvents(@Body request: EventsApiRequest): EventsApiResponse

    @POST(Constants.CIRCULARS_ENDPOINT)
    suspend fun fetchCirculars(@Body request: CircularApiRequest): CircularApiResponse

    @POST(Constants.ACTIVE_STUDENTS_ENDPOINT)
    suspend fun fetchActiveStudents(@Body request: StudentsApiRequest): StudentsApiResponse

    @POST(Constants.INACTIVE_STUDENTS_ENDPOINT)
    suspend fun fetchInActiveStudents(@Body request: StudentsApiRequest): StudentsApiResponse

    @POST(Constants.PLANNERS_AND_RESOURCES_ENDPOINT)
    suspend fun fetchPlannersAndResources(@Body request: PlannersAndResourcesApiRequest): PlannersAndResourcesApiResponse

    @POST(Constants.DIARY_ENDPOINT)
    suspend fun fetchDiaryAndInsert(@Body request: DiaryApiRequest): DiaryApiResponse

    @POST(Constants.DAYCARE_STATUS_ENDPOINT)
    suspend fun fetchDaycareStatus(@Body request: DayCareStatusApiRequest): DayCareStatusApiResponse

    @POST(Constants.DAYCARE_PLANS_ENDPOINT)
    suspend fun fetchDayCarePlansAndStudents(@Body request: DayCareApiRequest): DayCareApiResponse

    @POST(Constants.ALBUM_COVERS_ENDPOINT)
    suspend fun fetchAndInsertAlbumCovers(@Body request: AlbumCoverImagesApiRequest): AlbumCoverImagesApiResponse

    @Multipart
    @POST(Constants.ALBUMS_GALLERY_ENDPOINT)
    suspend fun uploadAlbumMedia(
        @Part("user_id") userId: RequestBody,
        @Part("album_id") albumId: RequestBody,
        @Part("view_type") viewType: RequestBody,
        @Part("page") page: RequestBody,
        @Part("type") type: RequestBody,
        @Part media: List<MultipartBody.Part>
    ): AlbumsGalleryApiResponse

    @POST(Constants.DAYCARE_ALBUM_COVERS_ENDPOINT)
    suspend fun fetchAndInsertDayCareAlbumCovers(@Body request: DayCareAlbumsApiRequest): AlbumCoverImagesApiResponse

    @Multipart
    @POST(Constants.DAYCARE_ALBUMS_GALLERY_ENDPOINT)
    suspend fun uploadDayCareAlbumMedia(
        @Part("user_id") userId: RequestBody,
        @Part("album_id") albumId: RequestBody,
        @Part("view_type") viewType: RequestBody,
        @Part("page") page: RequestBody,
        @Part("type") type: RequestBody,
        @Part media: List<MultipartBody.Part>
    ): AlbumsGalleryApiResponse

    @POST(Constants.HOLIDAY_CALENDER_ENDPOINT)
    suspend fun fetchHolidays(@Body request: HolidaysApiRequest): HolidaysApiResponse

    @POST(Constants.STAFF_ATTENDANCE_ENDPOINT)
    suspend fun staffAttendanceDetails(@Body request: StaffAttendanceApiRequest): StaffAttendanceApiResponse

    @POST(Constants.LEAVE_REQUEST_ENDPOINT)
    suspend fun leaveRequestDetails(@Body request: ApplyForLeaveApiRequest): ApplyForLeaveApiResponse

}