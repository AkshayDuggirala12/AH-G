package com.example.hara_gym.data.api

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.hara_gym.data.model.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import javax.inject.Singleton

// --- TOKEN MANAGEMENT ---
private val Context.dataStore by preferencesDataStore(name = "settings")

class TokenManager(private val context: Context) {
    companion object {
        private val TOKEN_KEY = stringPreferencesKey("jwt_token")
    }

    val token: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[TOKEN_KEY]
    }

    suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    suspend fun clearToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(TOKEN_KEY)
        }
    }
}

// --- API SERVICE INTERFACE ---
interface ApiService {
    @POST("auth/register")
    suspend fun register(@Body body: RegisterRequest): Response<UserDto>

    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): Response<LoginResponse>

    @GET("clients/my-plans")
    suspend fun getMyPlans(): Response<MyPlansResponse>

    @POST("clients/access-request")
    suspend fun submitAccessRequest(@Body body: ClientAccessRequestBody): Response<ClientAccessRequestDto>

    @GET("clients/access-request/me")
    suspend fun getMyAccessRequests(): Response<List<ClientAccessRequestDto>>

    @GET("workouts/my-plan")
    suspend fun getMyWorkoutPlan(): Response<WorkoutPlanDto>

    @GET("diets/my-plan")
    suspend fun getMyDietPlan(): Response<DietPlanDto>

    @GET("progress/weekly")
    suspend fun getWeeklyProgress(
        @Query("progress_date") progressDate: String? = null
    ): Response<List<WeeklyProgressItemDto>>

    @GET("progress/days/{dayName}")
    suspend fun getDayProgress(
        @Path("dayName") dayName: String,
        @Query("progress_date") progressDate: String? = null
    ): Response<WorkoutDayProgressDto>

    @POST("progress/exercise/toggle")
    suspend fun toggleExercise(
        @Body body: ToggleExerciseRequest
    ): Response<WorkoutDayProgressDto>
}

// --- NETWORK CONFIGURATION (Hilt Module) ---
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://akshayduggirala09.pythonanywhere.com/"

    @Provides
    @Singleton
    fun provideTokenManager(@ApplicationContext context: Context): TokenManager {
        return TokenManager(context)
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(tokenManager: TokenManager): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            val path = originalRequest.url.encodedPath
            
            if (path.contains("/auth/login") || path.contains("/auth/register")) {
                return@Interceptor chain.proceed(originalRequest)
            }

            val token = runBlocking { tokenManager.token.first() }
            val requestBuilder = originalRequest.newBuilder()
            if (!token.isNullOrBlank()) {
                requestBuilder.addHeader("Authorization", "Bearer $token")
            }
            chain.proceed(requestBuilder.build())
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: Interceptor): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}
