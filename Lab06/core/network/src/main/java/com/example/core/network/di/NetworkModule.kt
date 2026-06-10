package com.example.core.network.di

import com.example.core.network.api.CveApiService
import com.example.core.network.api.MockInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            // .addInterceptor(MockInterceptor()) // Comentado para usar la API Real de NVD
            .addInterceptor { chain ->
                val originalRequest = chain.request()
                // Si el usuario tiene una API Key, puede colocarla aquí para evitar límites estrictos de tasa.
                val apiKey = "" // Inserta tu clave de API de NVD aquí si tienes una
                val requestBuilder = originalRequest.newBuilder()
                if (apiKey.isNotEmpty()) {
                    requestBuilder.header("apiKey", apiKey)
                }
                chain.proceed(requestBuilder.build())
            }
            .build()
    }


    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://services.nvd.nist.gov/rest/json/cves/2.0/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideCveApiService(retrofit: Retrofit): CveApiService {
        return retrofit.create(CveApiService::class.java)
    }
}
