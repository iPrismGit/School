package com.iprism.school.utils

object Constants {

    const val BASE_URL = "https://littlepebbles.co.in/schoolappnew/teacher_app/ws/"
    const val IMAGES_URL = "https://littlepebbles.co.in/schoolappnew/"

//    const val BASE_URL = "https://lpipreschool.com/schoolapp/api/api/"
//    const val IMAGES_URL = "https://lpipreschool.com/schoolapp/"

    const val LOGIN_ENDPOINT = "login"
    const val RESENDOTP_ENDPOINT = "teacher_resend_otp"
    const val TEACHER_ACCESS = "teacher_view_access_details"
    const val CLASSES = "teacher_view_classes_details"
    const val TEACHER_STUDENTS = "class_taecher_view_students_details"
    const val CLASS_STUDENTSNEW = "class_taecher_view_students_dairy_remarks_details"
    const val DAIRY_UPDATE = "class_taecher_dairy_update_details"
    const val TEACHER_ATTANDANCE_UPDATE = "teacher_updating_attendance"
    const val SCHOOL_STAFF = "school_staff_details"
    const val TEACHERVIEWGROUPS = "teacher_view_groups_details"
    const val TEACHERCREATECALENDER = "teacher_create_calender"
    const val TEACHERCALENDERLIST = "teacher_claender_list"
    const val TEACHERCALENDERDETAILS = "teacher_view_single_claender_details"

    const val CALENDER_DELETE = "teacher_delete_calender"
    const val CALENDER_UPDATE = "teacher_update_calender"
    const val CALENDER_IMG_DELETE = "teacher_delete_calender_attachment"
    const val CALENDER_IMG_UPDATE = "teacher_update_calender_attachment"
    const val CREATE_CONSENT= "teacher_create_consent"
    const val CONSENT_LIST= "teacher_consent_list"
    const val CONSENT_SINGLE_VIEW= "teacher_view_single_consent_details"

    const val CONSENT_UPDATE= "teacher_updating_consent"
    const val CONSENT_DELETE= "teacher_delete_consent"
    const val CONSENT_IMG_UPDATE= "teacher_updating_consent_attachment"
    const val CONSENT_IMG_DELETE= "teacher_delete_consent_attachment"

    const val DAYCAREVIEWLIST= "teacher_view_daycare_details"
    const val TEACHERGROUPSTUDENTS= "teacher_view_group_students"
    const val DAYCAREREPORT= "teacher_create_daycare_report"
    const val VIEWDAYCARE= "teacher_view_student_daycare_report"
    const val DELETEDAYCAREREPORT= "teacher_delete_student_daycare_report"

    const val ACTIVITIEICONSDAYCARE= "teacher_view_daycare_activities"
    const val VIEWICONSDAYCARE= "teacher_view_daycare_activities_icons"
    const val UPDATEICONSDAYCARE= "teacher_update_daycare_activity_icon"
    const val ATTANDANCE_STUDENTS= "teacher_view_studnet_attendance"

    const val UPDATEATTANDANCE_STUDENTS= "teacher_updating_studnet_attendance"

    const val CREATENEW_MSG= "teacher_create_new_message"
    const val TEACHER_UPLOAD_ALBUMS= "teacher_upload_albums"
    const val ALBUMS_LIST= "teacher_albums_list"

    const val ALBUMS_DETAILS= "teacher_view_single_album_details"
    const val ALBUMS_DETAILS_UPDATE= "teacher_update_upload_albums"
    const val ALBUMS_DELETE= "teacher_delete_album_details"
    const val SINGLE_ALBUMS_DELETE= "teacher_delete_single_file_album_details"
    const val SINGLE_ALBUMS_ADD= "teacher_update_single_file_album_details"

    const val INBOX_MESSAGES= "teacher_inbox_messages"
    const val INBOX_SINGLE_MESSAGES= "teacher_view_single_inbox_message"
    const val REPLAY_INBOX_MESSAGES= "teacher_reply_to_inbox_message"
    const val MARK_ALL_MESSAGES= "teacher_mark_as_all_read_messages"

    const val MSG_UPDATE= "teacher_inbox_message_read_starred_archived"
    const val SEND_ARCHIVED= "teacher_view_sent_messages"
    const val SINGLEVIEW_MSG= "teacher_view_single_sent_message"

    const val INBOX_RSA= "teacher_inbox_message_read_starred_archived"
    const val STAFFLIST= "teacher_staff_list"
    const val ALLCLASSES= "teacher_view_all_classess_list"
    const val GENERATE_ID= "teacher_generate_student_id"
    const val CREATESTAFF= "teacher_create_staff"
    const val STATUS_STAFF= "teacher_activte_deactivate_staff"
    const val SINGLE_STAFF_DETAILS= "teacher_view_single_staff_details"
    const val UPDATE_STAFF_DETAILS= "teacher_update_staff_details"
    const val GENERATESTUDENT_ID= "teacher_generate_student_id"
    const val SESSIONLIST= "sessions_list"
    const val CREATE_STUDENT= "teacher_create_student"
    const val EDIT_STUDENT= "teacher_edit_student"

    const val PARENT_STUDENT= "teacher_update_student_parent_details"
    const val DELETE_STUDENT= "teacher_delete_student_details"
    const val STUDENTS_LIST= "class_taecher_view_all_students_details"
    const val STUDENTS_DETAILS= "teacher_view_single_student_details"
    const val ALLCABS= "view_all_cabs_list"

    const val ADDAUTH_PERSON= "teacher_view_update_student_otherised_persons_details"
    const val STUDENTOTHERDETAILS= "teacher_update_student_other_details"
    const val CREATE_GROUP= "teacher_create_new_group"

    const val GROUP_LIST= "teacher_view_all_groups_list"
    const val SINGLE_GROUP_DETAILS= "teacher_view_single_groups_details"
    const val DELETE_GROUP_DETAILS= "teacher_delete_group_details"
    const val UPDATE_GROUP_DETAILS= "teacher_update_group_details"


    const val CREATE_NEW_SUBJECT= "teacher_create_new_subject"
    const val SUBJECT_LIST= "teacher_view_all_subjects_list"

    const val UPDATE_SUBJECT_DETAILS= "teacher_update_subject_details"
    const val SINGLE_SUBJECT_DETAILS= "teacher_view_single_subject_details"

    const val CREATE_CLASS= "teacher_create_new_class"
    const val CLASS_LIST= "teacher_view_all_classess_list"
    const val SINGLE_CLASS_DETAILS= "teacher_view_single_class_details"
    const val CLASS_UPDATE= "teacher_update_class"
    const val CLASS_ACTIVE_DEACTIVE= "teacher_activte_deactivate_class"
    const val ADD_SUBJECT_TEACHER= "add_teachers_to_subjects"
    const val SUBJECT_TEACHER_LIST= "teacher_view_subjects_teachers_list"
    const val UPDATE_SUBJECT_TEACHER= "update_teachers_to_subjects"
    const val VIEW_STAFF_ATTANDANCE= "teacher_view_staff_attendance"
    const val MAIL_REPORT= "teacher_share_attendance_report"

    const val CREATE_MEAL= "teacher_create_new_meal_planner"
    const val VIEW_MEAL= "teacher_view_meal_planner"
    const val UPDATE_MEAL= "teacher_update_meal_planner"
    const val CAB_LIST= "view_all_cabs_list"
    const val CAREATE_PROMOTION= "create_new_promotion"
    const val VIEW_PROMOTION= "view_promotion_details"

    const val CREATE_SESSION= "create_new_session"
    const val UPDATE_SESSION= "update_session_name"
    const val VIEW_SESSION= "view_session_classes"
    const val SENT_SUGGESIONS= "teacher_sent_suggesions"

    const val ABOUT_US= "teacher_view_about_us"
    const val TC= "teacher_view_terms_and_conditions"
    const val PRIVACY= "teacher_view_privacy_policy"


    //new Apis

    const val CLASS_TEACHER_ENDPOINT = "class_teacher"
    const val ATTENDANCE_ENDPOINT = "attendance"
    const val REFRESH_TOKEN_ENDPOINT = "refresh_token"
    const val EVENTS_ENDPOINT = "events"
    const val CIRCULARS_ENDPOINT = "circulars"
    const val ACTIVE_STUDENTS_ENDPOINT = "fetch_active_students"
    const val INACTIVE_STUDENTS_ENDPOINT = "fetch_inactive_students"
    const val PLANNERS_AND_RESOURCES_ENDPOINT = "planners_resources"

}
