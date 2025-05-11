package com.example.hodos_final_android.service.api


import Resource
import Resource.Error
import Resource.Loading
import Resource.Success
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.airbnb.lottie.BuildConfig
import com.example.hodos_final_android.helper.TokenManager
import com.example.hodos_final_android.service.AuthService
import com.example.hodos_final_android.service.ChatBotService
import com.example.hodos_final_android.service.CommonService
import com.example.hodos_final_android.service.LocationService
import com.example.hodos_final_android.service.PlanTripService
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import okio.Buffer
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton



class TokenProvider @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    fun getToken(): String? {
        return TokenManager.getInstance().getAccessToken()
    }
}

class TokenInterceptor(private val tokenProvider: TokenProvider) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = tokenProvider.getToken()
        val requestBuilder = chain.request().newBuilder()

        token?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }

        return chain.proceed(requestBuilder.build())
    }
}
class ApiLoggerInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()

        val requestBody = request.body
        val bodyString = requestBody?.let {
            val buffer = Buffer()
            it.writeTo(buffer)
            buffer.readUtf8()
        } ?: "No Body"

        Log.i("API", "➡️ API Request: ${request.method} ${request.url}")
        Log.i("API", "Headers: ${request.headers}")
        Log.i("API", "Body: $bodyString")

        val response = chain.proceed(request)

        val responseBody = response.body
        val responseBodyString = responseBody?.string() ?: "No Body"

        Log.i("API", "⬅️ API Response: ${response.code} ${response.message}")
        Log.i("API", "Response Body: $responseBodyString")

        // clone lại response body để không bị mất sau khi đọc
        val newResponseBody = responseBodyString.toByteArray().let {
            okhttp3.ResponseBody.create(responseBody?.contentType(), it)
        }

        return response.newBuilder().body(newResponseBody).build()
    }
}


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        TokenManager.init(context)
        return context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideTokenProvider(sharedPreferences: SharedPreferences): TokenProvider {
        return TokenProvider(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        tokenProvider: TokenProvider
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(ApiLoggerInterceptor())
            .addInterceptor(TokenInterceptor(tokenProvider))
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            //.baseUrl("https://hodos-api.genny.id.vn/")
            .baseUrl("http://192.168.1.6:3000/")
            //.baseUrl("http://192.168.2.88:3000/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit): AuthService {
        return retrofit.create(AuthService::class.java)
    }
    @Provides
    @Singleton
    fun provideLocationService(retrofit: Retrofit): LocationService {
        return retrofit.create(LocationService::class.java)
    }
    @Provides
    @Singleton
    fun provideCommonService(retrofit: Retrofit): CommonService {
        return retrofit.create(CommonService::class.java)
    }

    @Provides
    @Singleton
    fun provideChatBotService(retrofit: Retrofit): ChatBotService {
        return retrofit.create(ChatBotService::class.java)
    }

    @Provides
    @Singleton
    fun providePlanTripService(retrofit: Retrofit): PlanTripService {
        return retrofit.create(PlanTripService::class.java)
    }
}

data class ErrorRes(
    val message: String,
    val statusCode: Int? = 0,
    val timestamp: String= "",
    val name: String = ""
)

fun parseJsonError(errorString: String): ErrorRes? {
    val jsonPart = errorString.substringAfter("Error: ").trim()
    return try {
        Gson().fromJson(jsonPart, ErrorRes::class.java)
    } catch (e: JsonSyntaxException) {
        return null
    }

}

inline fun <T> safeApiCall(
    crossinline apiCall: suspend () -> T
): Flow<Resource<T>> = flow {
    emit(Loading())

    try {
        val response = apiCall()
        emit(Success(response))
    } catch (e: HttpException) {
        val errorBody = e.response()?.errorBody()?.string()
        val statusCode = e.code()
        val message = errorBody ?: e.message()

        emit(Error(message = "Error: $message", statusCode = statusCode))
    } catch (e: IOException) {
        emit(Error(message = "Couldn't reach server. Check your internet connection."))
    } catch (e: Exception) {
        emit(Error(message = "An unexpected error occurred: ${e.message}"))
    }
}
