package com.learningplatform.cohorts;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class CohortTest {

    @Test
    void joinPreservesInsertionOrder() {
        Cohort cohort = new Cohort();

        StudentId student1 = new StudentId("Student-1");
        StudentId student2 = new StudentId("Student-2");
        StudentId student3 = new StudentId("Student-3");
        StudentId student4 = new StudentId("Student-4");

        cohort.join(student1);
        cohort.join(student2);
        cohort.join(student3);
        cohort.join(student4);

        assertEquals(
                "Student-1 -> Student-2 -> Student-3 -> Student-4",
                captureOutput(cohort::print)
        );
    }

    @Test
    void rejoiningStudentMovesThemToTheEnd() {
        Cohort cohort = new Cohort();

        StudentId student1 = new StudentId("Student-1");
        StudentId student2 = new StudentId("Student-2");
        StudentId student3 = new StudentId("Student-3");
        StudentId student4 = new StudentId("Student-4");

        cohort.join(student1);
        cohort.join(student2);
        cohort.join(student3);
        cohort.join(student4);

        cohort.leave(student1);
        cohort.join(student1);

        assertEquals(
                "Student-2 -> Student-3 -> Student-4 -> Student-1",
                captureOutput(cohort::print)
        );
    }

    @Test
    void sizeReturnsNumberOfStudents() {
        Cohort cohort = new Cohort();

        cohort.join(new StudentId("Student-1"));
        cohort.join(new StudentId("Student-2"));
        cohort.join(new StudentId("Student-3"));
        cohort.join(new StudentId("Student-4"));

        assertEquals(4, cohort.size());
    }

    @Test
    void joinRejectsDuplicateLogicalStudentId() {
        Cohort cohort = new Cohort();

        cohort.join(new StudentId("Student-1"));

        StudentId duplicateStudent1 =
                new StudentId("Student-1");

        assertThrows(
                IllegalStateException.class,
                () -> cohort.join(duplicateStudent1)
        );
    }

    @Test
    void joinRejectsNullStudent() {
        Cohort cohort = new Cohort();

        assertThrows(
                IllegalArgumentException.class,
                () -> cohort.join(null)
        );
    }

    @Test
    void leaveRemovesStudent() {
        Cohort cohort = new Cohort();

        StudentId student1 = new StudentId("Student-1");
        StudentId student2 = new StudentId("Student-2");
        StudentId student3 = new StudentId("Student-3");
        StudentId student4 = new StudentId("Student-4");

        cohort.join(student1);
        cohort.join(student2);
        cohort.join(student3);
        cohort.join(student4);

        cohort.leave(student1);

        assertDoesNotThrow(() -> cohort.join(student1));
        assertThrows(
                IllegalStateException.class,
                () -> cohort.join(student2)
        );
    }

    @Test
    void leavingSameStudentTwiceIsIdempotent() {
        Cohort cohort = new Cohort();

        StudentId student1 = new StudentId("Student-1");
        StudentId student2 = new StudentId("Student-2");
        StudentId student3 = new StudentId("Student-3");
        StudentId student4 = new StudentId("Student-4");

        cohort.join(student1);
        cohort.join(student2);
        cohort.join(student3);
        cohort.join(student4);

        cohort.leave(student1);
        cohort.leave(student1);

        assertEquals(3, cohort.size());
    }

    @Test
    void leaveRejectsNullStudent() {
        Cohort cohort = new Cohort();

        assertThrows(
                IllegalArgumentException.class,
                () -> cohort.leave(null)
        );
    }

    private String captureOutput(Runnable action) {
        PrintStream originalOut = System.out;

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            System.setOut(new PrintStream(output));

            action.run();

            return output.toString().trim();
        } finally {
            System.setOut(originalOut);
        }
    }
}