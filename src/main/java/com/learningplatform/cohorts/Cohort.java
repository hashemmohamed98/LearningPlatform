package com.learningplatform.cohorts;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.SequencedSet;

public final class Cohort {
    private final SequencedSet<StudentId> roster;

    public Cohort() {
        roster = new LinkedHashSet<>();
    }

    public void join(StudentId studentId) {
        if (studentId == null) {
            throw new IllegalArgumentException("Student id cannot be null");
        }

        if (!roster.add(studentId)) {
            throw new IllegalStateException("Student is already in the roster: " + studentId);
        }
    }

    public void leave(StudentId studentId) {
        if (studentId == null) {
            throw new IllegalArgumentException("Student id cannot be null");
        }

        roster.remove(studentId);
    }

    public int size() {
        return roster.size();
    }

    public int removeStudents(Collection<StudentId> students) {
        if (students == null) {
            throw new IllegalArgumentException("Students collection cannot be null");
        }

        int originalSize = roster.size();
        roster.removeAll(students);

        return originalSize - roster.size();
    }

    /**
     * Returns a read-only live view of the cohort roster.
     * Changes made through the cohort are reflected in the view.
     * The cohort must not be modified while the view is being iterated.
     */
    public SequencedSet<StudentId> getStudents() {
        return Collections.unmodifiableSequencedSet(roster);
    }

}