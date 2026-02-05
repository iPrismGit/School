
import android.os.Bundle

import com.iprism.school.base.BaseActivity

import com.iprism.school.databinding.ActivitySentMessagesBinding
import com.iprism.school.utils.User

class SentMessagesActivity : BaseActivity() {

    private lateinit var binding: ActivitySentMessagesBinding
    private var teacherId: String = ""
    private var auth_token: String = ""
    private var scl_id: String = ""
    private var emp_designation: String = ""
    private var emp_name: String = ""

    private var tag: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySentMessagesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        teacherId = userDetails[User.ID].toString()
        auth_token = userDetails[User.AUTH_TOKEN].toString()
        scl_id = userDetails[User.SCHOOL_ID].toString()

        tag = intent.getStringExtra("tag").toString()

        if (tag == ""){
            binding.activityTitleTxt.text = "Sent messages"
        }else{
            binding.activityTitleTxt.text = "Scheduled messages"
        }

        emp_name = userDetails[User.EMP_NAME].toString()
        emp_designation = userDetails[User.EMP_DESIGNATION].toString()

    }


}