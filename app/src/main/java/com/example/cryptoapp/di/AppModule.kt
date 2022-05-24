package com.example.cryptoapp.di

import android.app.Application
import android.os.SystemClock
import androidx.room.Room
import coil.ImageLoader
import coil.decode.SvgDecoder
import com.example.cryptoapp.data.local.CryptoDataBase
import com.example.cryptoapp.data.remote.CryptoApi
import com.example.cryptoapp.data.repository.CryptoRepositoryImpl
import com.example.cryptoapp.domain.repository.CryptoRepository
import com.example.cryptoapp.util.Constants.BASE_URL
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.util.concurrent.RateLimiter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCryptoApi(): CryptoApi {
        val builder = OkHttpClient.Builder()
        val limiter: RateLimiter = RateLimiter.create(10.0)
        val interceptor = Interceptor { chain ->
            limiter.acquire(1)
            chain.proceed(chain.request())
        }
         val okHttpClient = builder.addNetworkInterceptor(interceptor).build()



        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CryptoApi::class.java)
    }


    @Provides
    @Singleton
    fun provideStockDatabase(app: Application): CryptoDataBase {
        return Room.databaseBuilder(
            app,
            CryptoDataBase::class.java,
            "stockdb.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideCoinRepository(api:CryptoApi, db: CryptoDataBase): CryptoRepository {
        return CryptoRepositoryImpl(api, db)
    }

    @Provides
    @Singleton
    fun provideImageLoader(app: Application):ImageLoader{
        return ImageLoader.Builder(app)
            .components {
                add(SvgDecoder.Factory())
            }
            .build()
    }



}