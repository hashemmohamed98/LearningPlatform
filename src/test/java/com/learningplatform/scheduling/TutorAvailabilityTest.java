package com.learningplatform.scheduling;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class TutorAvailabilityTest {

    private TimeSlot slot(String start, String end) {
        return new TimeSlot(
                Instant.parse("2026-10-03T" + start + ":00Z"),
                Instant.parse("2026-10-03T" + end + ":00Z")
        );
    }

    @Test
    void rejectsOverlapInTheMiddle() {
        TutorAvailability availability = new TutorAvailability();

        availability.open(slot("09:00", "10:00"));

        List<ScheduledSlotView> before =
                availability.getScheduledSlots();

        assertThrows(
                IllegalStateException.class,
                () -> availability.open(slot("09:30", "10:30"))
        );

        assertEquals(before, availability.getScheduledSlots());
    }

    @Test
    void rejectsOverlapFromTheLeft() {
        TutorAvailability availability = new TutorAvailability();

        availability.open(slot("09:00", "10:00"));

        List<ScheduledSlotView> before =
                availability.getScheduledSlots();

        assertThrows(
                IllegalStateException.class,
                () -> availability.open(slot("08:00", "09:30"))
        );

        assertEquals(before, availability.getScheduledSlots());
    }

    @Test
    void rejectsNewSlotContainingExistingSlot() {
        TutorAvailability availability = new TutorAvailability();

        availability.open(slot("09:00", "10:00"));

        List<ScheduledSlotView> before =
                availability.getScheduledSlots();

        assertThrows(
                IllegalStateException.class,
                () -> availability.open(slot("08:00", "11:00"))
        );

        assertEquals(before, availability.getScheduledSlots());
    }

    @Test
    void acceptsAdjacentSlotAfterExistingSlot() {
        TutorAvailability availability = new TutorAvailability();

        availability.open(slot("09:00", "10:00"));
        availability.open(slot("10:00", "11:00"));

        assertEquals(
                List.of(
                        new ScheduledSlotView(
                                slot("09:00", "10:00"),
                                SlotStatus.OPEN
                        ),
                        new ScheduledSlotView(
                                slot("10:00", "11:00"),
                                SlotStatus.OPEN
                        )
                ),
                availability.getScheduledSlots()
        );
    }

    @Test
    void acceptsNonOverlappingSlotAfterExistingSlot() {
        TutorAvailability availability = new TutorAvailability();

        availability.open(slot("09:00", "10:00"));
        availability.open(slot("11:00", "12:00"));

        assertEquals(2, availability.size());
    }

    @Test
    void acceptsSlotWithNoNeighbours() {
        TutorAvailability availability = new TutorAvailability();

        availability.open(slot("09:00", "10:00"));

        assertEquals(1, availability.size());
    }

    @Test
    void acceptsAdjacentSlotBeforeExistingSlot() {
        TutorAvailability availability = new TutorAvailability();

        availability.open(slot("09:00", "10:00"));
        availability.open(slot("08:00", "09:00"));

        assertEquals(
                List.of(
                        new ScheduledSlotView(
                                slot("08:00", "09:00"),
                                SlotStatus.OPEN
                        ),
                        new ScheduledSlotView(
                                slot("09:00", "10:00"),
                                SlotStatus.OPEN
                        )
                ),
                availability.getScheduledSlots()
        );
    }

    @Test
    void acceptsSlotThatFitsGapBetweenTwoNeighbours() {
        TutorAvailability availability = new TutorAvailability();

        availability.open(slot("09:00", "10:00"));
        availability.open(slot("11:00", "12:00"));

        availability.open(slot("10:00", "11:00"));

        assertEquals(
                List.of(
                        new ScheduledSlotView(
                                slot("09:00", "10:00"),
                                SlotStatus.OPEN
                        ),
                        new ScheduledSlotView(
                                slot("10:00", "11:00"),
                                SlotStatus.OPEN
                        ),
                        new ScheduledSlotView(
                                slot("11:00", "12:00"),
                                SlotStatus.OPEN
                        )
                ),
                availability.getScheduledSlots()
        );
    }

    @Test
    void rejectsSameStartWithDifferentEnd() {
        TutorAvailability availability = new TutorAvailability();

        availability.open(slot("09:00", "10:00"));

        List<ScheduledSlotView> before =
                availability.getScheduledSlots();

        assertThrows(
                IllegalStateException.class,
                () -> availability.open(slot("09:00", "11:00"))
        );

        assertEquals(before, availability.getScheduledSlots());
    }

    @Test
    void rejectsSlotInsideExistingSlot() {
        TutorAvailability availability = new TutorAvailability();

        availability.open(slot("09:00", "10:00"));

        assertThrows(
                IllegalStateException.class,
                () -> availability.open(slot("09:15", "09:45"))
        );
    }

    @Test
    void rejectsExactDuplicateOfBookedSlot() {
        TutorAvailability availability = new TutorAvailability();

        TimeSlot bookedSlot = slot("09:00", "10:00");

        availability.open(bookedSlot);
        availability.book(bookedSlot);

        assertFalse(availability.isOpen(bookedSlot));

        assertThrows(
                IllegalStateException.class,
                () -> availability.open(bookedSlot)
        );

        assertFalse(availability.isOpen(bookedSlot));
    }

    @Test
    void bookRejectsSlotWithSameStartButDifferentEnd() {
        TutorAvailability availability = new TutorAvailability();

        TimeSlot publishedSlot = slot("09:00", "10:00");
        TimeSlot requestedSlot = slot("09:00", "09:30");

        availability.open(publishedSlot);

        assertThrows(
                IllegalArgumentException.class,
                () -> availability.book(requestedSlot)
        );

        assertTrue(availability.isOpen(publishedSlot));
    }

    @Test
    void getAvailableSlotsReturnsOnlyOpenSlots() {
        TutorAvailability availability = new TutorAvailability();

        TimeSlot first = slot("09:00", "10:00");
        TimeSlot second = slot("11:00", "12:00");

        availability.open(first);
        availability.open(second);

        availability.book(first);

        assertEquals(
                List.of(second),
                availability.getAvailableSlots()
        );
    }

    @Test
    void isOpenRejectsSlotWithSameStartButDifferentEnd() {
        TutorAvailability availability = new TutorAvailability();

        TimeSlot publishedSlot = slot("09:00", "10:00");
        TimeSlot requestedSlot = slot("09:00", "09:30");

        availability.open(publishedSlot);

        assertThrows(
                IllegalArgumentException.class,
                () -> availability.isOpen(requestedSlot)
        );
    }

    @Test
    void returnsScheduledSlotsSortedByStart() {
        TutorAvailability availability = new TutorAvailability();

        availability.open(slot("11:00", "12:00"));
        availability.open(slot("09:00", "10:00"));
        availability.open(slot("10:00", "11:00"));

        assertEquals(
                List.of(
                        new ScheduledSlotView(
                                slot("09:00", "10:00"),
                                SlotStatus.OPEN
                        ),
                        new ScheduledSlotView(
                                slot("10:00", "11:00"),
                                SlotStatus.OPEN
                        ),
                        new ScheduledSlotView(
                                slot("11:00", "12:00"),
                                SlotStatus.OPEN
                        )
                ),
                availability.getScheduledSlots()
        );
    }

}