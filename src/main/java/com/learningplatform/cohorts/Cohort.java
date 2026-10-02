package com.learningplatform.cohorts;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public final class Cohort {
    private final Set<StudentId> roster;

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

    public void print() {
        Iterator<StudentId> iterator = roster.iterator();

        while (iterator.hasNext()) {
            System.out.print(iterator.next());

            if (iterator.hasNext()) {
                System.out.print(" -> ");
            }
        }

        System.out.println();
    }

    public int size() {
        return roster.size();
    }
}