package com.ddaypunk.aperture.di

import com.ddaypunk.aperture.db.ApertureDatabase
import org.koin.core.module.Module

/**
 * KMM expectation for platforms to implement Koin module providing their specific driver
 * - Database schema: [ApertureDatabase] (declared in :shared/build.gradle.kts sqldelight block) and *.sq files found in :shared/src/common/sqldelight
 * - in Android returns an AndroidSqliteDriver instance - see :shared/src/androidMain/DriverFactory.android.kt
 * - in iOS returns a NativeSqliteDriver instance - see :shared/src/iosMain/DriverFactory.ios.kt
 */
expect val databaseDriverModule: Module