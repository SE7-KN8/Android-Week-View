package com.alamkanak.weekview

internal class EventChipsLoader<T>(
    config: WeekViewConfigWrapper,
    private val chipCache: EventChipCache<T>
) {

    var changed = true

    private val eventSplitter = WeekViewEventSplitter<T>(config)

    fun createAndCacheEventChips(events: List<WeekViewEvent<T>>) {
        chipCache.clear()
        chipCache += convertEventsToEventChips(events)
        changed = true
    }

    private fun convertEventsToEventChips(
        events: List<WeekViewEvent<T>>
    ): List<EventChip<T>> = events.sorted().map(this::convertEventToEventChips).flatten()

    private fun convertEventToEventChips(
        event: WeekViewEvent<T>
    ): List<EventChip<T>> = eventSplitter.split(event).map { EventChip(it, event, null) }
}
