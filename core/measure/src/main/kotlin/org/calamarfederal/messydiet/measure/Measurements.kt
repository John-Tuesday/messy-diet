/******************************************************************************
 * Copyright (c) 2023 John Tuesday Picot                                      *
 *                                                                            *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell  *
 * copies of the Software, and to permit persons to whom the Software is      *
 * furnished to do so, subject to the following conditions:                   *
 *                                                                            *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.                            *
 *                                                                            *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR *
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,   *
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE*
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER     *
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.                                                                  *
 ******************************************************************************/

package org.calamarfederal.messydiet.measure

import kotlin.math.absoluteValue

/**
 * Weight (technically mass)
 *
 * internally stored in grams as a [Double]
 */
data class Weight internal constructor(
    internal val grams: Double = 0.00
) : Comparable<Weight> {

    override operator fun compareTo(other: Weight): Int = grams.compareTo(other.grams)
    fun inMilligrams(): Double = grams * 1000.00
    fun inGrams(): Double = grams
    fun inKilograms(): Double = grams / 1000.00

    fun inOunces(): Double = gramsToOz(grams)
    fun inPounds(): Double = gramsToLbs(grams)

}

val Weight.absoluteValue get() = Weight(grams.absoluteValue)

operator fun Weight.plus(other: Weight): Weight = Weight(grams + other.grams)
operator fun Weight.minus(other: Weight): Weight = Weight(grams - other.grams)
operator fun Weight.times(ratio: Number): Weight = Weight(grams * ratio.toDouble())
operator fun Weight.div(factor: Number): Weight = Weight(grams / factor.toDouble())
operator fun Weight.div(other: Weight): Double = grams / other.grams

enum class WeightUnit {
    Milligram,
    Gram,
    Kilogram,
    Ounce,
    Pound,
}

internal fun toWeightWithUnit(num: Number, weightUnit: WeightUnit): Weight = when (weightUnit) {
    WeightUnit.Milligram -> num.milligrams
    WeightUnit.Gram -> num.grams
    WeightUnit.Kilogram -> num.kilograms
    WeightUnit.Ounce -> num.oz
    WeightUnit.Pound -> num.lbs
}

internal fun toNumberAsWeightUnit(weight: Weight, weightUnit: WeightUnit): Double = when(weightUnit) {
    WeightUnit.Milligram -> weight.inMilligrams()
    WeightUnit.Gram -> weight.inGrams()
    WeightUnit.Kilogram -> weight.inKilograms()
    WeightUnit.Ounce -> weight.inOunces()
    WeightUnit.Pound -> weight.inPounds()
}

fun weightOf(number: Number, unit: WeightUnit): Weight = toWeightWithUnit(num = number, weightUnit = unit)
fun weightOf(): Weight = 0.grams

fun Weight.inUnits(unit: WeightUnit): Double = toNumberAsWeightUnit(weight = this, weightUnit = unit)
fun WeightUnit.weightOf(num: Number): Weight = toWeightWithUnit(num = num, weightUnit = this)

fun Number.weightIn(unit: WeightUnit): Weight = toWeightWithUnit(num = this, weightUnit = unit)

private const val LBS_X_GRAMS = 453.59237//453.592
private fun lbsToGrams(lbs: Double) = lbs * LBS_X_GRAMS
private fun gramsToLbs(grams: Double) = grams / LBS_X_GRAMS
private const val OZ_X_GRAMS = 28.349523125
private fun ozToGrams(oz: Double) = oz * OZ_X_GRAMS
private fun gramsToOz(grams: Double) = grams / OZ_X_GRAMS

val Number.grams: Weight get() = Weight(toDouble())
val Number.milligrams: Weight get() = Weight(toDouble() / 1_000.00)
val Number.kilograms: Weight get() = Weight(toDouble() * 1_000.00)
val Number.lbs: Weight get() = Weight(lbsToGrams(toDouble()))
val Number.oz: Weight get() = Weight(ozToGrams(toDouble()))

/**
 * Volume
 *
 * internally stored as liters as a [Double]
 */
data class Volume internal constructor(
    internal val liters: Double = 0.00
): Comparable<Volume> {

    override fun compareTo(other: Volume): Int = liters.compareTo(other.liters)
    fun inLiters() = liters
    fun inMilliliters() = liters * 1_000.00

    fun inUsGallons() = litersToUsGallons(liters)
    fun inUsFlOz() = litersToFlOz(liters)
    fun inUsTablespoons() = litersToUsTablespoons(liters)
    fun inUsTeaspoons() = litersToUsTeaspoons(liters)
}

operator fun Volume.plus(other: Volume): Volume = Volume(liters + other.liters)
operator fun Volume.minus(other: Volume): Volume = Volume(liters - other.liters)
operator fun Volume.times(number: Number): Volume = Volume(liters * number.toDouble())
operator fun Volume.div(number: Number): Volume = Volume(liters / number.toDouble())
operator fun Volume.div(other: Volume): Double = liters / other.liters

private const val US_GAL_X_LITER = 3.785_411_784
private fun litersToUsGallons(liter: Double) = liter / US_GAL_X_LITER
private fun usGallonsToLiters(gallons: Double) = gallons * US_GAL_X_LITER
private const val US_FL_OZ_X_LITER = 0.029_573_529_562_5
private fun usFlOzToLiters(flOz: Double) = flOz * US_FL_OZ_X_LITER
private fun litersToFlOz(liter: Double) = liter / US_FL_OZ_X_LITER
private const val US_TBSP_X_LITER = US_FL_OZ_X_LITER / 2.00//0.0147868
private fun usTablespoonsToLiters(tbsp: Double) = tbsp * US_TBSP_X_LITER
private fun litersToUsTablespoons(liter: Double) = liter / US_TBSP_X_LITER
private const val US_TSP_X_LITER = 0.004_928_921_593_75//0.00492892
private fun usTeaspoonsToLiters(tsp: Double) = tsp * US_TSP_X_LITER
private fun litersToUsTeaspoons(liter: Double) = liter / US_TSP_X_LITER


val Number.liters: Volume get() = Volume(toDouble())
val Number.milliliters: Volume get() = Volume(toDouble() / 1_000.00)
val Number.usGallons: Volume get() = Volume(usGallonsToLiters(toDouble()))
val Number.usFlOz: Volume get() = Volume(usFlOzToLiters(toDouble()))
val Number.usTablespoons: Volume get() = Volume(usTablespoonsToLiters(toDouble()))
val Number.usTeaspoons: Volume get() = Volume(usTeaspoonsToLiters(toDouble()))

/**
 * Length
 *
 * internally stored as meters [Double]
 */
data class Length internal constructor(
    internal val meter: Double = 0.00,
): Comparable<Length> {
    override fun compareTo(other: Length): Int = meter.compareTo(other.meter)

    fun inMeters() = meter
    fun inKilometers() = meter / 1_000
    fun inCentimeters() = meter * 100
    fun inMillimeters() = meter * 1_000

    fun inMiles() = metersToMiles(meter)
    fun inFeet() = metersToFeet(meter)
    fun inInches() = metersToInches(meter)
}

operator fun Length.plus(other: Length): Length = Length(meter + other.meter)
operator fun Length.minus(other: Length): Length = Length(meter - other.meter)

enum class LengthUnit {
    Millimeter,
    Centimeter,
    Meter,
    Kilometer,
    Mile,
    Feet,
    Inch,
    ;
}

private fun toLengthWithUnit(num: Number, lengthUnit: LengthUnit) = when(lengthUnit) {
    LengthUnit.Millimeter -> num.millimeters
    LengthUnit.Centimeter -> num.centimeters
    LengthUnit.Meter -> num.meters
    LengthUnit.Kilometer -> num.kilometers
    LengthUnit.Mile -> num.miles
    LengthUnit.Feet -> num.feet
    LengthUnit.Inch -> num.inches
}

private fun toNumberAsLengthUnit(length: Length, lengthUnit: LengthUnit): Double = when(lengthUnit) {
    LengthUnit.Millimeter -> length.inMillimeters()
    LengthUnit.Centimeter -> length.inCentimeters()
    LengthUnit.Meter -> length.inMeters()
    LengthUnit.Kilometer -> length.inKilometers()
    LengthUnit.Mile -> length.inMiles()
    LengthUnit.Feet -> length.inFeet()
    LengthUnit.Inch -> length.inInches()
}

fun Length.inUnits(lengthUnit: LengthUnit): Double = toNumberAsLengthUnit(length = this, lengthUnit = lengthUnit)

fun lengthOf(number: Number, lengthUnit: LengthUnit): Length = toLengthWithUnit(num = number, lengthUnit = lengthUnit)
fun lengthOf(): Length = Length()
fun LengthUnit.lengthOf(num: Number) = toLengthWithUnit(num = num, lengthUnit = this)

private const val METER_X_MILE = 1_609.344
private fun milesToMeters(miles: Double) = miles * METER_X_MILE
private fun metersToMiles(meters: Double) = meters / METER_X_MILE
private const val METER_X_FOOT = 0.304_8
private fun feetToMeters(feet: Double) = feet * METER_X_FOOT
private fun metersToFeet(meter: Double) = meter / METER_X_FOOT
private const val METER_X_INCH = 0.025_4
private fun inchesToMeters(inch: Double) = inch * METER_X_INCH
private fun metersToInches(meter: Double) = meter / METER_X_INCH

val Number.meters: Length get() = Length(toDouble())
val Number.centimeters: Length get() = Length(toDouble() / 100)
val Number.millimeters: Length get() = Length(toDouble() / 1_000)
val Number.kilometers: Length get() = Length(toDouble() * 1_000)
val Number.miles: Length get() = Length(milesToMeters(toDouble()))
val Number.feet: Length get() = Length(feetToMeters(toDouble()))
val Number.inches: Length get() = Length(inchesToMeters(toDouble()))
