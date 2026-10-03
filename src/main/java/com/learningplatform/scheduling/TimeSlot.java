package com.learningplatform.scheduling;

import java.time.Instant;

public final class TimeSlot {
    private final Instant start;
    private final Instant end;

    public TimeSlot(Instant start, Instant end) {

        if (start == null || end == null) {
            throw new IllegalArgumentException("Start and end time cannot be null");
        }

        if (!end.isAfter(start)) {
            throw new IllegalArgumentException(
                    "End time must be after start time: start=" + start + ", end=" + end
            );
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


    public boolean overlaps(TimeSlot other) {
        return other.start.isBefore(end)
                && start.isBefore(other.end);
    }

    public Instant getStart() {
        return start;
    }

    public Instant getEnd() {
        return end;
    }

}