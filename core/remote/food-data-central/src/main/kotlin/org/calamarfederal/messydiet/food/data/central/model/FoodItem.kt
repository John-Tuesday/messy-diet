package org.calamarfederal.messydiet.food.data.central.model

import org.calamarfederal.messydiet.diet_data.model.Nutrition
import org.calamarfederal.messydiet.food.data.central.remote.schema.DataTypeSchema

typealias FDCNutritionInfo = Nutrition


data class FDCDate(
    val year: Int,
    val month: Int,
    val day: Int,
)

sealed interface FDCId {
    val fdcId: Int
}

fun foodDataCentralId(
    id: Int,
    dataType: FDCDataType,
): FDCId = when (dataType) {
    FDCDataType.Foundation -> FoundationFdcId(id)
    FDCDataType.Survey -> SurveyFdcId(id)
    FDCDataType.Branded -> BrandedFDCId(id)
    FDCDataType.Legacy -> LegacyFdcId(id)
}

sealed interface FDCFoodItem {
    val fdcId: FDCId
    val description: String
    val nutritionalInfo: FDCNutritionInfo?
}

enum class FDCDataType {
    Foundation,
    Survey,
    Branded,
    Legacy,
    ;

    companion object {
        internal fun fromString(text: String): FDCDataType? {
            return when (text.lowercase().trim()) {
                "foundation" -> FDCDataType.Foundation
                "branded" -> FDCDataType.Branded
                "survey (fndds)" -> FDCDataType.Survey
                "sr legacy" -> FDCDataType.Legacy
                else -> null
            }
        }
    }
}


internal val FDCDataType.schema: DataTypeSchema
    get() = when (this) {
        FDCDataType.Foundation -> DataTypeSchema.Foundation
        FDCDataType.Survey -> DataTypeSchema.SurveyFNDDS
        FDCDataType.Branded -> DataTypeSchema.Branded
        FDCDataType.Legacy -> DataTypeSchema.SRLegacy
    }

val FDCId.dataType: FDCDataType
    get() = when (this) {
        is BrandedFDCId -> FDCDataType.Branded
        is SurveyFdcId -> FDCDataType.Survey
        is LegacyFdcId -> FDCDataType.Legacy
        is FoundationFdcId -> FDCDataType.Foundation
    }
