package org.calamarfederal.messydiet.food.data.central

import org.calamarfederal.messydiet.food.data.central.SearchCriteriaBuilder.SearchSortType
import org.calamarfederal.messydiet.food.data.central.model.*
import org.calamarfederal.messydiet.food.data.central.remote.schema.*
import org.calamarfederal.messydiet.food.data.central.remote.schema.SortOrderSchema.Ascending
import org.calamarfederal.messydiet.food.data.central.remote.schema.SortOrderSchema.Descending

internal fun SearchResultSchema.toFDCSearchResult(): FDCSearchResult = FDCSearchResult(
    totalHits = totalHits ?: 0,
    currentPage = currentPage ?: 0,
    totalPages = totalPages ?: 0,
    foodItems = foods?.mapNotNull { it.toFdcFoodItemOrNull() } ?: listOf(),
)

internal fun SearchResultFoodSchema.toFdcFoodItemOrNull(): FDCFoodItem? {
    return when (dataType?.let { FDCDataType.fromString(it) }) {
        FDCDataType.Branded -> toFDCBrandedOrNull()
        FDCDataType.Foundation -> toFDCFoundationFoodOrNull()
        FDCDataType.Legacy -> toFdcAbridgedFoodItemOrNull()
        FDCDataType.Survey -> toFdcAbridgedFoodItemOrNull()
        else -> toFdcAbridgedFoodItemOrNull()
    }
}

internal val SearchCriteriaBuilder.SearchSortType.schema: SortBySchema
    get() = when (this) {
        SearchCriteriaBuilder.SearchSortType.DataType -> SortBySchema.DataType
        SearchCriteriaBuilder.SearchSortType.Description -> SortBySchema.LowercaseDescriptionKeyword
        SearchCriteriaBuilder.SearchSortType.FdcId -> SortBySchema.FdcId
        SearchCriteriaBuilder.SearchSortType.PublishedDate -> SortBySchema.PublishedDate
    }

data class FDCSearchResult(
    val totalHits: Int,
    val currentPage: Int,
    val totalPages: Int,
    val foodItems: List<FDCFoodItem>,
)

data class SearchCriteriaBuilder internal constructor(
    var query: String,
    var dataType: List<FDCDataType>? = null,
    var brandOwner: String? = null,
    var pageSize: Int? = null,
    var pageNumber: Int? = null,
    var searchSortType: SearchSortType? = null,
    var sortAscending: Boolean? = null,
    internal var startDate: String? = null,
    internal var endDate: String? = null,
) {
    enum class SearchSortType {
        DataType,
        Description,
        FdcId,
        PublishedDate,
        ;
    }

    internal fun setStartDate(year: Int, month: Int, day: Int) {
        startDate = FoodSearchCriteriaSchema.dateOf(year = year, month = month, day = day)
    }

    internal fun setEndDate(year: Int, month: Int, day: Int) {
        endDate = FoodSearchCriteriaSchema.dateOf(year = year, month = month, day = day)
    }
}

internal fun SearchCriteriaBuilder.build(): FoodSearchCriteriaSchema = FoodSearchCriteriaSchema(
    query = query,
    dataType = dataType?.map { it.schema }?.ifEmpty { null },
    pageSize = pageSize,
    pageNumber = pageNumber,
    sortBy = searchSortType?.schema,
    sortOrder = when (sortAscending) {
        null -> null
        true -> Ascending
        false -> Descending
    },
    brandOwner = brandOwner,
    tradeChannel = null,
    startDate = startDate,
    endDate = endDate,
)

object SearchQueryBuilder {
    internal fun exactly(text: String): String {
        return "\"$text\""
    }

    internal fun require(text: String): String {
        return "+$text"
    }

    internal fun exclude(text: String): String {
        return "-$text"
    }

    internal fun element(elementName: String, value: String): String = "$elementName:$value"
    internal fun withUpc(upc: String): String = element("gtinUpc", exactly(upc))

    internal infix fun String.and(other: String): String = "$this $other"
}

fun searchQuery(block: SearchQueryBuilder.() -> String): String {
    return SearchQueryBuilder.run { block() }
}

internal fun FoodSearchCriteriaSchema.Companion.formatToString(date: FDCDate): String = FoodSearchCriteriaSchema.dateOf(
    year = date.year,
    month = date.month,
    day = date.day
)

fun searchFoodDataCenter(
    pageSize: Int? = null,
    pageNumber: Int? = null,
    searchSortType: SearchSortType? = null,
    sortAscending: Boolean? = null,
    startDate: FDCDate? = null,
    endDate: FDCDate? = null,
    brandOwner: String? = null,
    query: SearchQueryBuilder.() -> String,
) = SearchCriteriaBuilder(
    query = SearchQueryBuilder.run { query() },
    pageSize = pageSize,
    pageNumber = pageNumber,
    searchSortType = searchSortType,
    sortAscending = sortAscending,
    startDate = startDate?.let { FoodSearchCriteriaSchema.formatToString(it) },
    endDate = endDate?.let { FoodSearchCriteriaSchema.formatToString(it) },
    brandOwner = brandOwner,
)
