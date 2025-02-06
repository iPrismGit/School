package com.iprism.parentapp.network


import com.iprism.school.model.Request.LoginReq
import com.iprism.school.model.Request.OtpReq
import com.iprism.school.model.Response.LoginResponse
import com.iprism.school.model.Response.OtpResponse
import com.iprism.school.utils.Constants
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface StaffApiService {

    @POST(Constants.LOGIN_ENDPOINT)
    fun loginUser(@Body loginApiRequest: LoginReq) : Call<LoginResponse>

    @POST(Constants.RESENDOTP_ENDPOINT)
    fun reSendOtp(@Body resendOtpApiRequest: OtpReq) : Call<OtpResponse>

//    @POST(Constants.CHILD_PROFILE_ENDPOINT)
//    fun fetchChildProfile(@Body childProfileApiRequest: ChildProfileApiRequest) : Call<ChildProfileApiResponse>
//
//    @POST(Constants.PARENT_PROFILE_ENDPOINT)
//    fun fetchParentProfile(@Body parentProfileApiRequest: ParentProfileApiRequest) : Call<ParentProfileApiResponse>
//
//    @POST(Constants.UPDATE_PARENT_PROFILE_ENDPOINT)
//    fun updateParentProfile(@Body updateProfileApiRequest: UpdateProfileApiRequest) : Call<UpdateProfileApiResponse>
//
//    @POST(Constants.UPDATE_PARENT_PROFILE_PIC_ENDPOINT)
//    fun updateParentProfilePic(@Body updateParentProfilePicApiRequest: UpdateParentProfilePicApiRequest) : Call<UpdateParentProfilePicApiResponse>
//
//    @POST(Constants.VIEW_SENT_MESSAGES_ENDPOINT)
//    fun viewParentSentMessages(@Body viewSentMessagesApiRequest: ViewSentMessagesApiRequest) : Call<ViewSentMessagesApiResponse>
//
//    @POST(Constants.VIEW_SINGLE_SENT_MESSAGE_ENDPOINT)
//    fun viewSingleSentMessages(@Body viewSingleSentMessageApiRequest: SingleSentMessageApiRequest) : Call<SingleSentMessageApiResponse>
//
//    @POST(Constants.SEND_MESSAGE_ENDPOINT)
//    fun sendMessage(@Body sentMessageApiRequest: SentMessageApiRequest) : Call<SentMessageApiResponse>
//
//    @POST(Constants.VIEW_PARENT_INBOX_MESSAGES_ENDPOINT)
//    fun viewInboxMessages(@Body viewParentInboxMessagesAiRequest: ParentInboxMessagesAiRequest) : Call<ParentInboxMessagesApiResponse>
//
//    @POST(Constants.PARENT_SINGLE_INBOX_MESSAGES_ENDPOINT)
//    fun viewSingleInboxMessages(@Body inboxSingleMessageApiRequest: InboxSingleMessageApiRequest) : Call<InboxSingleMessageApiResponse>
//
//
//    @POST(Constants.PARENT_MESSAGE_UPDATE_STATUS_ENDPOINT)
//    fun updateInboxMessageStatus(@Body updateMessageStatusApiRequest: UpdateMessageStatusApiRequest) : Call<UpdateMessageStatusApiResponse>
//
//    @POST(Constants.MARK_ALL_READ_ENDPOINT)
//    fun markAsAllMessagesRead(@Body parentMessagesMarkAsAllReadApiRequest: ParentMessagesMarkAsAllReadApiRequest) : Call<MarkAllReadApiResponse>
//
//    @POST(Constants.SEARCH_INBOX_MESSAGES_ENDPOINT)
//    fun searchInBoxMessages(@Body searchApiRequest: SearchApiRequest) : Call<ParentInboxMessagesApiResponse>
//
//    @POST(Constants.ALBUMS_ENDPOINT)
//    fun fetchAlbums(@Body albumsApiRequest: AlbumsApiRequest) : Call<AllAlbumsApiResponse>
//
//    @POST(Constants.SINGLE_PERSON_ALBUMS_ENDPOINT)
//    fun fetchSingleAlbums(@Body singlePersonAlbumsApiRequest : SinglePersonAlbumsApiRequest) : Call<SinglePersonAlbumsApiResponse>

}