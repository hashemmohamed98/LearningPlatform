package com.learningplatform.cohorts;

import java.util.*;

public final class Cohort {
    private final SequencedSet<StudentId> roster;
    private final static int  CAPACITY = 6;

    public Cohort() {
        roster = new LinkedHashSet<>();
    }

    public void join(StudentId studentId) {
        if (studentId == null) {
            throw new IllegalArgumentException("Student id cannot be null");
        }

        if (roster.contains(studentId)) {
            throw new IllegalStateException("Student is already in the roster: " + studentId);
        }

        if (roster.size() >= CAPACITY) {
            throw new CohortFullException(CAPACITY);
        }

        roster.add(studentId);
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
     * Returns an unmodifiable snapshot of the cohort roster.
     * <p>
     * The returned snapshot is independent of the cohort, so students who
     * join or leave the cohort after this method returns are not reflected
     * in the snapshot.
     * <p>
     * The snapshot preserves the order in which students joined the cohort.
     *
     * @return an unmodifiable snapshot of the cohort roster in join order
     */
    public SequencedSet<StudentId> getStudents() {
        return Collections.unmodifiableSequencedSet(new LinkedHashSet<>(roster));
    }

}