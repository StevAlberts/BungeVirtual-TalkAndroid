package com.nextcloud.talk.api

import android.content.Context
import android.util.Log
import com.nextcloud.talk.BuildConfig
import com.nextcloud.talk.R
import com.nextcloud.talk.utils.ApiUtils
import okhttp3.Dispatcher
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitHelper(private val context: Context) {

    private val TAG_API_SERVICE  = RetrofitHelper::class.simpleName

    // TODO: Make dynamic url here for .ml
    // init the api service
    fun getApiService(baseURL: String): ApiService {
        // log baseURL
        Log.d(TAG_API_SERVICE, "getApiService baseURL....: $baseURL")

        return Retrofit.Builder()
            .baseUrl(baseURL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient())
            .build()
            .create(ApiService::class.java)
    }

    private fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor { message -> Log.d(TAG_API_SERVICE, message) }
        if (BuildConfig.DEBUG) {
            interceptor.level = HttpLoggingInterceptor.Level.BODY
        }

        return interceptor
    }

    private fun provideHttpResponseInterceptor() = Interceptor { chain ->
        Log.d(TAG_API_SERVICE, "provideHttpResponseInterceptor.....................")
        val request = chain.request()
        val response = chain.proceed(request)
        if (response.code == 403) {
        // todo check if access token has expired or is invalid

        }
        response
    }


    private fun okHttpClient() : OkHttpClient {

        val dispatcher = Dispatcher()
        dispatcher.maxRequests = 100
        dispatcher.maxRequestsPerHost =10

        return  OkHttpClient
            .Builder()
            .addInterceptor(provideHttpLoggingInterceptor())
            .addInterceptor(provideHttpResponseInterceptor())
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .cache(null)
            .build()
    }



}