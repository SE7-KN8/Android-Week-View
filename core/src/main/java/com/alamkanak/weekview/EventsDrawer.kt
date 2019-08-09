package com.alamkanak.weekview

import android.content.Context
import android.graphics.Canvas
import java.util.Calendar

internal class SingleEventsDrawer<T>(
    context: Context,
    config: WeekViewConfigWrapper,
    private val chipCache: EventChipCache<T>
) : Drawer {

    private val eventChipDrawer = EventChipDrawer<T>(context, config)

    override fun draw(
        drawingContext: DrawingContext,
        canvas: Canvas
    ) = with(drawingContext) {
        for (date in dateRange) {
            drawEventsForDate(date, canvas)
        }
    }

    private fun drawEventsForDate(
        date: Calendar,
        canvas: Canvas
    ) {
        chipCache
            .normalEventChipsByDate(date)
            .filter { it.rect != null }
            .forEach { eventChipDrawer.draw(it, canvas) }
    }
}

internal class AllDayEventsDrawer<T>(
    context: Context,
    private val config: WeekViewConfigWrapper,
    private val cache: WeekViewCache<T>
) : CachingDrawer {

    private val eventChipDrawer = EventChipDrawer<T>(context, config)

    override fun draw(
        drawingContext: DrawingContext,
        canvas: Canvas
    ) {
        val eventChips = cache.allDayEventLayouts
        for (pair in eventChips) {
            val eventChip = pair.first
            val textLayout = pair.second
            eventChipDrawer.draw(eventChip, canvas, textLayout)
        }

        // Hide events when they are in the top left corner
        val headerBackground = config.headerBackgroundPaint

        val headerRowBottomLine = if (config.showHeaderRowBottomLine) {
            config.headerRowBottomLinePaint.strokeWidth
        } else {
            0f
        }

        val height = config.headerHeight - headerRowBottomLine * 1.5f
        val width = config.timeTextWidth + config.timeColumnPadding * 2

        canvas.clipRect(0f, 0f, width, height)
        canvas.drawRect(0f, 0f, width, height, headerBackground)

        canvas.restore()
        canvas.save()
    }

    override fun clear() {
        cache.clearAllDayEventLayouts()
    }
}
