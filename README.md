# RateRadar KZ

RateRadar KZ is an Android application concept for tracking currency rates and comparing buy and sell offers from exchange offices in Kazakhstan. This repository contains the initial Kotlin/Jetpack Compose project scaffold for SIS1.

## Planned functionality

- Users can choose a base and target currency from supported ISO currency codes.
- Users can enter an amount and calculate its converted value using the latest available rate.
- Users can browse exchange-office offers with buy rates, sell rates, and update times.
- Users can compare and sort offers by the best rate or resulting transaction amount.
- Users can filter offers by city, currency, and transaction type (buy or sell).
- Users can open an exchange-office detail screen with its address, contacts, and working hours.
- Users can add or remove favorite currencies and exchange offices, with selections retained after restart.
- Users can refresh data and see clear loading, empty, offline/cached, and error states.

## Project structure

```text
RateRadarKZ/
├── app/
│   ├── src/main/
│   │   ├── java/kz/edu/kbtu/rateradar/
│   │   │   ├── MainActivity.kt
│   │   │   └── ui/theme/
│   │   ├── res/
│   │   └── AndroidManifest.xml
│   ├── src/test/
│   └── build.gradle.kts
├── docs/
│   └── SIS1_Project_Description.md
├── output/pdf/
│   └── RateRadar_KZ_SIS1.pdf
├── sis2/                      # SIS2: LeetCode problems in Kotlin
│   ├── 01_TwoSum.kt
│   ├── 14_LongestCommonPrefix.kt
│   ├── 20_ValidParentheses.kt
│   ├── 21_MergeTwoSortedLists.kt
│   ├── 242_ValidAnagram.kt
│   └── README.md
├── gradle/wrapper/
├── .gitignore
├── build.gradle.kts
├── gradle.properties
├── settings.gradle.kts
├── gradlew
└── gradlew.bat
```

## Build and run

1. Open this folder in Android Studio.
2. Let Gradle finish syncing and select an emulator or connected Android device.
3. Click **Run app**.

From the terminal, use:

```bash
./gradlew assembleDebug
```

The project uses JDK 17 bytecode, Android SDK 36, and requires Android 7.0 (API 24) or newer.

## Documentation

The SIS1 project idea, navigation flow chart, and functionality list are in [docs/SIS1_Project_Description.md](docs/SIS1_Project_Description.md).

## SIS2 — Kotlin problems

Five Easy LeetCode problems solved in idiomatic Kotlin, one file per problem, with a short description of each approach: [sis2/](sis2/README.md).
