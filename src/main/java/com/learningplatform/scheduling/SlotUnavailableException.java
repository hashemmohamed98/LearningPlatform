package com.learningplatform.scheduling;

import com.learningplatform.common.DomainException;

import java.util.Objects;

public final class SlotUnavailableException extends DomainException {
    private final TimeSlot slot;

    public SlotUnavailableException(TimeSlot slot) {
        super("Time slot is unavailable: " +
                Objects.requireNonNull(slot, "slot cannot be null"));
        this.slot = slot;
    }

    public TimeSlot slot() {
        return slot;
    }
}
