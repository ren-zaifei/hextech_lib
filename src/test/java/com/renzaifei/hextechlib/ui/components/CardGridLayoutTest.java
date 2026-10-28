package com.renzaifei.hextechlib.ui.components;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class CardGridLayoutTest {
    @Test
    void emptyGridHasNoRowsOrScroll() {
        assertEquals(new CardGridLayout(0, 0, 0, 0, 0, 0),
                CardGridLayout.calculate(300, 200, 0, 90, 130, 12));
    }

    @Test
    void threeChoicesStayInOneCenteredRow() {
        assertEquals(new CardGridLayout(3, 1, 294, 3, 35, 0),
                CardGridLayout.calculate(300, 200, 3, 90, 130, 12));
    }

    @Test
    void exactHeightFitUsesAllAvailableRowsIncludingLastSpacing() {
        assertEquals(new CardGridLayout(3, 2, 294, 3, 0, 0),
                CardGridLayout.calculate(300, 272, 6, 90, 130, 12));
    }

    @Test
    void overflowAndIncompleteRowsRemainReachable() {
        assertEquals(new CardGridLayout(4, 2, 396, 0, 0, 96),
                CardGridLayout.calculate(300, 272, 7, 90, 130, 12));
        assertEquals(new CardGridLayout(6, 1, 600, 0, 0, 300),
                CardGridLayout.calculate(300, 130, 6, 90, 130, 12));
    }

    @Test
    void singleCardIsCenteredInBothDirections() {
        assertEquals(new CardGridLayout(1, 1, 112, 104, 42, 0),
                CardGridLayout.calculate(320, 240, 1, 112, 156, 12));
    }

    @Test
    void layoutsStayBoundedAcrossScreenSizesAndCardCounts() {
        for (int viewportWidth : new int[]{1, 90, 300, 640}) {
            for (int viewportHeight : new int[]{1, 130, 272, 580}) {
                for (int count = 1; count <= 128; count++) {
                    CardGridLayout layout = CardGridLayout.calculate(viewportWidth, viewportHeight, count, 90, 130, 12);
                    assertTrue(layout.columns() > 0);
                    assertEquals(Math.ceilDiv(count, layout.columns()), layout.rows());
                    assertTrue(layout.startX() >= 0 && layout.startY() >= 0);
                    assertTrue(layout.contentWidth() - layout.maxScroll() <= viewportWidth);
                    if (viewportHeight >= 130) {
                        assertTrue(layout.startY() + layout.rows() * 130 + (layout.rows() - 1) * 12 <= viewportHeight);
                    }
                }
            }
        }
    }

    @ParameterizedTest
    @CsvSource({"0, 200, 1, 90, 130, 12", "300, 0, 1, 90, 130, 12", "300, 200, -1, 90, 130, 12",
            "300, 200, 1, 0, 130, 12", "300, 200, 1, 90, 0, 12", "300, 200, 1, 90, 130, -1"})
    void invalidDimensionsAreRejected(int width, int height, int count, int cardWidth, int cardHeight, int spacing) {
        assertThrows(IllegalArgumentException.class,
                () -> CardGridLayout.calculate(width, height, count, cardWidth, cardHeight, spacing));
    }
}
