package com.learningplatform.scheduling;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public final class TutorAvailability {

    private final TreeMap<Instant, ScheduledSlot> slots;

    public TutorAvailability() {
        this.slots = new TreeMap<>();
    }

    public void open(TimeSlot timeSlot) {
        Instant newStart = timeSlot.getStart();

        Map.Entry<Instant, ScheduledSlot> previous = slots.floorEntry(newStart);
        Map.Entry<Instant, ScheduledSlot> next = slots.higherEntry(newStart);

        boolean overlapsPrevious = previous != null && previous.getValue().getTimeSlot().overlaps(timeSlot);

        boolean overlapsNext = next != null && next.getValue().getTimeSlot().overlaps(timeSlot);

        if (overlapsPrevious) {
            throw new IllegalStateException("TimeSlot " + timeSlot + " overlaps with published slot " + previous.getValue().getTimeSlot());
        }

        if (overlapsNext) {
            throw new IllegalStateException("TimeSlot " + timeSlot + " overlaps with published slot " + next.getValue().getTimeSlot());
        }

        slots.put(newStart, new ScheduledSlot(timeSlot, SlotStatus.OPEN));
    }

    public void book(TimeSlot timeSlot) {
        ScheduledSlot slot = getPublishedSlot(timeSlot);
        slot.book();
    }

    public boolean isOpen(TimeSlot timeSlot) {
        ScheduledSlot slot = getPublishedSlot(timeSlot);

        return slot.getStatus() == SlotStatus.OPEN;
    }

    private ScheduledSlot getPublishedSlot(TimeSlot timeSlot) {
        ScheduledSlot slot = slots.get(timeSlot.getStart());

        if (slot == null || !slot.getTimeSlot().equals(timeSlot)) {
            throw new IllegalArgumentException("TimeSlot " + timeSlot + " not published");
        }

        return slot;
    }

    public int size() {
        return slots.size();
    }

    public List<TimeSlot> getAvailableSlots() {
        List<TimeSlot> availableSlots = new ArrayList<>();

        for (Map.Entry<Instant, ScheduledSlot> entry : slots.entrySet()) {
            ScheduledSlot scheduledSlot = entry.getValue();

            if (scheduledSlot.getStatus() == SlotStatus.OPEN) {
                availableSlots.add(scheduledSlot.getTimeSlot());
            }
        }

        return List.copyOf(availableSlots);
    }

    public List<ScheduledSlotView> getScheduledSlots() {
        List<ScheduledSlotView> scheduledSlots = new ArrayList<>();
        for (ScheduledSlot slot : slots.values()) {
            ScheduledSlotView scheduledSlotView = new ScheduledSlotView(slot.getTimeSlot(), slot.getStatus());
            scheduledSlots.add(scheduledSlotView);
        }
        return List.copyOf(scheduledSlots);
    }
}
