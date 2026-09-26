package com.learningplatform.scheduling;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class AvailabilityPlayground {

    public static void main(String[] args) {

        Instant dateTime = Instant.now();
        Instant dateTime2 = dateTime.plus(1, ChronoUnit.HOURS);

        TimeSlot timeSlot1 = new TimeSlot(dateTime, dateTime2);
        TimeSlot timeSlot2 = new TimeSlot(
                dateTime.plus(1, ChronoUnit.DAYS),
                dateTime2.plus(1, ChronoUnit.DAYS)
        );

        TutorAvailability tutorAvailability = new TutorAvailability();

        // 1. Open a slot
        tutorAvailability.open(timeSlot1);

        System.out.println(
                "open slot -> expected true, got: "
                        + tutorAvailability.isOpen(timeSlot1)
        );

        // 2. Open another slot
        tutorAvailability.open(timeSlot2);

        System.out.println(
                "open second slot -> expected true, got: "
                        + tutorAvailability.isOpen(timeSlot2)
        );

        // 3. Book an open slot
        TimeSlot timeSlot3 = new TimeSlot(dateTime, dateTime2);

        tutorAvailability.book(timeSlot3);

        System.out.println(
                "book open slot -> expected false, got: "
                        + tutorAvailability.isOpen(timeSlot3)
        );

        // 4. Book the same slot twice
        try {
            tutorAvailability.book(timeSlot3);

            System.out.println(
                    "book same slot twice -> expected ISE, got: NO EXCEPTION"
            );

        } catch (Exception e) {

            System.out.println(
                    "book same slot twice -> expected ISE, got: "
                            + e.getClass().getSimpleName()
            );
        }

        // 5. Book a slot that was never opened
        try {
            TimeSlot timeSlot4 = new TimeSlot(
                    dateTime.plus(2, ChronoUnit.DAYS),
                    dateTime2.plus(2, ChronoUnit.DAYS)
            );

            tutorAvailability.book(timeSlot4);

            System.out.println(
                    "book unavailable slot -> expected ISE, got: NO EXCEPTION"
            );

        } catch (Exception e) {

            System.out.println(
                    "book unavailable slot -> expected IAE, got: "
                            + e.getClass().getSimpleName()
            );
        }
        // =============================
// BREAK IT: MUTABLE HASHMAP KEY
// =============================

        Instant originalStart =
                Instant.parse("2026-09-24T10:00:00Z");

        Instant originalEnd =
                Instant.parse("2026-09-24T11:00:00Z");

        TimeSlot slot = new TimeSlot(originalStart, originalEnd);

        TutorAvailability availability = new TutorAvailability();

        availability.open(slot);

// Mutate the SAME object that is being used as the HashMap key.
        slot.moveTo(Instant.parse("2026-09-24T12:00:00Z"));


// 1. Same reference
        System.out.println(
                "1. isOpen(same reference) -> expected false, got: "
                        + availability.isOpen(slot)
        );


// 2. New object with ORIGINAL times
        TimeSlot original =
                new TimeSlot(originalStart, originalEnd);

        System.out.println(
                "2. isOpen(original times) -> expected false, got: "
                        + availability.isOpen(original)
        );


// 3. New object with MOVED times
        TimeSlot moved =
                new TimeSlot(
                        slot.getStart(),
                        slot.getEnd()
                );

        System.out.println(
                "3. isOpen(moved times) -> expected  false, got: "
                        + availability.isOpen(moved)
        );


// 4. Map size
        System.out.println(
                "4. size() -> expected 1 , got: "
                        + availability.size()
        );


// 5. Iterate over entries
        System.out.println("5. map entries:");

           availability.print();
            slot.moveTo(originalStart);
            System.out.println("6. after move-back, isOpen(same reference) -> expected true, got: "
                    + availability.isOpen(slot));
            }
    }