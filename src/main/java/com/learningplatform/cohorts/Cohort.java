package com.learningplatform.cohorts;

import java.util.*;

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