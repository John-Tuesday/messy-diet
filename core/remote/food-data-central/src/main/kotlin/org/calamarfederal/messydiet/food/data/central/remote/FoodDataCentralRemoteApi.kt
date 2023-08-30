package org.calamarfederal.messydiet.food.data.central.remote

import org.calamarfederal.messydiet.food.data.central.remote.schema.*
import org.calamarfederal.messydiet.food.data.central.remote.schema.AbridgedFoodItemSchema
import org.calamarfederal.messydiet.food.data.central.remote.schema.BrandedFoodItemSchema
import org.calamarfederal.messydiet.food.data.central.remote.schema.FoodListCriteriaSchema
import org.calamarfederal.messydiet.food.data.central.remote.schema.FoodSearchCriteriaSchema
import org.calamarfederal.messydiet.food.data.central.remote.schema.FoodsCriteriaSchema
import org.calamarfederal.messydiet.food.data.central.remote.schema.FoundationFoodItemSchema
import org.calamarfederal.messydiet.food.data.central.remote.schema.SRLegacyFoodItemSchema
import org.calamarfederal.messydiet.food.data.central.remote.schema.SearchResultSchema
import org.calamarfederal.messydiet.food.data.central.remote.schema.SurveyFoodItemSchema
import retrofit2.Call
import retrofit2.http.*

private const val API_KEY_HEADER = "X-Api-Key"

internal interface FoodDataCentralApi {
    @GET("v1/food/{fdcId}")
    fun getFoodWithFdcIdAbridged(
        @Header(API_KEY_HEADER) apiKey: String,
        @Path("fdcId") fdcId: String,
        @Query("format") format: String? = null,
        @Query("nutrients") nutrients: List<Int>? = null,
    ): Call<AbridgedFoodItemSchema>

    @GET("v1/food/{fdcId}")
    fun getFoodWithFdcIdBranded(
        @Header(API_KEY_HEADER) apiKey: String,
        @Path("fdcId") fdcId: String,
        @Query("format") format: String? = null,
        @Query("nutrients") nutrients: List<Int>? = null,
    ): Call<BrandedFoodItemSchema>

    @GET("v1/food/{fdcId}")
    fun getFoodWithFdcIdFoundation(
        @Header(API_KEY_HEADER) apiKey: String,
        @Path("fdcId") fdcId: String,
        @Query("format") format: String? = null,
        @Query("nutrients") nutrients: List<Int>? = null,
    ): Call<FoundationFoodItemSchema>

    @GET("v1/food/{fdcId}")
    fun getFoodWithFdcIdLegacy(
        @Header(API_KEY_HEADER) apiKey: String,
        @Path("fdcId") fdcId: String,
        @Query("format") format: String? = null,
        @Query("nutrients") nutrients: List<Int>? = null,
    ): Call<SRLegacyFoodItemSchema>

    @GET("v1/food/{fdcId}")
    fun getFoodWithFdcIdSurvey(
        @Header(API_KEY_HEADER) apiKey: String,
        @Path("fdcId") fdcId: String,
        @Query("format") format: String? = null,
        @Query("nutrients") nutrients: List<Int>? = null,
    ): Call<SurveyFoodItemSchema>


    @POST("v1/foods")
    fun postFoodsWithFdcId(
        @Header(API_KEY_HEADER) apiKey: String,
        @Body requestBody: FoodsCriteriaSchema,
    ): Call<List<AbridgedFoodItemSchema>>

    @POST("v1/foods")
    fun postFoodsWithFdcIdBranded(
        @Header(API_KEY_HEADER) apiKey: String,
        @Body requestBody: FoodsCriteriaSchema,
    ): Call<List<AbridgedFoodItemSchema>>

    @GET("v1/foods/list")
    fun getFoodsList(
        @Header(API_KEY_HEADER) apiKey: String,
        @Query("dataType") dataType: Array<String>? = null,
        @Query("pageSize") pageSize: Int? = null,
        @Query("pageNumber") pageNumber: Int? = null,
        @Query("sortBy") sortBy: String? = null,
        @Query("sortOrder") sortOrder: String? = null,
    ): Call<List<AbridgedFoodItemSchema>>

    @POST("v1/foods/list")
    fun postFoodsList(
        @Header(API_KEY_HEADER) apiKey: String,
        @Body foodsCriteria: FoodListCriteriaSchema = FoodListCriteriaSchema(),
    ): Call<List<AbridgedFoodItemSchema>>

    @GET("v1/foods/search")
    fun getFoodsSearch(
        @Header(API_KEY_HEADER) apiKey: String,
        @Query("query") searchQuery: String,
        @Query("dataType") dataType: List<String>? = null,
        @Query("pageSize") pageSize: Int? = null,
        @Query("pageNumber") pageNumber: Int? = null,
        @Query("sortBy") sortBy: String? = null,
        @Query("sortOrder") sortOrder: String? = null,
        @Query("brandOwner") brandOwner: String? = null,
    ): Call<SearchResultSchema>

    @POST("v1/foods/search")
    fun postFoodsSearch(
        @Header(API_KEY_HEADER) apiKey: String,
        @Body requestBody: FoodSearchCriteriaSchema,
    ): Call<SearchResultSchema>
}
