package com.learningplatform.scheduling;

import java.util.HashMap;
import java.util.Map;

public final class TutorAvailability {

    private final Map<TimeSlot, SlotStatus> slots = new HashMap<>();



    public void open(TimeSlot timeSlot) {
        SlotStatus slotStatus = slots.putIfAbsent(timeSlot, SlotStatus.OPEN);
        if (slotStatus != null) {
            throw new IllegalStateException("Slot is already open");
        }
    }

    public void book(TimeSlot timeSlot) {
        SlotStatus tsl = slots.get(timeSlot);
        if(tsl == null){
            throw new IllegalArgumentException("TimeSlot " + timeSlot.toString() + " not published");
        }
        if (tsl == SlotStatus.BOOKED) {
            throw new IllegalStateException("Time Slot Already Booked");
        }
        slots.put(timeSlot, SlotStatus.BOOKED);
    }

    public boolean isOpen(TimeSlot timeSlot) {
        return SlotStatus.OPEN.equals(slots.get(timeSlot));

    }
    public int size() {
        return slots.size();
    }
    public void print() {
        for (Map.Entry<TimeSlot, SlotStatus> entry : slots.entrySet()) {
            System.out.println(entry.getKey().toString() + " -> " + entry.getValue().toString());
        }
    }

}
