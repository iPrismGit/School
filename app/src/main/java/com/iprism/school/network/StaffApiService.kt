package com.iprism.school.network


import com.iprism.school.model.Request.AddAuthorizedReq
import com.iprism.school.model.Request.AddSubjectTeacherReq
import com.iprism.school.model.Request.AlbumDetailsReq
import com.iprism.school.model.Request.AllclassesReq
import com.iprism.school.model.Request.AttandanceUpdateReq
import com.iprism.school.model.Request.CLass_StudentsReq
import com.iprism.school.model.Request.CalenderDeleteReq
import com.iprism.school.model.Request.CalenderImgDeleteReq
import com.iprism.school.model.Request.CalenderImgUpdateReq
import com.iprism.school.model.Request.CalenderUpdateReq
import com.iprism.school.model.Request.ClassDetailsReq
import com.iprism.school.model.Request.ClassListReq
import com.iprism.school.model.Request.ClassStatusChangeReq
import com.iprism.school.model.Request.ClassStudentsReq
import com.iprism.school.model.Request.ConsentImgDeleteReq
import com.iprism.school.model.Request.ConsentImgUpdateReq
import com.iprism.school.model.Request.ConsentUpdateReq
import com.iprism.school.model.Request.ConsentsListReq
import com.iprism.school.model.Request.CreateAlbumReq
import com.iprism.school.model.Request.CreateClassReq
import com.iprism.school.model.Request.CreateConsentsReq
import com.iprism.school.model.Request.CreateGroupReq
import com.iprism.school.model.Request.CreateMealReq
import com.iprism.school.model.Request.CreateNewMsgReq
import com.iprism.school.model.Request.CreateNewStaffReq
import com.iprism.school.model.Request.CreatePromotionsReq
import com.iprism.school.model.Request.CreateStudentReq
import com.iprism.school.model.Request.CreateSubjectReq
import com.iprism.school.model.Request.DairyStudentUpdateReq
import com.iprism.school.model.Request.LoginReq
import com.iprism.school.model.Request.OtpReq
import com.iprism.school.model.Request.DairyStudentsReq
import com.iprism.school.model.Request.DaycareActivityIconUpdateReq
import com.iprism.school.model.Request.DaycarereportReq
import com.iprism.school.model.Request.DeleteAlbumReq
import com.iprism.school.model.Request.DeleteDaycareReportReq
import com.iprism.school.model.Request.DeleteStduentReq
import com.iprism.school.model.Request.EditStudentReq
import com.iprism.school.model.Request.EmailReportReq
import com.iprism.school.model.Request.GroupDetailsReq
import com.iprism.school.model.Request.InboxMessageReplyReq
import com.iprism.school.model.Request.InboxMessagesReq
import com.iprism.school.model.Request.InboxSingleMsgReq
import com.iprism.school.model.Request.MarkAllReadReq
import com.iprism.school.model.Request.MealPlanListReq
import com.iprism.school.model.Request.MealUpdateReq
import com.iprism.school.model.Request.MessagesTypeResponse
import com.iprism.school.model.Request.MessagesTypesReq
import com.iprism.school.model.Request.ParentStudentReq
import com.iprism.school.model.Request.ReportCreateReq
import com.iprism.school.model.Request.SUbjectsTeacherListReq
import com.iprism.school.model.Request.SchoolStaffReq
import com.iprism.school.model.Request.SendFeedBackReq
import com.iprism.school.model.Request.SingleAlbumAddReq
import com.iprism.school.model.Request.SingleConsentViewReq
import com.iprism.school.model.Request.SingleDeleteAlbumReq
import com.iprism.school.model.Request.SingleMsgDetailsReq
import com.iprism.school.model.Request.StaffAttandanceReq
import com.iprism.school.model.Request.StaffDetailsReq
import com.iprism.school.model.Request.StaffDetailsUpdateReq
import com.iprism.school.model.Request.StaffListReq
import com.iprism.school.model.Request.StaffListResponse
import com.iprism.school.model.Request.StaffStatusReq
import com.iprism.school.model.Request.StudentAttandanceUpdateReq
import com.iprism.school.model.Request.StudentDetailsReq
import com.iprism.school.model.Request.StudentOtherDetailsReq
import com.iprism.school.model.Request.StudentsListReq
import com.iprism.school.model.Request.TeacherAccessReq
import com.iprism.school.model.Request.TeacherCalederDetailsReq
import com.iprism.school.model.Request.TeacherCalenderlistReq
import com.iprism.school.model.Request.TeacherCreateCalenderReq
import com.iprism.school.model.Request.TeacherGroupStudentsReq
import com.iprism.school.model.Request.TeacherSubjectReq
import com.iprism.school.model.Request.Update
import com.iprism.school.model.Request.UpdateClassReq
import com.iprism.school.model.Request.UpdateGroupReq
import com.iprism.school.model.Request.UpdateSubjectReq
import com.iprism.school.model.Request.ViewDayCareReq
import com.iprism.school.model.Request.ViewPromotionsReq
import com.iprism.school.model.Response.AboutUsResponse
import com.iprism.school.model.Response.ActivityIconsResponse
import com.iprism.school.model.Response.AdmissionResponse
import com.iprism.school.model.Response.AlbumDeleteResponse
import com.iprism.school.model.Response.AlbumDetailsResponse
import com.iprism.school.model.Response.AlbumUploadResponse
import com.iprism.school.model.Response.AlbumsListResponse
import com.iprism.school.model.Response.AllCabsResponse
import com.iprism.school.model.Response.AllClassesResponse
import com.iprism.school.model.Response.AttandanceStudentResponse
import com.iprism.school.model.Response.AttendanceUpdatedResponse
import com.iprism.school.model.Response.CabsListResponse
import com.iprism.school.model.Response.ClassDetailsResponse
import com.iprism.school.model.Response.ClassListResponse
import com.iprism.school.model.Response.ClassResponse
import com.iprism.school.model.Response.Class_studentResponse
import com.iprism.school.model.Response.ConsentsListResponse
import com.iprism.school.model.Response.CreateCalenderResponse
import com.iprism.school.model.Response.CreateStudentResponse
import com.iprism.school.model.Response.DairyUpdateResponse
import com.iprism.school.model.Response.DayCareReportsStudentsResponse
import com.iprism.school.model.Response.DayCareViewListResponse
import com.iprism.school.model.Response.DaycareActivityesResponse
import com.iprism.school.model.Response.DaycareReportResponse
import com.iprism.school.model.Response.GenerateIdResponse
import com.iprism.school.model.Response.GroupDetailsResponse
import com.iprism.school.model.Response.GroupsListResponse
import com.iprism.school.model.Response.GroupsResponse
import com.iprism.school.model.Response.InboxMessagesResponse
import com.iprism.school.model.Response.InboxSingleMsgResponse
import com.iprism.school.model.Response.LoginResponse
import com.iprism.school.model.Response.MealPlanListResponse
import com.iprism.school.model.Response.OtpResponse
import com.iprism.school.model.Response.ParentDetailsResponse
import com.iprism.school.model.Response.PrivacyResponse
import com.iprism.school.model.Response.SchoolStaffResponse
import com.iprism.school.model.Response.SessionListResponse
import com.iprism.school.model.Response.SingleConsentViewResponse
import com.iprism.school.model.Response.SingleMsgDetailsResponse
import com.iprism.school.model.Response.StaffAttandanceResponse
import com.iprism.school.model.Response.StaffDetailsResponse
import com.iprism.school.model.Response.StudentDeleteResponse
import com.iprism.school.model.Response.StudentDetailsResponse
import com.iprism.school.model.Response.StudentListResponse
import com.iprism.school.model.Response.SubjectTeacherListResponse
import com.iprism.school.model.Response.SubjectsListResponse
import com.iprism.school.model.Response.SuccessResponsePojo
import com.iprism.school.model.Response.TeacherAccessResponse
import com.iprism.school.model.Response.TeacherCalenderDetailsResponse
import com.iprism.school.model.Response.TeacherCalenderListResponse
import com.iprism.school.model.Response.TeacherGroupStudentsResponse
import com.iprism.school.model.Response.TermsandConditionResponse
import com.iprism.school.model.Response.ViewDayCareResponse
import com.iprism.school.model.classteachermodel.ClassesApiResponse
import com.iprism.school.model.authmodel.LoginApiRequest
import com.iprism.school.model.authmodel.LoginApiResponse
import com.iprism.school.model.classteachermodel.ClassTeacherApiRequest
import com.iprism.school.model.classteachermodel.ClassTeacherApiResponse
import com.iprism.school.model.classteachermodel.SectionsApiResponse
import com.iprism.school.utils.Constants
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

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

    @POST(Constants.TEACHERGROUPSTUDENTS)
    fun teacherGroupStudents(@Body teacherGroupStudentsReq: TeacherGroupStudentsReq): Call<TeacherGroupStudentsResponse>

    @POST(Constants.TEACHERGROUPSTUDENTS)
    fun reportsStudents(@Body teacherGroupStudentsReq: TeacherGroupStudentsReq): Call<DayCareReportsStudentsResponse>

    @POST(Constants.DAYCAREREPORT)
    fun daycareReport(@Body dayCareReportReq: DaycarereportReq): Call<ReportCreateReq>

    @POST(Constants.VIEWDAYCARE)
    fun viewDayCare(@Body viewDayCareReq: ViewDayCareReq): Call<ViewDayCareResponse>

    @POST(Constants.DELETEDAYCAREREPORT)
    fun deleteDayCareReport(@Body viewDayCareReq: DeleteDaycareReportReq): Call<DaycareReportResponse>

    @POST(Constants.ACTIVITIEICONSDAYCARE)
    fun activity_viewIconsDaycare(@Body viewDayCareReq: CalenderDeleteReq): Call<DaycareActivityesResponse>

    @POST(Constants.VIEWICONSDAYCARE)
    fun viewIconsDaycare(@Body viewDayCareReq: CalenderDeleteReq): Call<ActivityIconsResponse>

    @POST(Constants.UPDATEICONSDAYCARE)
    fun updateIconsDaycare(@Body viewDayCareReq: DaycareActivityIconUpdateReq): Call<DaycareReportResponse>

    @POST(Constants.ATTANDANCE_STUDENTS)
    fun attandanceStudents(@Body classStudentsreq: ClassStudentsReq): Call<AttandanceStudentResponse>

    @POST(Constants.UPDATEATTANDANCE_STUDENTS)
    fun updateAttandanceStudents(@Body attandanceUpdateReq: AttandanceUpdateReq): Call<AttendanceUpdatedResponse>

    @POST(Constants.CREATENEW_MSG)
    fun createNewMsg(@Body createNewMsgReq: CreateNewMsgReq): Call<AttendanceUpdatedResponse>

    @POST(Constants.TEACHER_UPLOAD_ALBUMS)
    fun teacherUploadAlbum(@Body createAlbumReq: CreateAlbumReq): Call<AlbumUploadResponse>

    @POST(Constants.ALBUMS_LIST)
    fun albumList(@Body schoolStaffReq: SchoolStaffReq): Call<AlbumsListResponse>

    @POST(Constants.ALBUMS_DETAILS)
    fun albumDetails(@Body albumDetailsReq: AlbumDetailsReq): Call<AlbumDetailsResponse>

    @POST(Constants.ALBUMS_DETAILS_UPDATE)
    fun albumDetailsUpdate(@Body createAlbumReq: CreateAlbumReq): Call<AlbumUploadResponse>

    @POST(Constants.ALBUMS_DELETE)
    fun albumDelete(@Body albumReq: DeleteAlbumReq): Call<AlbumDeleteResponse>

    @POST(Constants.SINGLE_ALBUMS_DELETE)
    fun single_album_Delete(@Body singleDeleteAlbumReq: SingleDeleteAlbumReq): Call<AlbumDeleteResponse>

    @POST(Constants.SINGLE_ALBUMS_ADD)
    fun single_album_Add(@Body singleAlbumAddReq: SingleAlbumAddReq): Call<AlbumDeleteResponse>

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

    @POST(Constants.INBOX_RSA)
    fun inbox_RSA(@Body inboxMessagesReq: InboxMessagesReq): Call<InboxMessagesResponse>

    @POST(Constants.STAFFLIST)
    fun staffList(@Body staffListReq: StaffListReq): Call<StaffListResponse>

    @POST(Constants.ALLCLASSES)
    fun allClasses(@Body allclassesReq: AllclassesReq): Call<AllClassesResponse>

    @POST(Constants.GENERATE_ID)
    fun generateId(@Body schoolStaffReq: SchoolStaffReq): Call<GenerateIdResponse>

    @POST(Constants.CREATESTAFF)
    fun createStaff(@Body createNewStaffReq: CreateNewStaffReq): Call<SuccessResponsePojo>

    @POST(Constants.STATUS_STAFF)
    fun staffStatus(@Body staffStatusReq: StaffStatusReq): Call<SuccessResponsePojo>

    @POST(Constants.SINGLE_STAFF_DETAILS)
    fun singleStaffDetails(@Body staffDetailsReq: StaffDetailsReq): Call<StaffDetailsResponse>

    @POST(Constants.UPDATE_STAFF_DETAILS)
    fun updateStaffDetails(@Body staffDetailsUpdateReq: StaffDetailsUpdateReq): Call<SuccessResponsePojo>

    @POST(Constants.GENERATESTUDENT_ID)
    fun generateStudentId(@Body schoolStaffReq: SchoolStaffReq): Call<AdmissionResponse>

    @POST(Constants.SESSIONLIST)
    fun sessionList(@Body schoolStaffReq: SchoolStaffReq): Call<SessionListResponse>

    @POST(Constants.CREATE_STUDENT)
    fun createStudent(@Body createStudentReq: CreateStudentReq): Call<CreateStudentResponse>

    @POST(Constants.EDIT_STUDENT)
    fun editStudent(@Body editStudentReq: EditStudentReq): Call<CreateStudentResponse>

    @POST(Constants.PARENT_STUDENT)
    fun parentStudent(@Body parentStudentReq: ParentStudentReq): Call<ParentDetailsResponse>

    @POST(Constants.DELETE_STUDENT)
    fun deleteStudent(@Body deleteStduentReq: DeleteStduentReq): Call<StudentDeleteResponse>

    @POST(Constants.ALLCABS)
    fun allCabs(@Body schoolStaffReq: SchoolStaffReq): Call<CabsListResponse>

    @POST(Constants.STUDENTS_LIST)
    fun studentsList(@Body resendOtpApiRequest: StudentsListReq): Call<StudentListResponse>

    @POST(Constants.STUDENTS_DETAILS)
    fun studentsDetails(@Body studentDetailsReq: StudentDetailsReq): Call<StudentDetailsResponse>

    @POST(Constants.GROUP_LIST)
    fun groupList(@Body schoolStaffReq: SchoolStaffReq): Call<GroupsListResponse>

    @POST(Constants.SINGLE_GROUP_DETAILS)
    fun groupDetails(@Body groupDetailsReq: GroupDetailsReq): Call<GroupDetailsResponse>

    @POST(Constants.DELETE_GROUP_DETAILS)
    fun groupDelete(@Body groupDetailsReq: GroupDetailsReq): Call<SuccessResponsePojo>

    @POST(Constants.UPDATE_GROUP_DETAILS)
    fun groupUpdate(@Body updateGroupReq: UpdateGroupReq): Call<SuccessResponsePojo>

    @POST(Constants.ADDAUTH_PERSON)
    fun addAuth(@Body addAuthorizedReq: AddAuthorizedReq): Call<SuccessResponsePojo>

    @POST(Constants.STUDENTOTHERDETAILS)
    fun studentOtherDetails(@Body studentOtherDetailsReq: StudentOtherDetailsReq): Call<SuccessResponsePojo>

    @POST(Constants.CREATE_GROUP)
    fun createGroup(@Body createGroupReq: CreateGroupReq): Call<SuccessResponsePojo>

    @POST(Constants.CREATE_NEW_SUBJECT)
    fun createSubject(@Body createSubjectReq: CreateSubjectReq): Call<SuccessResponsePojo>

    @POST(Constants.UPDATE_SUBJECT_DETAILS)
    fun updateSubject(@Body updateSubjectReq: UpdateSubjectReq): Call<SuccessResponsePojo>

    @POST(Constants.SUBJECT_LIST)
    fun subjectsList(@Body schoolStaffReq: SchoolStaffReq): Call<SubjectsListResponse>

    @POST(Constants.CREATE_CLASS)
    fun createClass(@Body createClassReq: CreateClassReq): Call<SuccessResponsePojo>

    @POST(Constants.CLASS_UPDATE)
    fun updateClass(@Body updateClassReq: UpdateClassReq): Call<SuccessResponsePojo>

    @POST(Constants.CLASS_ACTIVE_DEACTIVE)
    fun statusClass(@Body classStatusChangeReq: ClassStatusChangeReq): Call<SuccessResponsePojo>

    @POST(Constants.ADD_SUBJECT_TEACHER)
    fun addTeacherSubject(@Body addSubjectTeacherReq: AddSubjectTeacherReq): Call<SuccessResponsePojo>

    @POST(Constants.SUBJECT_TEACHER_LIST)
    fun teacherSubjectList(@Body sUbjectsTeacherListReq: SUbjectsTeacherListReq): Call<SubjectTeacherListResponse>

    @POST(Constants.UPDATE_SUBJECT_TEACHER)
    fun updateTeacherSubject(@Body teachersubjectReq: TeacherSubjectReq): Call<SuccessResponsePojo>

    @POST(Constants.SINGLE_CLASS_DETAILS)
    fun classDetails(@Body classDetailsReq: ClassDetailsReq): Call<ClassDetailsResponse>

    @POST(Constants.CLASS_LIST)
    fun classList(@Body classListReq: ClassListReq): Call<ClassListResponse>

    @POST(Constants.VIEW_STAFF_ATTANDANCE)
    fun viewStaffAttandance(@Body staffAttandanceReq: StaffAttandanceReq): Call<StaffAttandanceResponse>

    @POST(Constants.MAIL_REPORT)
    fun mailReportStaff(@Body emailReportReq: EmailReportReq): Call<SuccessResponsePojo>

    @POST(Constants.CREATE_MEAL)
    fun createMeal(@Body createMealReq: CreateMealReq): Call<SuccessResponsePojo>

    @POST(Constants.VIEW_MEAL)
    fun viewMeal(@Body mealPlanListReq: MealPlanListReq): Call<MealPlanListResponse>

    @POST(Constants.UPDATE_MEAL)
    fun updateMeal(@Body mealUpdateReq: MealUpdateReq): Call<SuccessResponsePojo>

    @POST(Constants.CAREATE_PROMOTION)
    fun createPromotions(@Body createPromotionsReq: CreatePromotionsReq): Call<SuccessResponsePojo>

    @POST(Constants.VIEW_PROMOTION)
    fun viewPromotions(@Body viewPromotionsReq: ViewPromotionsReq): Call<SuccessResponsePojo>

    @POST(Constants.SENT_SUGGESIONS)
    fun sentSuggetions(@Body sentFeedBackReq: SendFeedBackReq): Call<SuccessResponsePojo>

    @POST(Constants.ABOUT_US)
    fun aboutUs(@Body schoolStaffReq: SchoolStaffReq): Call<AboutUsResponse>

    @POST(Constants.PRIVACY)
    fun privacy(@Body schoolStaffReq: SchoolStaffReq): Call<PrivacyResponse>

    @POST(Constants.TC)
    fun termsandcondition(@Body schoolStaffReq: SchoolStaffReq): Call<TermsandConditionResponse>

    @POST(Constants.ALLCABS)
    fun allCabsList(@Body schoolStaffReq: SchoolStaffReq): Call<AllCabsResponse>

    //New Apis

    @POST(Constants.CLASS_TEACHER)
    suspend fun getYearClassAndSection(@Body request: ClassTeacherApiRequest): ClassTeacherApiResponse

    @POST(Constants.CLASS_TEACHER)
    suspend fun getClasses(@Body request: ClassTeacherApiRequest): ClassesApiResponse

    @POST(Constants.CLASS_TEACHER)
    suspend fun getSections(@Body request: ClassTeacherApiRequest): SectionsApiResponse

}