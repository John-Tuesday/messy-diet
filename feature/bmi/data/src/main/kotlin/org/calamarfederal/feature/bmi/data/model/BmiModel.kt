package org.calamarfederal.feature.bmi.data.model

data class Bmi internal constructor(
    val value: Double,
    val category: BmiCategory,
) {
    constructor() : this(0.00, fromBmiIndex(0.00).category)

    companion object {
        internal fun fromBmiIndex(bmiIndex: Double): Bmi = Bmi(
            value = bmiIndex,
            category = BmiCategory.fromBmiIndex(bmiIndex),
        )
    }
}

enum class BmiCategory(start: Double, endExclusive: Double) {
    VerySeverelyUnderweight(Double.NEGATIVE_INFINITY, 15.00),
    SeverelyUnderweight(15.00, 16.00),
    Underweight(16.00, 18.50),
    Normal(18.50, 25.00),
    Overweight(25.00, 30.00),
    ModeratelyObese(30.00, 35.00),
    SeverelyObese(35.00, 40.00),
    VerySeverelyObese(40.00, Double.POSITIVE_INFINITY),
    ;

    internal val openEndRange = start..<endExclusive

    companion object {
        internal fun fromBmiIndex(index: Double): BmiCategory = BmiCategory.entries.single { index in it.openEndRange }
    }
}
