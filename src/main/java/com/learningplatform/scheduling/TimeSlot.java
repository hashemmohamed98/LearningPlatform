package com.learningplatform.scheduling;

import java.time.Duration;
import java.time.Instant;

public  final class TimeSlot {
    private Instant start;
    private Instant end;

    public TimeSlot(Instant start, Instant end) {

        if (start == null || end == null) {
            throw new IllegalArgumentException("start and end time cannot be null");
        }
        if (!end.isAfter(start)) {
            throw new IllegalArgumentException("start time=" + start+" is after end Time = "+end);
        }

        this.start = start;
        this.end = end;
    }

    @Override
    public int hashCode() {
        return 31 * start.hashCode() + end.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimeSlot)) {
            return false;
        }
        return start.equals(((TimeSlot) o).start) && end.equals(((TimeSlot) o).end);

    }

    @Override
    public String toString() {
        return start.toString() + " -> " + end.toString();
    }

    public void moveTo(Instant newStart) {
        if (newStart == null) {
            throw new IllegalArgumentException("new Start time is null");
        }

        Duration shift = Duration.between(start, newStart);

        start = newStart;
        end = end.plus(shift);
    }

    public Instant getStart() {
        return start;
    }

    public Instant getEnd() {
        return end;
    }

}