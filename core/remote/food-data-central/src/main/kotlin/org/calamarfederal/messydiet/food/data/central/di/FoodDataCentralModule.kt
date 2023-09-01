package org.calamarfederal.messydiet.food.data.central.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import org.calamarfederal.messydiet.food.data.central.*
import org.calamarfederal.messydiet.food.data.central.remote.FoodDataCentralApi
import org.kodein.di.*
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

internal const val API_KEY_TAG = "fdc_api_key"
internal const val BASE_URL_KEY_TAG = "fdc_base_url"
internal const val NETWORK_DISPATCHER_TAG = "network_dispatcher"

internal val backendDiModule = DI.Module(name = "backendDiModule") {
    bindConstant<String>(tag = API_KEY_TAG) { "DEMO_KEY" }
    bindConstant<String>(tag = BASE_URL_KEY_TAG) { "https://api.nal.usda.gov/fdc/" }

    bindProvider(tag = NETWORK_DISPATCHER_TAG) { Dispatchers.IO }

    bindSingleton<OkHttpClient> {
        OkHttpClient
            .Builder()
            .connectTimeout(1.minutes.toJavaDuration())
            .readTimeout(1.minutes.toJavaDuration())
            .writeTimeout(30.seconds.toJavaDuration())
            .build()
    }
    bindProvider<Converter.Factory> { MoshiConverterFactory.create() }
    bindProvider<FoodDataCentralApi> {
        Retrofit
            .Builder()
            .baseUrl(instance<String>(tag = BASE_URL_KEY_TAG))
            .client(instance())
            .addConverterFactory(instance())
            .build()
            .create()
    }

    bindProvider<FoodDataCentralRemoteSource> {
        FoodDataCentralRemoteSourceImplementation(
            networkDispatcher = instance(tag = NETWORK_DISPATCHER_TAG),
            apiKey = instance<String>(tag = API_KEY_TAG),
            fdcApi = instance(),
        )
    }

    bindProvider<FoodDataCentralRepository> {
        FoodDataCentralRepositoryImplementation(
            dispatcher = instance(tag = NETWORK_DISPATCHER_TAG),
            fdcSource = instance(),
        )
    }
}

internal val internalDi = DI {
    import(backendDiModule)

}

data object FoodDataCentral {
    internal fun foodDataCentralApi(): FoodDataCentralApi = internalDi.direct.instance()
    internal fun foodDataCentralRemoteSource(
        dispatcher: CoroutineDispatcher,
        apiKey: String,
    ): FoodDataCentralRemoteSource = internalDi.direct.instance()

    fun repository(
        networkDispatcher: CoroutineDispatcher,
        apiKey: String,
    ): FoodDataCentralRepository = internalDi.direct.instance()
}
