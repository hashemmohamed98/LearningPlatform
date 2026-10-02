package com.learningplatform.cohorts;

public final class StudentId {
    private final String value;

    public StudentId(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Student id cannot be null or blank");
        }

        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudentId other)) return false;
        return value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value;
    }
}