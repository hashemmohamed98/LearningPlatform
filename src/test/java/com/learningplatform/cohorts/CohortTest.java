package com.learningplatform.cohorts;
import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
                List.of(student1, student2, student3, student4),
                List.copyOf(cohort.getStudents())
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
                List.of(student2, student3, student4, student1),
                List.copyOf(cohort.getStudents())
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

    @Test
    void removeStudentsReturnsNumberRemovedAndUpdatesSize() {
        Cohort cohort = new Cohort();

        StudentId student1 = new StudentId("Student-1");
        StudentId student2 = new StudentId("Student-2");
        StudentId student3 = new StudentId("Student-3");
        StudentId student4 = new StudentId("Student-4");

        cohort.join(student1);
        cohort.join(student2);
        cohort.join(student3);
        cohort.join(student4);

        List<StudentId> studentsToRemove = new ArrayList<>();
        studentsToRemove.add(student1);
        studentsToRemove.add(student3);

        int removed = cohort.removeStudents(studentsToRemove);

        assertEquals(2, removed);
        assertEquals(2, cohort.size());
    }
    @Test
    void removeLastStudentReturnsNumberRemovedAndUpdatesSize() {
        Cohort cohort = new Cohort();

        StudentId student1 = new StudentId("Student-1");
        StudentId student2 = new StudentId("Student-2");
        StudentId student3 = new StudentId("Student-3");
        StudentId student4 = new StudentId("Student-4");

        cohort.join(student1);
        cohort.join(student2);
        cohort.join(student3);
        cohort.join(student4);

        List<StudentId> studentsToRemove = new ArrayList<>();
        studentsToRemove.add(student4);

        int removed = cohort.removeStudents(studentsToRemove);

        assertEquals(1, removed);
        assertEquals(3, cohort.size());
    }
    @Test
    void removeStudentThatDoesNotExistReturnsZeroAndLeavesSizeUnchanged() {
        Cohort cohort = new Cohort();

        StudentId student1 = new StudentId("Student-1");
        StudentId student2 = new StudentId("Student-2");
        StudentId student3 = new StudentId("Student-3");
        StudentId student4 = new StudentId("Student-4");
        StudentId student5 = new StudentId("Student-5");

        cohort.join(student1);
        cohort.join(student2);
        cohort.join(student3);
        cohort.join(student4);

        List<StudentId> studentsToRemove = new ArrayList<>();
        studentsToRemove.add(student5);

        int removed = cohort.removeStudents(studentsToRemove);

        assertEquals(0, removed);
        assertEquals(4, cohort.size());
    }
    @Test
    void removeStudentsRejectsNullCollection() {
        Cohort cohort = new Cohort();

        assertThrows(
                IllegalArgumentException.class,
                () -> cohort.removeStudents(null)
        );
    }

    @Test
    void addingToReturnedRosterDoesNotChangeCohort() {
        Cohort cohort = new Cohort();
        StudentId student1 = new StudentId("Student-1");
        StudentId student2 = new StudentId("Student-2");
        cohort.join(student1);
        cohort.join(student2);
       int size =  cohort.getStudents().size();
        StudentId student3 = new StudentId("Student-3");
        Set<StudentId> students = cohort.getStudents();
        assertThrows(
                UnsupportedOperationException.class,
                () -> students.add(student3));

        assertEquals(size, cohort.size());
    }

    @Test
    void rosterSnapshotDoesNotChangeAfterLaterJoin() {
        Cohort cohort = new Cohort();
        StudentId student1 = new StudentId("Student-1");
        Set<StudentId> students = cohort.getStudents();
        cohort.join(student1);
        assertEquals(0, students.size());
    }

    @Test
    void joinRejectsSeventhStudent() {
        Cohort cohort = new Cohort();

        for (int i = 1; i <= 6; i++) {
            cohort.join(new StudentId("Student-" + i));
        }

        StudentId student7 = new StudentId("Student-7");

        CohortFullException ex = assertThrows(
                CohortFullException.class,
                () -> cohort.join(student7)
        );

        assertEquals(6, ex.capacity());

        assertEquals(
                List.of(
                        new StudentId("Student-1"),
                        new StudentId("Student-2"),
                        new StudentId("Student-3"),
                        new StudentId("Student-4"),
                        new StudentId("Student-5"),
                        new StudentId("Student-6")
                ),
                List.copyOf(cohort.getStudents())
        );
    }

    @Test
    void joinRejectsDuplicateInFullCohortAsAlreadyInRoster() {
        Cohort cohort = new Cohort();

        for (int i = 1; i <= 6; i++) {
            cohort.join(new StudentId("Student-" + i));
        }

        StudentId duplicate = new StudentId("Student-3");

        assertThrows(
                IllegalStateException.class,
                () -> cohort.join(duplicate)
        );
    }

}