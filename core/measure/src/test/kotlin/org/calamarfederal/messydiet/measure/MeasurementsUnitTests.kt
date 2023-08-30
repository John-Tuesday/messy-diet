package org.calamarfederal.messydiet.measure

import org.junit.Test
import kotlin.math.absoluteValue

private fun assertEqual(actual: Double, expected: Double, tolerance: Double = 0.000_001) = assert(
    (actual - expected).absoluteValue <= tolerance
) {
    "actual: $actual\nexpected: $expected\ntolerance: $tolerance"
}

private fun <T> assertEqual(actual: T, expected: T, tolerance: Double = 0.000_001, getValue: (T) -> Double) = assertEqual(
    actual = getValue(actual),
    expected = getValue(expected),
    tolerance = tolerance,
)
private fun assertEqual(actual: Length, expected: Length, tolerance: Double = 0.000_001) =
    assertEqual(
        actual = actual,
        expected = expected,
        tolerance = tolerance,
    ) { it.meter }

private fun assertEqual(actual: Weight, expected: Weight, tolerance: Double = 0.000_001) =
    assertEqual(
        actual = actual,
        expected = expected,
        tolerance = tolerance,
    ) { it.grams }

private fun assertEqual(actual: Volume, expected: Volume, tolerance: Double = 0.000_001) =
    assertEqual(
        actual = actual,
        expected = expected,
        tolerance = tolerance,
    ) { it.liters }

class MeasurementsUnitTests {
    @Test
    fun `Metric identity test`() {
        assertEqual(1.grams, 0.001.kilograms)
        assertEqual(1.grams.inMilligrams(), 0.001.kilograms.inMilligrams())
        assertEqual(1.grams.inGrams(), 0.001.kilograms.inGrams())
        assertEqual(1.grams, 1_000.milligrams)
        assertEqual(1.grams.inKilograms(), 1_000.milligrams.inKilograms())

        assertEqual(1.liters, 1_000.milliliters)
        assertEqual(1.liters.inLiters(), 1_000.milliliters.inLiters())
        assertEqual(1.liters.inMilliliters(), 1_000.milliliters.inMilliliters())

        assertEqual(1.meters, 0.001.kilometers)
        assertEqual(1.meters.inCentimeters(), 0.001.kilometers.inCentimeters())
        assertEqual(1.meters.inMeters(), 0.001.kilometers.inMeters())
        assertEqual(1.meters, 100.centimeters)
        assertEqual(1.meters.inMillimeters(), 100.centimeters.inMillimeters())
        assertEqual(1.meters, 1_000.millimeters)
        assertEqual(1.meters.inKilometers(), 1_000.millimeters.inKilometers())
    }

    @Test
    fun `US identity tests`() {
        assertEqual(12.inches, 1.feet)
        assertEqual(12.inches.inInches(), 1.feet.inInches())
        assertEqual(1.miles, 5280.feet)
        assertEqual(1.miles.inMiles(), 5280.feet.inMiles())
        assertEqual(1.miles.inFeet(), 5280.feet.inFeet())

        assertEqual(1.usGallons, 128.usFlOz)
        assertEqual(1.usGallons.inUsTablespoons(), 128.usFlOz.inUsTablespoons())
        assertEqual(1.usGallons.inUsGallons(), 128.usFlOz.inUsGallons())
        assertEqual(1.usGallons, 256.usTablespoons)
        assertEqual(1.usGallons.inUsTeaspoons(), 256.usTablespoons.inUsTeaspoons())
        assertEqual(1.usGallons, 768.usTeaspoons)
        assertEqual(1.usGallons.inUsFlOz(), 768.usTeaspoons.inUsFlOz())

        assertEqual(1.lbs, 16.oz)
        assertEqual(1.lbs.inPounds(), 16.oz.inPounds())
        assertEqual(1.oz, 0.0625.lbs)
        assertEqual(1.oz.inOunces(), 0.0625.lbs.inOunces())
    }

    @Test
    fun `US X Metric identity tests`() {
        assertEqual(1.inches, 0.025_4.meters)
        assertEqual(1.feet, 0.304_8.meters)
        assertEqual(1.miles, 1_609.344.meters)

        assertEqual(1.lbs, 453.59237.grams)
        assertEqual(1.oz, 28.349523125.grams)

        assertEqual(1.usGallons, 3.785_411_784.liters)
        assertEqual(1.usFlOz, 0.029_573_529_562_5.liters)
        assertEqual(1.usTeaspoons, 0.004_928_921_593_75.liters)
        assertEqual(1.usTablespoons, 0.014_786_764_781_25.liters)
    }

    @Test
    fun `Weight conversion`() {
        val testWeight = 7.grams

        assert(testWeight.inKilograms() == 0.007)

        assert(testWeight.inGrams() == 7.00)
        assert(testWeight.inMilligrams() == 7_000.00)

        assertEqual(testWeight.inOunces(), 0.246_918)
        assertEqual(testWeight.inPounds(), 0.0154324)
    }

    @Test
    fun `Length conversion`() {
        val testLength = 7.meters

        assert(testLength.inKilometers() == 0.007)
        assert(testLength.inMeters() == 7.00)
        assert(testLength.inCentimeters() == 700.00)
        assert(testLength.inMillimeters() == 7_000.00)

        assertEqual(testLength.inMiles(),0.004_349_6)
        assertEqual(testLength.inFeet() , 22.96587926509186)
        assertEqual(testLength.inInches(), 275.59055118110234)
    }

    @Test
    fun `Volume conversion`() {
        val testVolume = 7.liters

        assert(testVolume.inLiters() == 7.00)
        assert(testVolume.inMilliliters() == 7_000.00)

        assertEqual(testVolume.inUsFlOz(), 236.698158912901)
        assertEqual(testVolume.inUsGallons(), 1.849204366507039)
        assertEqual(testVolume.inUsTablespoons(), 473.396317825802)
        assertEqual(testVolume.inUsTeaspoons() , 1420.188953477406)
    }
}
