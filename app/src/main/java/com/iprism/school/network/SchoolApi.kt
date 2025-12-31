import com.iprism.school.network.StaffApiService
import com.iprism.school.utils.AuthInterceptor
import com.iprism.school.utils.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object SchoolApi {

    private const val TIMEOUT = 30L

    private val authInterceptor = AuthInterceptor()

    fun setAuthToken(token: String) {
        authInterceptor.token = token
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(authInterceptor)
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val schoolApiService: StaffApiService by lazy {
        retrofit.create(StaffApiService::class.java)
    }

}
