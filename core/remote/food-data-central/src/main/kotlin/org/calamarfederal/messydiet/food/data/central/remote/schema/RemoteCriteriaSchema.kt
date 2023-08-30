package org.calamarfederal.messydiet.food.data.central.remote.schema

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.calamarfederal.messydiet.food.data.central.remote.schema.FoodSearchCriteriaSchema.Companion
import org.calamarfederal.messydiet.food.data.central.remote.schema.FoodsCriteriaSchema.FormatType.Full

@JsonClass(generateAdapter = false)
internal enum class SortOrderSchema {
    @Json(name = "asc")
    Ascending,

    @Json(name = "desc")
    Descending,
    ;
}

@JsonClass(generateAdapter = false)
internal enum class SortBySchema {
    /**
     * Will replace [DataTypeKeyword] in future release
     */
    @Json(name = "dataType")
    DataType,

    @Json(name = "dataType.keyword")
    DataTypeKeyword,

    /**
     * Will replace [LowercaseDescriptionKeyword] in future release
     */
    @Json(name = "description")
    Description,

    @Json(name = "lowercaseDescription.keyword")
    LowercaseDescriptionKeyword,


    @Json(name = "fdcId")
    FdcId,

    @Json(name = "publishedDate")
    PublishedDate,
    ;
}

@JsonClass(generateAdapter = false)
internal enum class DataTypeSchema {
    Branded,
    Foundation,

    @Json(name = "Survey (FNDDS)")
    SurveyFNDDS,

    @Json(name = "SR Legacy")
    SRLegacy,
    ;
}


@JsonClass(generateAdapter = true)
internal data class FoodListCriteriaSchema(
    val dataType: List<DataTypeSchema>? = null,
    // Optional. Maximum number of results to return for the current page. Default is 50.
    val pageSize: Int? = null,
    // Optional. Page number to retrieve. The offset into the overall result set is expressed as (pageNumber * pageSize)
    val pageNumber: Int? = null,
    val sortBy: SortBySchema? = null,
    val sortOrder: SortOrderSchema? = null,
)

@JsonClass(generateAdapter = true)
internal data class FoodsCriteriaSchema(
    val fdcIds: List<Int>,
    val format: FormatType = Full,
    val nutrients: List<Int>? = null,
) {
    enum class FormatType {
        @Json(name = "abridged")
        Abridged,

        @Json(name = "full")
        Full
    }
}

@JsonClass(generateAdapter = true)
internal data class FoodSearchCriteriaSchema(
    // Search terms to use in the search. The string may also include standard [search operators](https://fdc.nal.usda.gov/help.html#bkmk-2)
    val query: String,
    val dataType: List<DataTypeSchema>? = null,
    // Optional. Maximum number of results to return for the current page. Default is 50.
    val pageSize: Int? = null,
    val pageNumber: Int? = null,
    val sortBy: SortBySchema? = null,
    val sortOrder: SortOrderSchema? = null,
    // Optional. Filter results based on the brand owner of the food. Only applies to Branded Foods.
    val brandOwner: String? = null,
    // Optional. Filter foods containing any of the specified trade channels.
    val tradeChannel: List<TradeChannel>? = null,
    // Filter foods published on or after this date. Format: YYYY-MM-DD
    val startDate: String? = null,
    // Filter foods published on or before this date. Format: YYYY-MM-DD
    val endDate: String? = null
) {
    enum class TradeChannel {
        @Json(name = "CHILD_NUTRITION_FOOD_PROGRAMS")
        ChildNutritionFoodPrograms,

        @Json(name = "DRUG")
        Drug,

        @Json(name = "FOOD_SERVICE")
        FoodService,

        @Json(name = "GROCERY")
        Grocery,

        @Json(name = "MASS_MERCHANDISING")
        MassMerchandising,

        @Json(name = "MILITARY")
        Military,

        @Json(name = "ONLINE")
        Online,

        @Json(name = "VENDING")
        Vending,
        ;
    }

    companion object
}

internal fun Companion.dateOf(year: Int, month: Int, day: Int): String {
    require(year in 1000..9999)
    require(month in 1..12)
    require(day in 1..31)


    return "$year-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}"
}
