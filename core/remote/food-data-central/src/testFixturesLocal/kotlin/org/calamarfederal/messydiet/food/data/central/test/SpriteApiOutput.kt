package org.calamarfederal.messydiet.food.data.central.test

import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import org.calamarfederal.messydiet.food.data.central.remote.schema.BrandedFoodItemSchema
import org.calamarfederal.messydiet.remote.food.data.central.test.FoodItemExpect

@OptIn(ExperimentalStdlibApi::class)
internal fun parseJson(json: String): BrandedFoodItemSchema = Moshi
    .Builder()
    .build()
    .adapter<BrandedFoodItemSchema>()
    .fromJson(json)!!

internal val FoodItemExpect.SpriteTest.getFoodResultSchema: BrandedFoodItemSchema
    get() = parseJson(
        SpriteTestApiGetOutput
    )

internal const val SpriteTestApiGetOutput = """
    {
    "fdcId": 2613419,
    "availableDate": "10/1/2018",
    "brandOwner": "The Coca-Cola Company",
    "dataSource": "GDSN",
    "dataType": "Branded",
    "description": "Sprite Bottle, 1.75 Liters",
    "foodClass": "Branded",
    "gtinUpc": "00049000057980",
    "householdServingFullText": "12 fl oz (360mL)",
    "ingredients": "INGREDIENTS: CARBONATED WATER, HIGH FRUCTOSE CORN SYRUP, CITRIC ACID, NATURAL FLAVORS, SODIUM CITRATE, SODIUM BENZOATE (TO PROTECT TASTE).",
    "modifiedDate": "10/1/2018",
    "publicationDate": "8/31/2023",
    "servingSize": 360.0,
    "servingSizeUnit": "MLT",
    "preparationStateCode": "PREPARED",
    "brandedFoodCategory": "Non Alcoholic Beverages  Ready to Drink",
    "gpcClassCode": 50202300,
    "foodNutrients": [
        {
            "id": 32636707,
            "amount": 10.6,
            "type": "FoodNutrient",
            "nutrient": {
                "id": 1235,
                "number": "539",
                "name": "Sugars, added",
                "rank": 1540,
                "unitName": "g"
            },
            "foodNutrientDerivation": {
                "id": 71,
                "code": "LCSA",
                "description": "Calculated from an approximate value per serving size measure"
            }
        },
        {
            "id": 32636698,
            "amount": 10.83,
            "type": "FoodNutrient",
            "nutrient": {
                "id": 1005,
                "number": "205",
                "name": "Carbohydrate, by difference",
                "rank": 1110,
                "unitName": "g"
            },
            "foodNutrientDerivation": {
                "id": 71,
                "code": "LCSA",
                "description": "Calculated from an approximate value per serving size measure"
            }
        },
        {
            "id": 32636701,
            "amount": 10.56,
            "type": "FoodNutrient",
            "nutrient": {
                "id": 2000,
                "number": "269",
                "name": "Sugars, total including NLEA",
                "rank": 1510,
                "unitName": "g"
            },
            "foodNutrientDerivation": {
                "id": 71,
                "code": "LCSA",
                "description": "Calculated from an approximate value per serving size measure"
            }
        },
        {
            "id": 32636699,
            "amount": 39.0,
            "type": "FoodNutrient",
            "nutrient": {
                "id": 1008,
                "number": "208",
                "name": "Energy",
                "rank": 300,
                "unitName": "kcal"
            },
            "foodNutrientDerivation": {
                "id": 71,
                "code": "LCSA",
                "description": "Calculated from an approximate value per serving size measure"
            }
        },
        {
            "id": 32636703,
            "amount": 0.0,
            "type": "FoodNutrient",
            "nutrient": {
                "id": 1087,
                "number": "301",
                "name": "Calcium, Ca",
                "rank": 5300,
                "unitName": "mg"
            },
            "foodNutrientDerivation": {
                "id": 71,
                "code": "LCSA",
                "description": "Calculated from an approximate value per serving size measure"
            }
        },
        {
            "id": 32636705,
            "amount": 19.0,
            "type": "FoodNutrient",
            "nutrient": {
                "id": 1093,
                "number": "307",
                "name": "Sodium, Na",
                "rank": 5800,
                "unitName": "mg"
            },
            "foodNutrientDerivation": {
                "id": 71,
                "code": "LCSA",
                "description": "Calculated from an approximate value per serving size measure"
            }
        },
        {
            "id": 32636696,
            "amount": 0.0,
            "type": "FoodNutrient",
            "nutrient": {
                "id": 1003,
                "number": "203",
                "name": "Protein",
                "rank": 600,
                "unitName": "g"
            },
            "foodNutrientDerivation": {
                "id": 71,
                "code": "LCSA",
                "description": "Calculated from an approximate value per serving size measure"
            }
        },
        {
            "id": 32636709,
            "amount": 0.0,
            "type": "FoodNutrient",
            "nutrient": {
                "id": 1257,
                "number": "605",
                "name": "Fatty acids, total trans",
                "rank": 15400,
                "unitName": "g"
            },
            "foodNutrientDerivation": {
                "id": 71,
                "code": "LCSA",
                "description": "Calculated from an approximate value per serving size measure"
            }
        },
        {
            "id": 32636704,
            "amount": 0.0,
            "type": "FoodNutrient",
            "nutrient": {
                "id": 1089,
                "number": "303",
                "name": "Iron, Fe",
                "rank": 5400,
                "unitName": "mg"
            },
            "foodNutrientDerivation": {
                "id": 71,
                "code": "LCSA",
                "description": "Calculated from an approximate value per serving size measure"
            }
        },
        {
            "id": 32636700,
            "amount": 0.0,
            "type": "FoodNutrient",
            "nutrient": {
                "id": 1057,
                "number": "262",
                "name": "Caffeine",
                "rank": 18300,
                "unitName": "mg"
            },
            "foodNutrientDerivation": {
                "id": 71,
                "code": "LCSA",
                "description": "Calculated from an approximate value per serving size measure"
            }
        },
        {
            "id": 32636706,
            "amount": 0.0,
            "type": "FoodNutrient",
            "nutrient": {
                "id": 1162,
                "number": "401",
                "name": "Vitamin C, total ascorbic acid",
                "rank": 6300,
                "unitName": "mg"
            },
            "foodNutrientDerivation": {
                "id": 71,
                "code": "LCSA",
                "description": "Calculated from an approximate value per serving size measure"
            }
        },
        {
            "id": 32636697,
            "amount": 0.0,
            "type": "FoodNutrient",
            "nutrient": {
                "id": 1004,
                "number": "204",
                "name": "Total lipid (fat)",
                "rank": 800,
                "unitName": "g"
            },
            "foodNutrientDerivation": {
                "id": 71,
                "code": "LCSA",
                "description": "Calculated from an approximate value per serving size measure"
            }
        },
        {
            "id": 32636711,
            "amount": 0.0,
            "type": "FoodNutrient",
            "nutrient": {
                "id": 2067,
                "number": "960",
                "name": "Vitamin A",
                "rank": 7430,
                "unitName": "µg"
            },
            "foodNutrientDerivation": {
                "id": 71,
                "code": "LCSA",
                "description": "Calculated from an approximate value per serving size measure"
            }
        },
        {
            "id": 32636710,
            "amount": 0.0,
            "type": "FoodNutrient",
            "nutrient": {
                "id": 1258,
                "number": "606",
                "name": "Fatty acids, total saturated",
                "rank": 9700,
                "unitName": "g"
            },
            "foodNutrientDerivation": {
                "id": 71,
                "code": "LCSA",
                "description": "Calculated from an approximate value per serving size measure"
            }
        },
        {
            "id": 32636702,
            "amount": 0.0,
            "type": "FoodNutrient",
            "nutrient": {
                "id": 1079,
                "number": "291",
                "name": "Fiber, total dietary",
                "rank": 1200,
                "unitName": "g"
            },
            "foodNutrientDerivation": {
                "id": 71,
                "code": "LCSA",
                "description": "Calculated from an approximate value per serving size measure"
            }
        },
        {
            "id": 32636708,
            "amount": 0.0,
            "type": "FoodNutrient",
            "nutrient": {
                "id": 1253,
                "number": "601",
                "name": "Cholesterol",
                "rank": 15700,
                "unitName": "mg"
            },
            "foodNutrientDerivation": {
                "id": 71,
                "code": "LCSA",
                "description": "Calculated from an approximate value per serving size measure"
            }
        }
    ],
    "foodUpdateLog": [
        {
            "fdcId": 2613419,
            "availableDate": "10/1/2018",
            "brandOwner": "The Coca-Cola Company",
            "dataSource": "GDSN",
            "dataType": "Branded",
            "description": "Sprite Bottle, 1.75 Liters",
            "foodClass": "Branded",
            "gtinUpc": "00049000057980",
            "householdServingFullText": "12 fl oz (360mL)",
            "ingredients": "INGREDIENTS: CARBONATED WATER, HIGH FRUCTOSE CORN SYRUP, CITRIC ACID, NATURAL FLAVORS, SODIUM CITRATE, SODIUM BENZOATE (TO PROTECT TASTE).",
            "modifiedDate": "10/1/2018",
            "publicationDate": "8/31/2023",
            "servingSize": 360.0,
            "servingSizeUnit": "MLT",
            "brandedFoodCategory": "Non Alcoholic Beverages  Ready to Drink",
            "foodAttributes": []
        },
        {
            "fdcId": 2556784,
            "availableDate": "10/1/2018",
            "brandOwner": "The Coca-Cola Company",
            "dataSource": "GDSN",
            "dataType": "Branded",
            "description": "Sprite Bottle, 1.75 Liters",
            "foodClass": "Branded",
            "gtinUpc": "00049000057980",
            "householdServingFullText": "12 fl oz (360mL)",
            "ingredients": "INGREDIENTS: CARBONATED WATER, HIGH FRUCTOSE CORN SYRUP, CITRIC ACID, NATURAL FLAVORS, SODIUM CITRATE, SODIUM BENZOATE (TO PROTECT TASTE).",
            "modifiedDate": "10/1/2018",
            "publicationDate": "6/29/2023",
            "servingSize": 360.0,
            "servingSizeUnit": "MLT",
            "brandedFoodCategory": "Non Alcoholic Beverages  Ready to Drink",
            "foodAttributes": []
        },
        {
            "fdcId": 2519564,
            "availableDate": "10/1/2018",
            "brandOwner": "The Coca-Cola Company",
            "dataSource": "GDSN",
            "dataType": "Branded",
            "description": "Sprite Bottle, 1.75 Liters",
            "foodClass": "Branded",
            "gtinUpc": "00049000057980",
            "householdServingFullText": "12 fl oz (360mL)",
            "ingredients": "INGREDIENTS: CARBONATED WATER, HIGH FRUCTOSE CORN SYRUP, CITRIC ACID, NATURAL FLAVORS, SODIUM CITRATE, SODIUM BENZOATE (TO PROTECT TASTE).",
            "modifiedDate": "10/1/2018",
            "publicationDate": "4/27/2023",
            "servingSize": 360.0,
            "servingSizeUnit": "MLT",
            "brandedFoodCategory": "Non Alcoholic Beverages  Ready to Drink",
            "foodAttributes": []
        },
        {
            "fdcId": 769084,
            "availableDate": "12/14/2018",
            "brandOwner": "The Coca-Cola Company-0049000000016",
            "dataSource": "GDSN",
            "dataType": "Branded",
            "description": "Sprite Bottle, 1.75 Liters",
            "foodClass": "Branded",
            "gtinUpc": "00049000057980",
            "householdServingFullText": "12 fl oz (360mL)",
            "ingredients": "INGREDIENTS: CARBONATED WATER, HIGH FRUCTOSE CORN SYRUP, CITRIC ACID, NATURAL FLAVORS, SODIUM CITRATE, SODIUM BENZOATE (TO PROTECT TASTE).",
            "modifiedDate": "12/14/2018",
            "publicationDate": "2/27/2020",
            "servingSize": 360.0,
            "servingSizeUnit": "ml",
            "brandedFoodCategory": "Non Alcoholic Beverages  Ready to Drink",
            "foodAttributes": []
        },
        {
            "fdcId": 612052,
            "availableDate": "12/14/2018",
            "brandOwner": "The Coca-Cola Company-0049000000016",
            "dataSource": "GDSN",
            "dataType": "Branded",
            "description": "Sprite Bottle, 1.75 Liters",
            "foodClass": "Branded",
            "gtinUpc": "00049000057980",
            "householdServingFullText": "12 fl oz (360mL)",
            "ingredients": "INGREDIENTS: CARBONATED WATER, HIGH FRUCTOSE CORN SYRUP, CITRIC ACID, NATURAL FLAVORS, SODIUM CITRATE, SODIUM BENZOATE (TO PROTECT TASTE).",
            "modifiedDate": "12/14/2018",
            "publicationDate": "12/6/2019",
            "servingSize": 360.0,
            "servingSizeUnit": "ml",
            "brandedFoodCategory": "Non Alcoholic Beverages  Ready to Drink",
            "foodAttributes": []
        }
    ],
    "labelNutrients": {
        "fat": {},
        "saturatedFat": {},
        "transFat": {},
        "cholesterol": {},
        "sodium": {},
        "carbohydrates": {},
        "fiber": {},
        "sugars": {},
        "protein": {},
        "calcium": {},
        "iron": {},
        "calories": {}
    }
}
"""
