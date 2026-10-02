package com.learningplatform.cohorts;

public class CohortPlayground {

    public static void main(String[] args) {

        StudentId student1 = new StudentId("Student-1");
        StudentId student2 = new StudentId("Student-2");
        StudentId student3 = new StudentId("Student-3");
        StudentId student4 = new StudentId("Student-4");

        Cohort cohort = new Cohort();

        // Check 1: initial insertion order
        cohort.join(student1);
        cohort.join(student2);
        cohort.join(student3);
        cohort.join(student4);

        System.out.println(
                "Check 1 - Expected: Student-1 -> Student-2 -> Student-3 -> Student-4"
        );
        System.out.print("Got: ");
        cohort.print();

        // Check 2: rejoining a student from the front
        cohort.leave(student1);
        cohort.join(student1);

        System.out.println(
                "Check 2 - Expected: Student-2 -> Student-3 -> Student-4 -> Student-1"
        );
        System.out.print("Got: ");
        cohort.print();

        // Check 3: size
        System.out.println("Check 3 - Expected size: 4");
        System.out.println("Got: " + cohort.size());

        // Check 4: duplicate logical StudentId.
        // This is a different object, so equals/hashCode must be used.
        StudentId duplicateStudent1 = new StudentId("Student-1");

        System.out.println(
                "Check 4 - Expected duplicate join: IllegalStateException"
        );

        try {
            cohort.join(duplicateStudent1);
            System.out.println("Got: NO EXCEPTION");
        } catch (Exception e) {
            System.out.println(
                    "Got: " + e.getClass().getSimpleName()
            );
        }

        // Check 5: null join
        System.out.println(
                "Check 5 - Expected null join: IllegalArgumentException"
        );

        try {
            cohort.join(null);
            System.out.println("Got: NO EXCEPTION");
        } catch (Exception e) {
            System.out.println(
                    "Got: " + e.getClass().getSimpleName()
            );
        }

        // Check 6: leave twice is idempotent
        cohort.leave(student1);

        System.out.println("Check 6a - Expected size after first leave: 3");
        System.out.println("Got: " + cohort.size());

        cohort.leave(student1);

        System.out.println("Check 6b - Expected size after second leave: 3");
        System.out.println("Got: " + cohort.size());

        // Check 7: null leave
        System.out.println(
                "Check 7 - Expected null leave: IllegalArgumentException"
        );

        try {
            cohort.leave(null);
            System.out.println("Got: NO EXCEPTION");
        } catch (Exception e) {
            System.out.println(
                    "Got: " + e.getClass().getSimpleName()
            );
        }
    }
}