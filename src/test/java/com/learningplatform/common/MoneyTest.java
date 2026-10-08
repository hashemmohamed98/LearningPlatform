package com.learningplatform.common;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MoneyTest {

    @Test
    void twoAmountsWithDifferentDecimalsAreEqual() {
        Currency currency = Currency.getInstance("EGP");
        BigDecimal amount = new BigDecimal("10.0");
        BigDecimal otherAmount = new BigDecimal("10.00");
        Money money = new Money(amount, currency);
        Money money2 = new Money(otherAmount, currency);
        assertEquals(money,money2);
    }
    @Test
    void nullAmountIsRejected() {
        Currency currency = Currency.getInstance("EGP");

        assertThrows(
                IllegalArgumentException.class,
                () -> new Money(null, currency)
        );
    }

    @Test
    void nullCurrencyIsRejected() {
        BigDecimal amount = new BigDecimal("10.00");

        assertThrows(
                IllegalArgumentException.class,
                () -> new Money(amount, null)
        );
    }

    @Test
    void negativeAmountIsRejected() {
        Currency currency = Currency.getInstance("EGP");
        BigDecimal amount = new BigDecimal("-10.00");

        assertThrows(
                IllegalArgumentException.class,
                () -> new Money(amount, currency)
        );
    }

    @Test
    void plusAddsAmountsCorrectly() {
        Currency currency = Currency.getInstance("EGP");
        Money money = new Money(new BigDecimal("10.00"), currency);
        Money other = new Money(new BigDecimal("5.00"), currency);

        Money result = money.plus(other);

        assertEquals(new Money(new BigDecimal("15.00"), currency), result);
    }

    @Test
    void plusRejectsDifferentCurrency() {
        Money money = new Money(
                new BigDecimal("10.00"),
                Currency.getInstance("EGP")
        );

        Money other = new Money(
                new BigDecimal("5.00"),
                Currency.getInstance("USD")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> money.plus(other)
        );
    }

    @Test
    void plusLeavesBothOriginalsUnchanged() {
        Currency currency = Currency.getInstance("EGP");
        Money money = new Money(new BigDecimal("10.00"), currency);
        Money other = new Money(new BigDecimal("5.00"), currency);

        Money result = money.plus(other);

        assertEquals(new BigDecimal("10.00"), money.amount());
        assertEquals(new BigDecimal("5.00"), other.amount());
        assertEquals(new Money(new BigDecimal("15.00"), currency), result);
    }
    @Test
    void amountWithMoreThanTwoDecimalPlacesIsRejected() {
        Currency currency = Currency.getInstance("EGP");

        assertThrows(
                IllegalArgumentException.class,
                () -> new Money(new BigDecimal("10.005"), currency)
        );
    }

    @Test
    void amountIsNormalizedToCurrencyScale() {
        Currency currency = Currency.getInstance("EGP");
        Money money = new Money(new BigDecimal("10.0"), currency);

        assertEquals(new BigDecimal("10.00"), money.amount());
    }
}
