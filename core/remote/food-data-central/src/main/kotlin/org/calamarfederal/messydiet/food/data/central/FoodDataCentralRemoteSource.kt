package org.calamarfederal.messydiet.food.data.central

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.calamarfederal.messydiet.food.data.central.model.*
import org.calamarfederal.messydiet.food.data.central.remote.FoodDataCentralApi


internal interface FoodDataCentralRemoteSource {
    suspend fun searchFood(searchCriteria: SearchCriteriaBuilder.() -> Unit): ResultResponse<FDCSearchResult, FDCError>
    suspend fun searchFoodNormal(searchCriteria: SearchCriteriaBuilder): ResultResponse<FDCSearchResult, FDCError>

    suspend fun getFoodByFdcId(fdcId: FDCId): ResultResponse<FDCFoodItem, FDCError>
}

internal class FoodDataCentralRemoteSourceImplementation constructor(
    private val networkDispatcher: CoroutineDispatcher,
    private val apiKey: String,
    private val fdcApi: FoodDataCentralApi,
) : FoodDataCentralRemoteSource {

    override suspend fun searchFoodNormal(searchCriteria: SearchCriteriaBuilder): ResultResponse<FDCSearchResult, FDCError> =
        withContext(networkDispatcher) {
            val response = fdcApi.postFoodsSearch(
                apiKey = apiKey,
                requestBody = searchCriteria.build(),
            ).execute()

            if (response.isSuccessful)
                return@withContext ResultResponse.Success(response.body()!!.toFDCSearchResult())
            return@withContext ResultResponse.Failure(
                FDCError.NetworkError(
                    message = response.message(),
                    code = response.code()
                )
            )
        }

    override suspend fun searchFood(searchCriteria: SearchCriteriaBuilder.() -> Unit): ResultResponse<FDCSearchResult, FDCError> =
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
                FDCError.NetworkError(
                    message = response.message(),
                    code = response.code()
                )
            )
        }

    override suspend fun getFoodByFdcId(fdcId: FDCId): ResultResponse<FDCFoodItem, FDCError> =
        withContext(networkDispatcher) {
            val fdcIdString = fdcId.fdcId.toString()
            when (fdcId) {
                is BrandedFDCId -> {
                    val response = fdcApi.getFoodWithFdcIdBranded(
                        apiKey = apiKey,
                        fdcId = fdcIdString,
                    ).execute()
                    if (!response.isSuccessful) ResultResponse.Failure(
                        FDCError.NetworkError(
                            message = response.message(),
                            code = response.code()
                        )
                    ) else ResultResponse.Success(response.body()!!.toModel())
                }

                is FoundationFdcId -> {
                    val response = fdcApi.getFoodWithFdcIdFoundation(
                        apiKey = apiKey,
                        fdcId = fdcIdString,
                    ).execute()
                    if (!response.isSuccessful) ResultResponse.Failure(
                        FDCError.NetworkError(
                            message = response.message(),
                            code = response.code()
                        )
                    ) else ResultResponse.Success(response.body()!!.toModel())
                }

                is LegacyFdcId -> {
                    val response = fdcApi.getFoodWithFdcIdLegacy(
                        apiKey = apiKey,
                        fdcId = fdcIdString,
                    ).execute()
                    if (!response.isSuccessful) ResultResponse.Failure(
                        FDCError.NetworkError(
                            message = response.message(),
                            code = response.code()
                        )
                    ) else ResultResponse.Success(response.body()!!.toModel())
                }

                is SurveyFdcId -> {
                    val response = fdcApi.getFoodWithFdcIdSurvey(
                        apiKey = apiKey,
                        fdcId = fdcIdString,
                    ).execute()
                    if (!response.isSuccessful) ResultResponse.Failure(
                        FDCError.NetworkError(
                            message = response.message(),
                            code = response.code()
                        )
                    ) else ResultResponse.Success(response.body()!!.toModel())
                }
            }
        }
}
