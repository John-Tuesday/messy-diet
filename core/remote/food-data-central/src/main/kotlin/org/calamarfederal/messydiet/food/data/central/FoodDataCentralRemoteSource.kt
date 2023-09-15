package org.calamarfederal.messydiet.food.data.central

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.calamarfederal.messydiet.food.data.central.model.*
import org.calamarfederal.messydiet.food.data.central.remote.FoodDataCentralApi
import retrofit2.Response


/**
 * Interface for remote
 */
internal interface FoodDataCentralRemoteSource {
    suspend fun searchFood(searchCriteria: SearchCriteriaBuilder.() -> Unit): ResultResponse<FDCSearchResult, FoodDataCentralError>
    suspend fun searchFoodNormal(searchCriteria: SearchCriteriaBuilder): ResultResponse<FDCSearchResult, FoodDataCentralError>

    suspend fun getFoodByFdcId(
        fdcId: FDCId,
        abridged: Boolean = false,
    ): ResultResponse<FDCFoodItem, FoodDataCentralError>
}

internal class FoodDataCentralRemoteSourceImplementation(
    private val networkDispatcher: CoroutineDispatcher,
    private val apiKey: String,
    private val fdcApi: FoodDataCentralApi,
) : FoodDataCentralRemoteSource {

    override suspend fun searchFoodNormal(searchCriteria: SearchCriteriaBuilder): ResultResponse<FDCSearchResult, FoodDataCentralError> =
        withContext(networkDispatcher) {
            val response = fdcApi.postFoodsSearch(
                apiKey = apiKey,
                requestBody = searchCriteria.build(),
            ).execute()

            if (response.isSuccessful)
                return@withContext ResultResponse.Success(response.body()!!.toFDCSearchResult())
            return@withContext ResultResponse.Failure(
                toFoodDataCentralError(response)
            )
        }

    override suspend fun searchFood(searchCriteria: SearchCriteriaBuilder.() -> Unit): ResultResponse<FDCSearchResult, FoodDataCentralError> =
        withContext(networkDispatcher) {
            val criteria = SearchCriteriaBuilder("").run {
                searchCriteria()
                build()
            }
            val response = fdcApi.postFoodsSearch(
                apiKey = apiKey,
                requestBody = criteria,
            ).execute()

            if (response.isSuccessful)
                return@withContext ResultResponse.Success(response.body()!!.toFDCSearchResult())
            return@withContext ResultResponse.Failure(
                toFoodDataCentralError(response)
            )
        }


    private fun toFoodDataCentralError(response: Response<*>): FoodDataCentralError {
        return FoodDataCentralError.fromCode(response.code(), response.message())
    }

    override suspend fun getFoodByFdcId(
        fdcId: FDCId,
        abridged: Boolean,
    ): ResultResponse<FDCFoodItem, FoodDataCentralError> =
        withContext(networkDispatcher) {
            val fdcIdString = fdcId.fdcId.toString()
            if (abridged) {
                val response = fdcApi.getFoodWithFdcIdAbridged(
                    apiKey = apiKey,
                    fdcId = fdcIdString,
                    format = "abridged",
                ).execute()

                if (!response.isSuccessful)
                    ResultResponse.Failure(toFoodDataCentralError(response))
                else
                    ResultResponse.Success(response.body()!!.toModel())
            } else
                when (fdcId) {
                    is BrandedFDCId -> {
                        val response = fdcApi.getFoodWithFdcIdBranded(
                            apiKey = apiKey,
                            fdcId = fdcIdString,
                        ).execute()

                        if (!response.isSuccessful)
                            ResultResponse.Failure(toFoodDataCentralError(response))
                        else
                            ResultResponse.Success(response.body()!!.toModel())
                    }

                    is FoundationFdcId -> {
                        val response = fdcApi.getFoodWithFdcIdFoundation(
                            apiKey = apiKey,
                            fdcId = fdcIdString,
                        ).execute()

                        if (!response.isSuccessful)
                            ResultResponse.Failure(toFoodDataCentralError(response))
                        else
                            ResultResponse.Success(response.body()!!.toModel())
                    }

                    is LegacyFdcId -> {
                        val response = fdcApi.getFoodWithFdcIdLegacy(
                            apiKey = apiKey,
                            fdcId = fdcIdString,
                        ).execute()

                        if (!response.isSuccessful)
                            ResultResponse.Failure(toFoodDataCentralError(response))
                        else
                            ResultResponse.Success(response.body()!!.toModel())
                    }

                    is SurveyFdcId -> {
                        val response = fdcApi.getFoodWithFdcIdSurvey(
                            apiKey = apiKey,
                            fdcId = fdcIdString,
                        ).execute()

                        if (!response.isSuccessful)
                            ResultResponse.Failure(toFoodDataCentralError(response))
                        else
                            ResultResponse.Success(response.body()!!.toModel())
                    }
                }
        }
}
