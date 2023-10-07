package org.calamarfederal.messydiet.feature.meal.data.local

import androidx.room.TypeConverter
import io.github.john.tuesday.nutrition.MassPortion
import io.github.john.tuesday.nutrition.Portion
import io.github.john.tuesday.nutrition.VolumePortion
import org.calamarfederal.physical.measurement.*
import kotlin.math.absoluteValue

internal class MeasureConverters {
    @TypeConverter
    fun fromWeightToDoubleOrNull(weight: Mass?): Double? = weight?.inGrams()

    @TypeConverter
    fun fromDoubleToWeightOrNull(grams: Double?): Mass? = grams?.grams

    @TypeConverter
    fun fromFoodEnergyToDoubleOrNull(energy: Energy?): Double? = energy?.inKilocalories()

    @TypeConverter
    fun fromDoubleToFoodEnergyOrNull(kcals: Double?): Energy? = kcals?.kilocalories

    @TypeConverter
    fun fromFoodEnergyToDouble(energy: Energy): Double = energy.inKilocalories()

    @TypeConverter
    fun fromDoubleToFoodEnergy(kcals: Double): Energy = kcals.kilocalories

    @TypeConverter
    fun fromWeightToLong(weight: Mass): Double = weight.inGrams()

    @TypeConverter
    fun fromDoubleToWeight(grams: Double): Mass = grams.grams
}

internal class NutritionConverters {

    @TypeConverter
    fun fromPortionToPair(portion: Portion): Double = when (portion) {
        is MassPortion -> portion.mass.inGrams()
        is VolumePortion -> -portion.volume.inLiters()
    }

    @TypeConverter
    fun fromPairToPortion(amount: Double): Portion =
        if (amount >= 0)
            Portion(amount.absoluteValue.grams)
        else
            Portion(amount.absoluteValue.liters)
}
