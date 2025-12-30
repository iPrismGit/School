import com.iprism.school.network.StaffApiService
import com.iprism.school.utils.AuthInterceptor
import com.iprism.school.utils.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object SchoolApi {

    private const val TIMEOUT = 30L
    private var authToken: String? = null

    fun setAuthToken(token: String) {
        authToken = token
    }

    private val okHttpClient: OkHttpClient
        get() {
            val builder = OkHttpClient.Builder()
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)

            authToken?.let {
                builder.addInterceptor(AuthInterceptor(it))
            }

            return builder.build()
        }

    private val retrofit: Retrofit
        get() = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val schoolApiService: StaffApiService
        get() = retrofit.create(StaffApiService::class.java)

}
