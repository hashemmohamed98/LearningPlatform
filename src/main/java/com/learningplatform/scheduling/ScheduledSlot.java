package com.learningplatform.scheduling;

final class ScheduledSlot {
    private final TimeSlot timeSlot;
    private SlotStatus status;

    ScheduledSlot(TimeSlot timeSlot, SlotStatus status) {
        this.timeSlot = timeSlot;
        this.status = status;
    }

    public void book() {
        if (status == SlotStatus.BOOKED) {
            throw new SlotUnavailableException(timeSlot);
        }

        status = SlotStatus.BOOKED;
    }

    ScheduledSlotView toView() {
        return new ScheduledSlotView(timeSlot, status);
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }
    public SlotStatus getStatus() {
        return status;
    }


}
