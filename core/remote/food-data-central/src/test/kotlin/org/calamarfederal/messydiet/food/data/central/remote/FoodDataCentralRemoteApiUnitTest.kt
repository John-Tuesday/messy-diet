package org.calamarfederal.messydiet.food.data.central.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import org.calamarfederal.messydiet.food.data.central.BadSpriteTest
import org.calamarfederal.messydiet.food.data.central.SpriteTest
import org.calamarfederal.messydiet.food.data.central.TestApiKey
import org.calamarfederal.messydiet.food.data.central.di.FoodDataCentral
import org.calamarfederal.messydiet.food.data.central.remote.schema.AbridgedFoodItemSchema
import org.calamarfederal.messydiet.food.data.central.remote.schema.AbridgedFoodNutrientSchema
import org.calamarfederal.messydiet.food.data.central.remote.schema.BrandedFoodItemSchema
import org.calamarfederal.messydiet.food.data.central.remote.schema.DataTypeSchema.Foundation
import org.calamarfederal.messydiet.food.data.central.remote.schema.DataTypeSchema.SRLegacy
import org.calamarfederal.messydiet.food.data.central.remote.schema.FoodSearchCriteriaSchema
import org.calamarfederal.messydiet.food.data.central.remote.schema.FoodSearchCriteriaSchema.TradeChannel.ChildNutritionFoodPrograms
import org.calamarfederal.messydiet.food.data.central.remote.schema.FoodSearchCriteriaSchema.TradeChannel.Grocery
import org.calamarfederal.messydiet.food.data.central.remote.schema.SearchResultFoodSchema
import org.calamarfederal.messydiet.food.data.central.remote.schema.SortBySchema.DataTypeKeyword
import org.calamarfederal.messydiet.food.data.central.remote.schema.SortOrderSchema.Ascending
import org.calamarfederal.messydiet.food.data.central.searchQuery
import org.junit.Before
import org.junit.Test
import retrofit2.Response

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

@OptIn(ExperimentalStdlibApi::class)
private fun prettyPrint(nutrient: AbridgedFoodNutrientSchema) {
    println(Moshi.Builder().build().adapter<AbridgedFoodNutrientSchema>().indent("    ").toJson(nutrient))
}

@OptIn(ExperimentalStdlibApi::class)
private fun prettyPrint(item: AbridgedFoodItemSchema) {
    println(Moshi.Builder().build().adapter<AbridgedFoodItemSchema>().indent("    ").toJson(item))
    val nutrients = item.foodNutrients!!
    for (n in nutrients) {
        prettyPrint(n)
    }
}

@OptIn(ExperimentalStdlibApi::class)
private fun prettyPrint(item: SearchResultFoodSchema) {
    println(Moshi.Builder().build().adapter<SearchResultFoodSchema>().indent("    ").toJson(item))
}

@OptIn(ExperimentalStdlibApi::class)
private fun prettyPrint(item: BrandedFoodItemSchema) {
    println(Moshi.Builder().build().adapter<BrandedFoodItemSchema>().indent("    ").toJson(item))
}

internal class FoodDataCentralRemoteApiUnitTest {
    private lateinit var fdcApi: FoodDataCentralApi

    @Before
    fun setUp() {
        fdcApi = FoodDataCentral.foodDataCentralApi()
    }

    @Test
    fun `Post food search `() {
        val body = fdcApi.postFoodsSearch(
            TestApiKey,
            FoodSearchCriteriaSchema(
                query = "Cheddar Cheese",
                dataType = listOf(
//                    DataType.Branded,
                    Foundation,
                    SRLegacy,
//                    DataType.SURVEY_FNDDS,
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
        println("$body")
    }

    @Test
    fun `Sprite test from FoodsList`() {
        val result = fdcApi.postFoodsSearch(
            apiKey = TestApiKey,
            FoodSearchCriteriaSchema(
                query = searchQuery { withUpc(SpriteTest.spriteUpc) },
                dataType = listOf(SpriteTest.spriteTypeSchema),
            ),
        ).execute().assertSuccessful().body()!!

        for (f in result.foods!!) {
            prettyPrint(f)
        }
    }

    @Test
    fun `Sprite test from Food`() {
        val result = fdcApi.getFoodWithFdcIdBranded(
            apiKey = TestApiKey,
            fdcId = SpriteTest.spriteFdcIdString,
        ).execute().assertSuccessful().body()!!

        prettyPrint(result)
    }

    @Test
    fun `Bad Sprite test from food`() {
        val result = fdcApi.getFoodWithFdcIdBranded(
            apiKey = TestApiKey,
            fdcId = BadSpriteTest.spriteFdcIdString
        ).execute().assertSuccessful().body()!!

        prettyPrint(result)
    }
}
