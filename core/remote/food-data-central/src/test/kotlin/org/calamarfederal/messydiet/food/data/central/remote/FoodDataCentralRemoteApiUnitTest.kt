package org.calamarfederal.messydiet.food.data.central.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import org.calamarfederal.messydiet.food.data.central.di.API_KEY_TAG
import org.calamarfederal.messydiet.food.data.central.di.testDi
import org.calamarfederal.messydiet.food.data.central.remote.schema.AbridgedFoodItemSchema
import org.calamarfederal.messydiet.food.data.central.remote.schema.BrandedFoodItemSchema
import org.calamarfederal.messydiet.food.data.central.remote.schema.DataTypeSchema.Foundation
import org.calamarfederal.messydiet.food.data.central.remote.schema.DataTypeSchema.SRLegacy
import org.calamarfederal.messydiet.food.data.central.remote.schema.FoodSearchCriteriaSchema
import org.calamarfederal.messydiet.food.data.central.remote.schema.FoodSearchCriteriaSchema.TradeChannel.ChildNutritionFoodPrograms
import org.calamarfederal.messydiet.food.data.central.remote.schema.FoodSearchCriteriaSchema.TradeChannel.Grocery
import org.calamarfederal.messydiet.food.data.central.remote.schema.SearchResultSchema
import org.calamarfederal.messydiet.food.data.central.remote.schema.SortBySchema.DataTypeKeyword
import org.calamarfederal.messydiet.food.data.central.remote.schema.SortOrderSchema.Ascending
import org.calamarfederal.messydiet.remote.food.data.central.test.FoodItemExpect
import org.kodein.di.direct
import org.kodein.di.instance
import retrofit2.Response
import kotlin.test.BeforeTest
import kotlin.test.Test

private fun <T> Response<T>.assertSuccessful(
    lazyMessage: (Response<T>) -> Unit = {},
): Response<T> {
    assert(isSuccessful) {
        println("response UNSUCCESSFUL:")
        println("code: ${code()}")
        println("message: ${message()}")
        lazyMessage(this)
    }
    return this
}

internal class FoodDataCentralRemoteApiUnitTest {
    private lateinit var fdcApi: FoodDataCentralApi
    private lateinit var testApiKey: String

    @BeforeTest
    fun setUp() {
        fdcApi = testDi.direct.instance()
        testApiKey = testDi.direct.instance(tag = API_KEY_TAG)
    }


    @Test
    fun `Post food search `() {
        val result = fdcApi.postFoodsSearch(
            testApiKey,
            FoodSearchCriteriaSchema(
                query = "Cheddar Cheese",
                dataType = listOf(
                    Foundation,
                    SRLegacy,
                ),
                pageSize = 25,
                pageNumber = 2,
                sortBy = DataTypeKeyword,
                sortOrder = Ascending,
                brandOwner = "Kar Nut Products Company",
                tradeChannel = listOf(
                    ChildNutritionFoodPrograms,
                    Grocery,
                ),
                startDate = "2021-01-01",
                endDate = "2021-12-30",
            )
        ).execute().assertSuccessful() {
            val body = it.body()!!
            println("$body")
        }.body()!!
        println("$result")
    }
}

internal class FoodDataCentralRemoteApiPrettyPrinter {
    private lateinit var fdcApi: FoodDataCentralApi
    private lateinit var testApiKey: String

    @BeforeTest
    fun setUp() {
        fdcApi = testDi.direct.instance<FoodDataCentralApi>()
        testApiKey = testDi.direct.instance(tag = API_KEY_TAG)
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun prettyPrintGetFoodAbridged(fdcId: String, nutrients: List<Int>? = null) {
        val result = fdcApi.getFoodWithFdcIdAbridged(
            apiKey = testApiKey,
            fdcId = fdcId,
            format = "abridged",
            nutrients = nutrients,
        ).execute().assertSuccessful().body()!!

        println(
            Moshi.Builder().build().adapter<AbridgedFoodItemSchema>().indent("    ").toJson(result)
        )
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun prettyPrintGetFoodBranded(fdcId: String, nutrients: List<Int>? = null, formatFull: Boolean = false) {
        val result = fdcApi.getFoodWithFdcIdBranded(
            apiKey = testApiKey,
            fdcId = fdcId,
            format = if (formatFull) "full" else null,
            nutrients = nutrients,
        ).execute().assertSuccessful().body()!!

        println(
            Moshi.Builder().build().adapter<BrandedFoodItemSchema>().indent("    ").toJson(result)
        )
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun prettyPrintSearchFood(criteriaSchema: FoodSearchCriteriaSchema) {
        val result = fdcApi.postFoodsSearch(
            apiKey = testApiKey,
            requestBody = criteriaSchema,
        ).execute().assertSuccessful().body()!!

        println(Moshi.Builder().build().adapter<SearchResultSchema>().indent("    ").toJson(result))
    }

    @Test
    fun `One off testing`() {
        prettyPrintGetFoodBranded(
            fdcId = FoodItemExpect.SpriteTest.fdcIdString,
        )
    }
}
