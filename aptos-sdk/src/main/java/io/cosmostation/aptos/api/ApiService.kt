package io.cosmostation.aptos.api

import io.cosmostation.aptos.AptosClient
import io.cosmostation.aptos.BuildConfig
import io.cosmostation.aptos.model.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface ApiService {
    companion object {
        fun create(): ApiService {
            val builder = Retrofit.Builder().baseUrl(AptosClient.instance.currentNetwork.rpcUrl)
                .addConverterFactory(GsonConverterFactory.create())

            if (BuildConfig.DEBUG) {
                val interceptor = HttpLoggingInterceptor()
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
                val client = OkHttpClient.Builder().addInterceptor(interceptor)
                    .connectTimeout(10, TimeUnit.SECONDS).build()
                builder.client(client)
            }

            return builder.build().create(ApiService::class.java)
        }
    }

    @GET("/v1/accounts/{address}")
    fun getAccount(@Path("address") address: String): Call<Account>

    @GET("/v1/accounts/{address}/resources")
    fun getAccountResource(@Path("address") address: String): Call<List<AccountResource>>

    @GET("v1/accounts/{address}/transactions")
    fun getAccountTransactions(@Path("address") address: String): Call<List<Transaction>>

    @POST("/v1/transactions/encode_submission")
    fun encodeSubmission(@Body msg: EncodeRequest): Call<String>

    @POST("/v1/transactions")
    @Headers("Content-Type: application/json")
    fun submitTransaction(@Body submitRequest: SubmitRequest): Call<Transaction>
}