package com.iprism.school.network

import com.iprism.school.model.daycare.DayCareAttendanceApiRequest
import com.iprism.school.model.albums.AlbumCoverImagesApiRequest
import com.iprism.school.model.albums.AlbumCoverImagesApiResponse
import com.iprism.school.model.albums.AlbumsGalleryApiResponse
import com.iprism.school.model.albums.DayCareAlbumsApiRequest
import com.iprism.school.model.applyforleavemodel.ApplyForLeaveApiRequest
import com.iprism.school.model.applyforleavemodel.ApplyForLeaveApiResponse
import com.iprism.school.model.authmodel.LoginApiRequest
import com.iprism.school.model.authmodel.LoginApiResponse
import com.iprism.school.model.authmodel.ResendOtpApiRequest
import com.iprism.school.model.circularmodels.CircularApiRequest
import com.iprism.school.model.circularmodels.CircularApiResponse
import com.iprism.school.model.classteachermodel.AttendanceStudentsApiRequest
import com.iprism.school.model.classteachermodel.AttendanceStudentsApiResponse
import com.iprism.school.model.classteachermodel.ClassTeacherApiRequest
import com.iprism.school.model.classteachermodel.ClassTeacherApiResponse
import com.iprism.school.model.contentpagesmodel.ContentPagesApiRequest
import com.iprism.school.model.contentpagesmodel.ContentPagesApiResponse
import com.iprism.school.model.contentpagesmodel.SchoolSupportApiRequest
import com.iprism.school.model.contentpagesmodel.SchoolSupportApiResponse
import com.iprism.school.model.dairy.DiaryApiRequest
import com.iprism.school.model.dairy.DiaryApiResponse
import com.iprism.school.model.daycare.DayCareApiRequest
import com.iprism.school.model.daycare.DayCareApiResponse
import com.iprism.school.model.daycare.DayCareStatusApiRequest
import com.iprism.school.model.daycare.DayCareStatusApiResponse
import com.iprism.school.model.eventsmodel.EventsApiRequest
import com.iprism.school.model.eventsmodel.EventsApiResponse
import com.iprism.school.model.helptutorials.HelpTutorialsApiRequest
import com.iprism.school.model.helptutorials.HelpTutorialsApiResponse
import com.iprism.school.model.holidaysmodel.HolidaysApiRequest
import com.iprism.school.model.holidaysmodel.HolidaysApiResponse
import com.iprism.school.model.homepagemodel.HomePageApiRequest
import com.iprism.school.model.homepagemodel.HomePageApiResponse
import com.iprism.school.model.leaverequestmodel.LeaveRequestApiRequest
import com.iprism.school.model.leaverequestmodel.LeaveRequestApiResponse
import com.iprism.school.model.messagemodel.MessagesApiRequest
import com.iprism.school.model.messagemodel.MessagesApiResponse
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
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface StaffApiService {

    @POST(Constants.LOGIN_ENDPOINT)
    suspend fun userLogin(@Body loginApiRequest: LoginApiRequest): LoginApiResponse

    @POST(Constants.RESEND_OTP_ENDPOINT)
    suspend fun resendOtp(@Body request: ResendOtpApiRequest): LoginApiResponse

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

    @POST(Constants.HOME_PAGE_ENDPOINT)
    suspend fun fetchHomePageDetails(@Body request: HomePageApiRequest): HomePageApiResponse

    @POST(Constants.TUTORIALS_ENDPOINT)
    suspend fun fetchHelpTutorials(@Body request: HelpTutorialsApiRequest): HelpTutorialsApiResponse

    @POST(Constants.CONTENT_PAGES_ENDPOINT)
    suspend fun fetchAppContent(@Body request: ContentPagesApiRequest): ContentPagesApiResponse

    @POST(Constants.MESSAGES_ENDPOINT)
    suspend fun fetchAndInsertMessages(@Body request: MessagesApiRequest): MessagesApiResponse

    @POST(Constants.DAYCARE_ATTENDANCE_ENDPOINT)
    suspend fun fetchDAyCareStudentsAndInsertAttendance(@Body request: DayCareAttendanceApiRequest): DayCareApiResponse

    @POST(Constants.SCHOOL_SUPPORT_ENDPOINT)
    suspend fun fetchSchoolSupportDetails(@Body request: SchoolSupportApiRequest): SchoolSupportApiResponse

    @POST(Constants.TECH_SUPPORT_ENDPOINT)
    suspend fun fetchTechnicalSupportDetails(@Body request: SchoolSupportApiRequest): SchoolSupportApiResponse

    @POST(Constants.STUDENT_LEAVE_REQUEST_ENDPOINT)
    suspend fun studentLeaveRequests(@Body request: LeaveRequestApiRequest): LeaveRequestApiResponse


}