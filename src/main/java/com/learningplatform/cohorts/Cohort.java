package com.learningplatform.cohorts;

import java.util.*;

/**
 * Represents a cohort with a thread-safe in-memory student roster.
 *
 * <p>All access to the roster is guarded by the roster monitor
 * (synchronized on the roster instance). This ensures thread safety
 * between multiple threads accessing the same Cohort instance.
 *
 * <p>Read operations must use the same monitor because:
 * <ul>
 *     <li>{@code getStudents()}: Creating a snapshot while another thread
 *     modifies the LinkedHashSet may result in inconsistent data or a
 *     {@link java.util.ConcurrentModificationException}.</li>
 * </ul>
 *
 * <p>This synchronization does not protect against concurrent modifications
 * through multiple Cohort instances representing the same cohort, or across
 * multiple JVMs. Those scenarios require database-level locking to ensure
 * consistency.
 */

public final class Cohort {
    private final SequencedSet<StudentId> roster;
    private static final int CAPACITY = 6;

    public Cohort() {
        roster = new LinkedHashSet<>();
    }

    public void join(StudentId studentId) {
        if (studentId == null) {
            throw new IllegalArgumentException("Student id cannot be null");
        }

        synchronized (roster) {
            if (roster.contains(studentId)) {
                throw new IllegalStateException("Student is already in the roster: " + studentId);
            }

            if (roster.size() >= CAPACITY) {
                throw new CohortFullException(CAPACITY);
            }
            roster.add(studentId);
        }
    }

    public void leave(StudentId studentId) {
        if (studentId == null) {
            throw new IllegalArgumentException("Student id cannot be null");
        }

        synchronized (roster) {
            roster.remove(studentId);
        }

    }

    public int removeStudents(Collection<StudentId> students) {
        if (students == null) {
            throw new IllegalArgumentException("Students collection cannot be null");
        }
        synchronized (roster) {
            int originalSize = roster.size();
            roster.removeAll(students);

            return originalSize - roster.size();
        }
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
        synchronized (roster) {
            return Collections.unmodifiableSequencedSet(new LinkedHashSet<>(roster));
        }
    }

}