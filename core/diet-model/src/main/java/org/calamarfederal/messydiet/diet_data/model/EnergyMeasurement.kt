package org.calamarfederal.messydiet.diet_data.model

data class FoodEnergy internal constructor(
    internal val kcals: Double = 0.00,
)

enum class FoodEnergyUnit {
    Kilocalories,
}

operator fun FoodEnergy.plus(other: FoodEnergy): FoodEnergy = FoodEnergy(kcals + other.kcals)

private fun foodEnergyInUnitsOf(foodEnergy: FoodEnergy, foodEnergyUnit: FoodEnergyUnit): Double =
    when (foodEnergyUnit) {
        FoodEnergyUnit.Kilocalories -> foodEnergy.inKilocalories()
    }

private fun numberToFoodEnergy(number: Number, foodEnergyUnit: FoodEnergyUnit): FoodEnergy = when (foodEnergyUnit) {
    FoodEnergyUnit.Kilocalories -> number.kcal
}

fun FoodEnergy.inUnits(foodEnergyUnit: FoodEnergyUnit): Double = foodEnergyInUnitsOf(this, foodEnergyUnit)

fun FoodEnergyUnit.energyOf(number: Number): FoodEnergy = numberToFoodEnergy(number, this)

fun Number.energyIn(foodEnergyUnit: FoodEnergyUnit): FoodEnergy = numberToFoodEnergy(this, foodEnergyUnit)

fun FoodEnergy.inKilocalories(): Double = kcals

val Number.kcal: FoodEnergy get() = FoodEnergy(toDouble())
