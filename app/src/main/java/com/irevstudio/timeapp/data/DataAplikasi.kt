/*
 * Created On : 12/7/18 11:06 AM
 * Author : Aqil Prakoso
 * Copyright (c) 2018 iRevStudio
 */

package com.irevstudio.timeapp.data

class DataAplikasi {
    var mName: String? = null
    var mPackageName: String? = null
    var mEventTime: Long? = 0
    var mUsageTime: Long? = 0
    var mEventType: Int? = 0
    var mCount: Int = 0

    fun copy(): DataAplikasi {
        val newItem = DataAplikasi()
        newItem.mName = this.mName
        newItem.mPackageName = this.mPackageName
        newItem.mEventTime = this.mEventTime
        newItem.mUsageTime = this.mUsageTime
        newItem.mEventType = this.mEventType
        newItem.mCount = this.mCount
        return newItem
    }
}
