# Messy Diet

![app icon](/app/src/main/res/mipmap-xxxhdpi/ic_launcher.webp)

Nutrient tracker and diet manager. Create custom meals or scan the barcode of a food item to automatically retrieve the
nutrients and serving size. Adjust the serving size of a meal to calculate the change in nutritional information.

## Screenshots

![overview screenshot](/app/src/main/play/listings/en-US/graphics/phone-screenshots/1_all_meals.png)
![search upc screenshot](/app/src/main/play/listings/en-US/graphics/phone-screenshots/2_search_upc.png)
manually enter the UPC/GTIN or use the barcode scanner feature.

![search result screenshot](/app/src/main/play/listings/en-US/graphics/phone-screenshots/3_search_result.png)
![custom meal screenshot simple](/app/src/main/play/listings/en-US/graphics/phone-screenshots/4_meal_custom_simple.png)
![custom meal screenshot detailed](/app/src/main/play/listings/en-US/graphics/phone-screenshots/5_meal_custom_detailed.png)

## Barcode scanning

As of 25 September 2023, the barcode code is used to search only the USDA food database (FDC). The FDC database
unfortunately has internally inconsistent data and poor documentation. For example, GTIN/UPC 071068160241 is reported to
have 76mg serving size, instead of the actual 76g.

In the future, the plan is to add support for other api which could serve the same function.

## Building

The simplest way to build and run this app is to use Android Studio and checkout this repo. Then simply connect your
device and run the app.

## Libraries and Tools

* [Kodein DI](https://kosi-libs.org/kodein)
  * [Source](https://github.com/kosi-libs/Kodein)
  * Kotlin dependency injection framework
* [Mockk](https://mockk.io/)
  * [Source](https://github.com/mockk/mockk)
  * Kotlin mocking framework
* Hilt
* Room
* Retrofit
* Moshi
* [ML Kit's Barcode scanning](https://developers.google.com/ml-kit/vision/barcode-scanning)

## Structure

`:build-logic` contains convention plugins to create a single source of truth for the Android configurations.

`:app-platform` defines a gradle platform to enforce dependency version alignment between all modules, except those
added using `includedBuild` like `:build-logic`.

`:core` holds all the shared and platform agnostic code. **Except** `:core:android:hilt` which handles Coroutine
Dispatcher injection through Hilt and annotations. `:core:test:*` contains sample data and helper functions for use in
other tests.

`:feature` are where all the logical sections of the app are located. Each submodule in `:feature` contains two separate
modules of their own: `:data` and `:presentation`. **Except** `:feature:measure` which defines shared string resources
and compose code for units of measure (as defined in `:core:measure` and `:core:diet-model`).

`:feature:bmi:*` is currently unused, but it's basically a BMI calculator. It needs to be either removed or integrated
in the app.

`:screenshot` is where automatic screenshots are (going to be) configured. The screenshots are for sharing on platforms
like Github and F-droid, not for testing.

App metadata is specified
in [Triple-T Structure for F-Droid](https://f-droid.org/en/docs/All_About_Descriptions_Graphics_and_Screenshots/#triple-t-structure),
specifically in `app/main/play`. **The feature graphic is temporary and ought to change.**

## Philosophy and References

Unfortunately, this project has gone under multiple fairly radical structural shifts and the evidence is still very
apparent. However, the current structure is primarily based
on [android architecture guidance](https://developer.android.com/topic/architecture). The build logic is based on
Android sample app [Now in Android](https://github.com/android/nowinandroid), which is in turn based
on [https://developer.squareup.com/blog/herding-elephants/](https://developer.squareup.com/blog/herding-elephants/)
and [https://github.com/jjohannes/idiomatic-gradle](https://github.com/jjohannes/idiomatic-gradle). The presentation and
data layers of each feature are made separate gradle modules to enforce separation. The
file [libs.versions.toml](/libs.versions.toml) acts as the single source of truth for dependency versions. To enforce
consistent version alignment of transient dependencies,
a [platform](https://docs.gradle.org/current/userguide/java_platform_plugin.html#java_platform_plugin) is defined
in `:app-platform`.

## Codebase Commentary

This is an exploratory android and compose project. As such, there is an unfortunate lack of uniformity in conventions
of style, technique, and architectural design.

### DI

This project currently primarily uses Dagger Hilt for DI, but it also makes use of Kodein DI in
the `:core:remote:food-data-central` Gradle module. This was done as to experiment with Kodein. The plan is to unify the
whole project under one DI framework (either manual, hilt, or Kodein).

### Module Structure

Modules `:core:android:hilt` and `:feature:measure` should probably be in their own shared module.

Module `:core:measure` and maybe `:core:diet-model` should be (and eventually will be) its own project entirely with its
own Github repo. Then this project could depend on it as either a git submodule or a published Maven library.
