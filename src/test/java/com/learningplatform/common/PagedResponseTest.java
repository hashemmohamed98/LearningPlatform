package com.learningplatform.common;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PagedResponseTest {

    @Test
    void validPageNumberZeroIsAccepted() {
        List<String> items = List.of("item1", "item2");

        assertDoesNotThrow(() -> new PagedResponse<>(items, 0, 2, 10));
    }

    @Test
    void negativePageNumberIsRejected() {
        List<String> items = List.of("item1");

        assertThrows(IllegalArgumentException.class, () -> new PagedResponse<>(items, -1, 2, 1));
    }

    @Test
    void zeroPageSizeIsRejected() {
        List<String> items = List.of("item1");

        assertThrows(IllegalArgumentException.class, () -> new PagedResponse<>(items, 0, 0, 1));
    }

    @Test
    void negativePageSizeIsRejected() {
        List<String> items = List.of("item1");

        assertThrows(IllegalArgumentException.class, () -> new PagedResponse<>(items, 0, -1, 1));
    }

    @Test
    void negativeTotalElementsIsRejected() {
        List<String> items = List.of("item1", "item2");

        assertThrows(IllegalArgumentException.class, () -> new PagedResponse<>(items, 0, 2, -1));
    }

    @Test
    void nullItemsListIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new PagedResponse<String>(null, 0, 2, 0));
    }

    @Test
    void nullElementInItemsIsRejected() {
        List<String> items = new ArrayList<>();
        items.add("item1");
        items.add(null);

        assertThrows(IllegalArgumentException.class, () -> new PagedResponse<>(items, 0, 2, 2));
    }

    @Test
    void moreItemsThanPageSizeIsRejected() {
        List<String> items = List.of("item1", "item2", "item3");

        assertThrows(IllegalArgumentException.class, () -> new PagedResponse<>(items, 0, 2, 10));
    }

    @Test
    void moreItemsThanTotalElementsIsAccepted() {
        List<String> items = List.of("item1");

        assertDoesNotThrow(() -> new PagedResponse<>(items, 0, 2, 0));
    }

    @Test
    void modifyingOriginalListDoesNotChangeResponseItems() {
        List<String> items = new ArrayList<>();
        items.add("item1");

        PagedResponse<String> response = new PagedResponse<>(items, 0, 2, 1);

        items.add("item2");

        assertEquals(List.of("item1"), response.items());
    }

    @Test
    void responseItemsCannotBeModified() {
        PagedResponse<String> response = new PagedResponse<>(List.of("item1"), 0, 2, 1);

        assertThrows(UnsupportedOperationException.class, () -> response.items().add("item2"));
    }

    @Test
    void totalPagesIsZeroWhenTotalElementsIsZero() {
        PagedResponse<String> response = new PagedResponse<>(List.of(), 0, 20, 0);

        assertEquals(0, response.totalPages());
    }

    @Test
    void totalPagesIsOneWhenThereIsOneElement() {
        PagedResponse<String> response = new PagedResponse<>(List.of("item1"), 0, 20, 1);

        assertEquals(1, response.totalPages());
    }

    @Test
    void totalPagesIsOneWhenElementsExactlyFillOnePage() {
        PagedResponse<String> response = new PagedResponse<>(List.of(), 0, 20, 20);

        assertEquals(1, response.totalPages());
    }

    @Test
    void totalPagesRoundsUpWhenLastPageIsPartiallyFilled() {
        PagedResponse<String> response = new PagedResponse<>(List.of(), 0, 20, 21);

        assertEquals(2, response.totalPages());
    }

    @Test
    void totalPagesIsCorrectForMultipleFullPages() {
        PagedResponse<String> response = new PagedResponse<>(List.of(), 0, 20, 100);

        assertEquals(5, response.totalPages());
    }

    @Test
    void totalPagesHandlesLargeTotalElements() {
        PagedResponse<String> response = new PagedResponse<>(List.of(), 0, 1, (long) Integer.MAX_VALUE + 1);

        assertEquals((long) Integer.MAX_VALUE + 1, response.totalPages());
    }

    @Test
    void totalPagesHandlesLongMaxValue() {
        PagedResponse<String> response = new PagedResponse<>(List.of(), 0, 3, Long.MAX_VALUE);

        assertEquals(3074457345618258603L, response.totalPages());
    }
}