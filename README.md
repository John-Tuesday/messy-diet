# Messy Diet

![app icon](/app/src/main/res/mipmap-xxxhdpi/ic_launcher.webp)

Nutrient tracker and diet manager. Create custom meals or scan the barcode of a food item to automatically retrieve the
nutrients and serving size. Adjust the serving size of a meal to calculate the change in nutritional information.

## Barcode scanning

As of 25 September 2023, the barcode code is used to search only the USDA food database (FDC). The FDC database
unfortunately has internally inconsistent data and poor documentation. For example, GTIN/UPC 071068160241 is reported to
have 76mg serving size, instead of the actual 76g.

In the future, the plan is to add support for other api which could serve the same function.

## Regarding the Code

This is an exploratory android and compose project. As such, there is an unfortunate lack of uniformity in conventions
of style, technique, and architectural design.

This project currently primarily uses Dagger Hilt for DI, but it also makes use of Kodein DI in the core:remote:
food-data-central Gradle module. This was done as to experiment with Kodein. The plan is to unify the whole project
under one DI framework (either manual, hilt, or Kodein).

For a mocking framework, this project uses Mockk.
