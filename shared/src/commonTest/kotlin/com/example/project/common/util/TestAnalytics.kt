package com.example.project.common.util

import com.example.project.analytics.domain.Analytics
import com.example.project.analytics.domain.AnalyticsEvent

internal class TestAnalytics : Analytics {

    private val _events = mutableListOf<AnalyticsEvent>()

    @Suppress("MemberVisibilityCanBePrivate") val events: List<AnalyticsEvent> = _events

    internal val lastTrackEvent: AnalyticsEvent
        get() = events.last()

    fun reset() {
        _events.clear()
    }

    override fun track(event: AnalyticsEvent) {
        _events.add(event)
    }

    override fun track(events: List<AnalyticsEvent>) {
        _events.addAll(events)
    }
}

internal val testAnalytics = TestAnalytics()
