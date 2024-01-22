This is a Kotlin Multiplatform project targeting Android, iOS.

## Local Project Setup:
- Follow instructions [here](https://www.jetbrains.com/help/kotlin-multiplatform-dev/multiplatform-setup.html) when using Android Studio IDE
- Follow instructions [here](https://www.jetbrains.com/help/kotlin-multiplatform-dev/fleet.html) when using Jetbrains Fleet IDE

**Be sure to open both Android Studio (including an emulator) and XCode (including a simulator) at least once before opening this project in the newer Fleet IDE. If the iOS build does not work in Fleet, close it, and try to open the iosApp.xcodeproj within XCode. Build it successfully, and then try Fleet again.**

## Multiplatform Project Structure
* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - `commonMain` is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    `iosMain` would be the right folder for such calls.

* `/iosApp` contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform, 
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.

* `/shared` is for the code that will be shared between all targets in the project.
  The most important subfolder is `commonMain`. If preferred, you can add code to the platform-specific folders here too.

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…

## Dependencies used
* [sqldelight](https://cashapp.github.io/sqldelight/2.0.1/multiplatform_sqlite/) - for storing and loading the nominee data
