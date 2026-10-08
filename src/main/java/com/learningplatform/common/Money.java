package com.learningplatform.common;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

public record Money(BigDecimal amount, Currency currency) {

    public Money {
        if (amount == null || currency == null) {
            throw new IllegalArgumentException("Amount and currency are mandatory");
        }

        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }

        try {
            amount = amount.setScale(
                    currency.getDefaultFractionDigits(),
                    RoundingMode.UNNECESSARY
            );
        } catch (ArithmeticException e) {
            throw new IllegalArgumentException(
                    "Amount has too many decimal places. Amount:"+amount+" currency:"+currency,
                    e
            );
        }
    }

    public Money plus(Money other) {
        if (other == null) {
            throw new IllegalArgumentException("Money is null");
        }

        if (!currency.equals(other.currency)) {
            throw new IllegalArgumentException(
                    "Currency mismatch: " + currency.getCurrencyCode()
                            + " vs " + other.currency.getCurrencyCode()
            );
        }

        return new Money(amount.add(other.amount), currency);
    }
}

