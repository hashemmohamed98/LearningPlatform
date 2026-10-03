package com.learningplatform.scheduling;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TimeSlotTest {

    @Test
    void rejectsNullStart() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new TimeSlot(
                        null,
                        Instant.parse("2026-10-03T10:00:00Z")
                )
        );
    }

    @Test
    void rejectsNullEnd() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new TimeSlot(
                        Instant.parse("2026-10-03T09:00:00Z"),
                        null
                )
        );
    }

    @Test
    void rejectsEndBeforeStart() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new TimeSlot(
                        Instant.parse("2026-10-03T10:00:00Z"),
                        Instant.parse("2026-10-03T09:00:00Z")
                )
        );
    }

    @Test
    void rejectsEndEqualToStart() {
        Instant time = Instant.parse("2026-10-03T10:00:00Z");

        assertThrows(
                IllegalArgumentException.class,
                () -> new TimeSlot(time, time)
        );
    }

    @Test
    void touchingSlotsDoNotOverlap() {
        TimeSlot first = new TimeSlot(
                Instant.parse("2026-10-03T09:00:00Z"),
                Instant.parse("2026-10-03T10:00:00Z")
        );

        TimeSlot second = new TimeSlot(
                Instant.parse("2026-10-03T10:00:00Z"),
                Instant.parse("2026-10-03T11:00:00Z")
        );

        assertFalse(first.overlaps(second));
        assertFalse(second.overlaps(first));
    }
}