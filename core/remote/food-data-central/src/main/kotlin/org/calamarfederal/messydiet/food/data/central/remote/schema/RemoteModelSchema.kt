package org.calamarfederal.messydiet.food.data.central.remote.schema

import com.squareup.moshi.JsonClass
import org.calamarfederal.messydiet.food.data.central.model.FDCDate

/**
 * Parse [dateString] assuming M/D/YYYY formatting
 *
 * to be used with any Response spec
 */
internal fun parseResponseDate(
    dateString: String,
): FDCDate {
    val (m, d, y) = dateString.split('/')
    return FDCDate(year = y.toInt(), month = m.toInt(), day = d.toInt())
}

@JsonClass(generateAdapter = true)
internal data class AbridgedFoodItemSchema(
    val dataType: String,
    val description: String,
    val fdcId: Int,
    val foodNutrients: List<AbridgedFoodNutrientSchema>? = null,
    val publicationDate: String? = null,
    // only applies to Branded Foods
    val brandOwner: String? = null,
    // only applies to Branded Foods
    val gtinUpc: String? = null,
    // only applies to Foundation and SRLegacy Foods
    val ndbNumber: Int? = null,
    // only applies to Survey Foods
    val foodCode: String? = null,
) {
    fun parseDate(): FDCDate? {
        if (publicationDate == null)
            return null

        val (y, m, d) = publicationDate.split('-')
        return FDCDate(year = y.toInt(), month = m.toInt(), day = d.toInt())
    }
}

@JsonClass(generateAdapter = true)
internal data class AbridgedFoodNutrientSchema(
    val number: String? = null,
    val name: String? = null,
    val amount: Double? = null,
    val unitName: String? = null,
    val derivationCode: String? = null,
    val derivationDescription: String? = null,
)

@JsonClass(generateAdapter = true)
internal data class BrandedFoodItemLabelFloatBoxSchema(
    val number: Double? = null
)

@JsonClass(generateAdapter = true)
internal data class BrandedFoodItemLabelNutrientsSchema(
    val fat: BrandedFoodItemLabelFloatBoxSchema? = null,
    val saturatedFat: BrandedFoodItemLabelFloatBoxSchema? = null,
    val transFat: BrandedFoodItemLabelFloatBoxSchema? = null,
    val cholesterol: BrandedFoodItemLabelFloatBoxSchema? = null,
    val sodium: BrandedFoodItemLabelFloatBoxSchema? = null,
    val carbohydrates: BrandedFoodItemLabelFloatBoxSchema? = null,
    val fiber: BrandedFoodItemLabelFloatBoxSchema? = null,
    val sugars: BrandedFoodItemLabelFloatBoxSchema? = null,
    val protein: BrandedFoodItemLabelFloatBoxSchema? = null,
    val calcium: BrandedFoodItemLabelFloatBoxSchema? = null,
    val iron: BrandedFoodItemLabelFloatBoxSchema? = null,
    val potassium: BrandedFoodItemLabelFloatBoxSchema? = null,
    val calories: BrandedFoodItemLabelFloatBoxSchema? = null,
)

@JsonClass(generateAdapter = true)
internal data class BrandedFoodItemSchema(
    val fdcId: Int,
    /**
     * example: 8/18/2018 (assuming format is M/D/YYYY)
     */
    val availableDate: String? = null,
    val brandOwner: String? = null,
    val dataSource: String? = null,
    val dataType: String,
    val description: String,
    val foodClass: String? = null,
    val gtinUpc: String? = null,
    val householdServingFullText: String? = null,
    val ingredients: String? = null,
    val modifiedDate: String? = null,
    val publicationDate: String? = null,
    val servingSize: Double? = null,
    val servingSizeUnit: String? = null,
    val preparationStateCode: String? = null,
    val brandedFoodCategory: String? = null,
    val tradeChannel: List<String>? = null,
    val gpcClassCode: Int? = null,
    val foodNutrients: List<FoodNutrientSchema>? = null,
    val foodUpdateLog: List<FoodUpdateLogSchema>? = null,
    val labelNutrients: BrandedFoodItemLabelNutrientsSchema? = null,
) {
    companion object
}

@JsonClass(generateAdapter = true)
internal data class FoodAttributeFoodAttributeTypeSchema(
    val id: Int? = null,
    val name: String? = null,
    val description: String? = null,
)

@JsonClass(generateAdapter = true)
internal data class FoodAttributeSchema(
    val id: Int? = null,
    val sequenceNumber: Int? = null,
    val value: String? = null,
    val foodAttributeType: FoodAttributeFoodAttributeTypeSchema? = null,
)

@JsonClass(generateAdapter = true)
internal data class FoodCategorySchema(
    val id: Int? = null,
    val code: String? = null,
    val description: String? = null,
)

@JsonClass(generateAdapter = true)
internal data class FoodComponentSchema(
    val id: Int? = null,
    val name: String? = null,
    val dataPoints: Int? = null,
    val gramWeight: Double? = null,
    val isRefuse: Boolean? = null,
    val minYearAcquired: Int? = null,
    val percentWeight: Double? = null,
)

@JsonClass(generateAdapter = true)
internal data class FoodNutrientSchema(
    val id: Int,
    //val id: String,
    val amount: Double? = null,
    val dataPoints: Int? = null,
    val min: Double? = null,
    val max: Double? = null,
    val median: Double? = null,
    val type: String? = null,
    val nutrient: NutrientSchema? = null,
    val foodNutrientDerivation: FoodNutrientDerivationSchema? = null,
    val nutrientAnalysisDetails: NutrientAnalysisDetailsSchema? = null
)

@JsonClass(generateAdapter = true)
internal data class FoodNutrientDerivationSchema(
    val id: Int? = null,
    val code: String? = null,
    val description: String? = null,
    val foodNutrientSource: FoodNutrientSourceSchema? = null
)

@JsonClass(generateAdapter = true)
internal data class FoodNutrientSourceSchema(
    val id: Int? = null,
    val code: String? = null,
    val description: String? = null
)

@JsonClass(generateAdapter = true)
internal data class FoodPortionSchema(
    val id: Int? = null,
    val amount: Double? = null,
    val dataPoints: Int? = null,
    val gramWeight: Double? = null,
    val minYearAcquired: Int? = null,
    val modifier: String? = null,
    val portionDescription: String? = null,
    val sequenceNumber: Int? = null,
    val measureUnit: MeasureUnitSchema? = null
)

@JsonClass(generateAdapter = true)
internal data class FoodUpdateLogSchema(
    val fdcId: Int? = null,
    val availableDate: String? = null,
    val brandOwner: String? = null,
    val dataSource: String? = null,
    val dataType: String? = null,
    val description: String? = null,
    val foodClass: String? = null,
    val gtinUpc: String? = null,
    val householdServingFullText: String? = null,
    val ingredients: String? = null,
    val modifiedDate: String? = null,
    val publicationDate: String? = null,
    val servingSize: Double? = null,
    val servingSizeUnit: String? = null,
    val brandedFoodCategory: String? = null,
    val changes: String? = null,
    val foodAttributes: List<FoodAttributeSchema>? = null,
)

@JsonClass(generateAdapter = true)
internal data class FoundationFoodItemSchema(
    val fdcId: Int,
    val dataType: String,
    val description: String,
    val foodClass: String? = null,
    val footNote: String? = null,
    val isHistoricalReference: Boolean? = null,
    val ndbNumber: Int? = null,
    val publicationDate: String? = null,
    val scientificName: String? = null,
    val foodCategory: FoodCategorySchema? = null,
    val foodComponents: List<FoodComponentSchema>? = null,
    val foodNutrients: List<FoodNutrientSchema>? = null,
    val foodPortions: List<FoodPortionSchema>? = null,
    val inputFoods: List<InputFoodFoundationSchema>? = null,
    val nutrientConversionFactors: List<NutrientConversionFactorsSchema>? = null
)

@JsonClass(generateAdapter = true)
internal data class InputFoodFoundationSchema(
    val id: Int? = null,
    val foodDescription: String? = null,
    val inputFood: SampleFoodItemSchema? = null
)

@JsonClass(generateAdapter = true)
internal data class InputFoodSurveySchema(
    val id: Int? = null,
    val amount: Double? = null,
    val foodDescription: String? = null,
    val ingredientCode: Int? = null,
    val ingredientDescription: String? = null,
    val ingredientWeight: Double? = null,
    val portionCode: String? = null,
    val portionDescription: String? = null,
    val sequenceNumber: Int? = null,
    val surveyFlag: Int? = null,
    val unit: String? = null,
    val inputFood: SurveyFoodItemSchema? = null,
    val retentionFactor: RetentionFactorSchema? = null
)

@JsonClass(generateAdapter = true)
internal data class MeasureUnitSchema(
    val id: Int? = null,
    val abbreviation: String? = null,
    val name: String? = null
)

@JsonClass(generateAdapter = true)
internal data class NutrientSchema(
    val id: Int? = null,
    val number: String? = null,
    val name: String? = null,
    val rank: Int? = null,
    val unitName: String? = null
)

@JsonClass(generateAdapter = true)
internal data class NutrientAcquisitionDetailsSchema(
    val sampleUnitId: Int? = null,
    val purchaseDate: String? = null,
    val storeCity: String? = null,
    val storeState: String? = null
)

@JsonClass(generateAdapter = true)
internal data class NutrientAnalysisDetailsSchema(
    val subSampleId: Int? = null,
    val amount: Double? = null,
    val nutrientId: Int? = null,
    val labMethodDescription: String? = null,
    val labMethodOriginalDescription: String? = null,
    val labMethodLink: String? = null,
    val labMethodTechnique: String? = null,
    val nutrientAcquisitionDetails: List<NutrientAcquisitionDetailsSchema>? = null
)

@JsonClass(generateAdapter = true)
internal data class NutrientConversionFactorsSchema(
    val type: String? = null,
    val value: Double? = null
)

@JsonClass(generateAdapter = true)
internal data class RetentionFactorSchema(
    val id: Int? = null,
    val code: Int? = null,
    val description: String? = null
)

@JsonClass(generateAdapter = true)
internal data class SampleFoodItemSchema(
    val fdcId: Int,
    val datatype: String? = null,
    val description: String,
    val foodClass: String? = null,
    val publicationDate: String? = null,
    val foodAttributes: List<FoodCategorySchema>? = null
)

@JsonClass(generateAdapter = true)
internal data class SearchResultSchema(
    val foodSearchCriteria: FoodSearchCriteriaSchema? = null,
    // The total number of foods found matching the search criteria.
    val totalHits: Int? = null,
    // The current page of results being returned.
    val currentPage: Int? = null,
    // The total number of pages found matching the search criteria.
    val totalPages: Int? = null,
    // The list of foods found matching the search criteria. See Food Fields below.
    val foods: List<SearchResultFoodSchema>? = null
)

@JsonClass(generateAdapter = true)
internal data class SearchResultFoodSchema(
    val fdcId: Int,
    val dataType: String? = null,
    val description: String,
    // Any A unique ID identifying the food within FNDDS.
    val foodCode: String? = null,
    val foodNutrients: List<AbridgedFoodNutrientSchema>? = null,
    val publicationDate: String? = null,
    // The scientific name of the food.
    val scientificName: String? = null,
    // Brand owner for the food. Only applies to Branded Foods.
    val brandOwner: String? = null,
    // GTIN or UPC code identifying the food. Only applies to Branded Foods.
    val gtinUpc: String? = null,
    // The list of ingredients (as it appears on the product label). Only applies to Branded Foods.
    val ingredients: String? = null,
    // Unique number assigned for foundation foods. Only applies to Foundation and SRLegacy Foods.
    val ndbNumber: Int? = null,
    val additionalDescriptions: String? = null,
    val allHighlightFields: String? = null,
    // Relative score indicating how well the food matches the search criteria.
    val score: Double? = null
)

@JsonClass(generateAdapter = true)
internal data class SRLegacyFoodItemSchema(
    val fdcId: Int,
    val dataType: String,
    val description: String,
    val foodClass: String? = null,
    val isHistoricalReference: Boolean? = null,
    val ndbNumber: Int? = null,
    val publicationDate: String? = null,
    val scientificName: String? = null,
    val foodCategory: FoodCategorySchema? = null,
    val foodNutrients: List<FoodNutrientSchema>? = null,
    val nutrientConversionFactors: List<NutrientConversionFactorsSchema>? = null
)

@JsonClass(generateAdapter = true)
internal data class SurveyFoodItemSchema(
    val fdcId: Int,
    val datatype: String? = null,
    val description: String,
    val endDate: String? = null,
    val foodClass: String? = null,
    val foodCode: String? = null,
    val publicationDate: String? = null,
    val startDate: String? = null,
    val foodAttributes: List<FoodAttributeSchema>? = null,
    val foodPortions: List<FoodPortionSchema>? = null,
    val inputFoods: List<InputFoodSurveySchema>? = null,
    val wweiaFoodCategory: WweiaFoodCategorySchema? = null
)

@JsonClass(generateAdapter = true)
internal data class WweiaFoodCategorySchema(
    val wweiaFoodCategoryCode: Int? = null,
    val wweiaFoodCategoryDescription: String? = null
)
