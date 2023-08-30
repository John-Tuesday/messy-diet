package org.calamarfederal.messydiet.food.data.central.di

import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.OkHttpClient
import org.calamarfederal.messydiet.food.data.central.*
import org.calamarfederal.messydiet.food.data.central.remote.FoodDataCentralApi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

internal const val BASE_URL = "https://api.nal.usda.gov/fdc/"

internal fun provideOkClient() = OkHttpClient
    .Builder()
    .connectTimeout(1.minutes.toJavaDuration())
    .readTimeout(1.minutes.toJavaDuration())
    .writeTimeout(30.seconds.toJavaDuration())
    .build()

internal fun provideFdcApi(
    client: OkHttpClient = provideOkClient(),
): FoodDataCentralApi = Retrofit
    .Builder()
    .baseUrl(BASE_URL)
    .client(client)
    .addConverterFactory(MoshiConverterFactory.create())
    .build()
    .create()

internal fun provideFdcRemoteSource(
    networkDispatcher: CoroutineDispatcher,
    apiKey: String,
): FoodDataCentralRemoteSource = FoodDataCentralRemoteSourceImplementation(
    networkDispatcher = networkDispatcher,
    apiKey = apiKey,
    fdcApi = provideFdcApi(),
)

internal fun provideFoodDataCenterRepository(
    networkDispatcher: CoroutineDispatcher,
    apiKey: String,
): FoodDataCentralRepository = FoodDataCentralRepositoryImplementation(
    dispatcher = networkDispatcher,
    fdcSource = provideFdcRemoteSource(networkDispatcher, apiKey),
)

data object FoodDataCentral {
    internal fun foodDataCentralApi() = provideFdcApi()
    internal fun foodDataCentralRemoteSource(
        dispatcher: CoroutineDispatcher,
        apiKey: String,
    ) = provideFdcRemoteSource(
        networkDispatcher = dispatcher,
        apiKey = apiKey,
    )

    fun repository(
        networkDispatcher: CoroutineDispatcher,
        apiKey: String,
    ): FoodDataCentralRepository = FoodDataCentralRepositoryImplementation(
        dispatcher = networkDispatcher,
        fdcSource = provideFdcRemoteSource(networkDispatcher, apiKey),
    )
}
