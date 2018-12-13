/*
 * Created On : 12/7/18 11:05 AM
 * Author : Aqil Prakoso
 * Copyright (c) 2018 iRevStudio
 */

package com.irevstudio.timeapp.presenter

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.irevstudio.timeapp.data.DataAplikasi
import com.irevstudio.timeapp.util.AppUtil
import java.text.SimpleDateFormat
import java.util.*

class PengolahData {

    private val USAGE_TIME_MIX: Long = 5000
    //===============================percobaan

    fun getTargetAppData(context: Context, namaapp: String): List<DataAplikasi> {
        val items = ArrayList<DataAplikasi>()

        val mUsageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager?
        if (mUsageStatsManager != null) {

            val timeNow = System.currentTimeMillis()
            val cal = Calendar.getInstance()
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)
            val startTime = cal.timeInMillis

            val events = mUsageStatsManager.queryEvents(startTime, timeNow)
            val event = UsageEvents.Event()
            Log.d("tanggal", "start $startTime")
            Log.d("tanggal", "now $timeNow")
            val item = DataAplikasi()
            var backEvent: ClonedEvent? = null
            var start: Long = 0

            while (events.hasNextEvent()) {
                events.getNextEvent(event)
                val currentPackage = event.packageName
                val eventType = event.eventType
                val eventTime = event.timeStamp
                if (currentPackage == namaapp) {
                    Log.d(
                        "||||||||||>",
                        "$currentPackage " + SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()).format(
                            Date(eventTime)
                        ) + " " + eventType
                    )

                    Log.d("********", "start $start")
                    if (eventType == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                        if (start == 0L) {
                            start = eventTime
                            item.mEventTime = eventTime
                            item.mEventType = eventType
                            item.mUsageTime = 0
                            items.add(item.copy())
                        }
                    } else if (eventType == UsageEvents.Event.MOVE_TO_BACKGROUND) { // 结束事件
                        if (start > 0) {
                            backEvent = ClonedEvent(event)
                        }
                        Log.d("********", "add end $start")
                    }
                } else {
                    if (backEvent != null && start > 0) {
                        item.mEventTime = backEvent.timeStamp
                        item.mEventType = backEvent.eventType
                        item.mUsageTime = backEvent.timeStamp - start
                        Log.d("masukan", "back " + backEvent.timeStamp + " - " + start + " = " + item.mUsageTime)
                        if (item.mUsageTime!! <= 0) item.mUsageTime = 0
                        if (item.mUsageTime!! > USAGE_TIME_MIX) item.mCount++
                        items.add(item.copy())
                        start = 0
                        backEvent = null
                    }
                }
            }
        }
        return items
    }

    fun getAppData(appItems: List<DataAplikasi>) {

        var duration: Long = 0
        for (item in appItems) {
            if (item.mEventType == UsageEvents.Event.USER_INTERACTION || item.mEventType == UsageEvents.Event.NONE) {
                continue
            }
            duration += item.mUsageTime!!
        }
        Log.d("aqil", "values = " + AppUtil.formatMilliSeconds(duration))

    }

    class ClonedEvent(event: UsageEvents.Event) {

        var packageName: String
        var eventClass: String
        var timeStamp: Long = 0
        var eventType: Int = 0

        init {
            packageName = event.packageName
            eventClass = event.className
            timeStamp = event.timeStamp
            eventType = event.eventType
        }
    }

    //======================================getAllApps
    fun getApps(context: Context, sort: Int): List<DataAplikasi> {

        val items = ArrayList<DataAplikasi>()
        val newList = ArrayList<DataAplikasi>()
        val manager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager?
        if (manager != null) {

            var prevPackage = ""
            val startPoints = HashMap<String, Long>()
            val endPoints = HashMap<String, ClonedEvent>()


            val timeNow = System.currentTimeMillis()
            val cal = Calendar.getInstance()
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)
            val startTime = cal.timeInMillis

            val events = manager.queryEvents(startTime, timeNow)
            val event = UsageEvents.Event()
            while (events.hasNextEvent()) {

                events.getNextEvent(event)
                val eventType = event.eventType
                val eventTime = event.timeStamp
                val eventPackage = event.packageName

                if (eventType == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                    var item = containItem(items, eventPackage)
                    if (item == null) {
                        item = DataAplikasi()
                        item.mPackageName = eventPackage
                        items.add(item)
                    }
                    if (!startPoints.containsKey(eventPackage)) {
                        startPoints[eventPackage] = eventTime
                    }
                }

                if (eventType == UsageEvents.Event.MOVE_TO_BACKGROUND) {
                    if (startPoints.size > 0 && startPoints.containsKey(eventPackage)) {
                        endPoints[eventPackage] = ClonedEvent(event)
                    }
                }

                if (TextUtils.isEmpty(prevPackage)) prevPackage = eventPackage
                if (prevPackage != eventPackage) { // 包名有变化
                    if (startPoints.containsKey(prevPackage) && endPoints.containsKey(prevPackage)) {
                        val lastEndEvent = endPoints[prevPackage]
                        val listItem = containItem(items, prevPackage)
                        if (listItem != null) { // update list item info
                            listItem.mEventTime = lastEndEvent!!.timeStamp
                            listItem.mEventTime = lastEndEvent.timeStamp
                            var duration = lastEndEvent.timeStamp - startPoints[prevPackage]!!
                            if (duration <= 0) duration = 0
                            listItem.mUsageTime = listItem.mUsageTime?.plus(duration)
                            if (duration > USAGE_TIME_MIX) {
                                listItem.mCount++
                            }
                        }
                        startPoints.remove(prevPackage)
                        endPoints.remove(prevPackage)
                    }
                    prevPackage = eventPackage
                }
            }
        }

        if (items.size > 0) {
            val packageManager = context.packageManager
            for (item in items) {
                item.mName = AppUtil.parsePackageName(packageManager, item.mPackageName!!)
                newList.add(item)
            }

            when (sort) {
                0 -> newList.sortWith(Comparator { left, right -> (left.mUsageTime?.let { right.mUsageTime?.minus(it) })?.toInt()!! })
                1 -> newList.sortWith(Comparator { left, right -> (left.mEventTime?.let { right.mEventTime?.minus(it) })?.toInt()!! })
                else -> newList.sortWith(Comparator { left, right -> right.mCount - left.mCount })
            }
        }
        return newList
    }

    private fun containItem(items: List<DataAplikasi>, packageName: String): DataAplikasi? {
        for (item in items) {
            if (item.mPackageName == packageName) return item
        }
        return null
    }

    companion object {
        var instance: PengolahData? = null
            private set

        fun init() {
            instance = PengolahData()
        }
    }
}

