/*
 * Created On : 12/9/18 6:36 PM
 * Author : Aqil Prakoso
 * Copyright (c) 2018 iRevStudio
 */

package com.irevstudio.timeapp.util

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager

object AppUtil {

    fun parsePackageName(pckManager: PackageManager, data: String): String {
        val applicationInformation: ApplicationInfo? = try {
            pckManager.getApplicationInfo(data, PackageManager.GET_META_DATA)
        } catch (e: PackageManager.NameNotFoundException) {
            null
        }

        return (if (applicationInformation != null) pckManager.getApplicationLabel(applicationInformation) else data) as String
    }

    fun formatMilliSeconds(milliSeconds: Long): String {
        val second = milliSeconds / 1000L
        return when {
            second < 60 -> String.format("%ss", second)
            second < 60 * 60 -> String.format("%sm %ss", second / 60, second % 60)
            else -> String.format("%sh %sm %ss", second / 3600, second % 3600 / 60, second % 3600.toLong() % 60)
        }
    }

}