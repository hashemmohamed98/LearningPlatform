package com.learningplatform.cohorts;

import com.learningplatform.common.DomainException;

public final class CohortFullException extends DomainException {

    private final int capacity;

    public CohortFullException(int capacity) {
        super("Cohort is full (capacity: " + capacity + ")");
        this.capacity = capacity;
    }

    public int capacity() {
        return capacity;
    }

}