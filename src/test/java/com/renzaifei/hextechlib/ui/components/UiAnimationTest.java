package com.renzaifei.hextechlib.ui.components;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class UiAnimationTest {
    @ParameterizedTest
    @CsvSource({"-10, 0, 0", "0, 0, 0", "140, 0, 0.875", "280, 0, 1", "44, 1, 0",
            "45, 1, 0", "185, 1, 0.875", "325, 1, 1", "640, 1000, 1", "140, -1, 0.875"})
    void entranceIsStaggeredClampedAndBounded(double elapsed, int index, double expected) {
        assertEquals(expected, UiAnimation.entranceProgress(elapsed, index), 1.0E-9);
    }

    @ParameterizedTest
    @ValueSource(ints = {30, 60, 144})
    void smoothingIsIndependentOfFrameRate(int frameRate) {
        double value = 0.0;
        int frames = frameRate / 6;
        for (int frame = 0; frame < frames; frame++) {
            value = UiAnimation.approach(value, 1.0, 1.0 / frameRate, 12.0);
        }
        assertEquals(UiAnimation.approach(0.0, 1.0, (double) frames / frameRate, 12.0), value, 1.0E-12);
    }

    @Test
    void hoverCanReverseWithoutJumpingOrOvershooting() {
        double hovered = UiAnimation.approach(0.0, 1.0, 0.05, 14.0);
        double leaving = UiAnimation.approach(hovered, 0.0, 0.05, 14.0);
        assertTrue(hovered > 0.0 && hovered < 1.0);
        assertTrue(leaving > 0.0 && leaving < hovered);
        assertEquals(1.0, UiAnimation.approach(hovered, 1.0, 10.0, 14.0));
        assertEquals(0.0, UiAnimation.approach(leaving, 0.0, 10.0, 14.0));
    }

    @Test
    void nonPositiveTimeOrSpeedDoesNotAdvanceAnimation() {
        assertEquals(0.3, UiAnimation.approach(0.3, 1.0, 0.0, 14.0));
        assertEquals(0.3, UiAnimation.approach(0.3, 1.0, -1.0, 14.0));
        assertEquals(0.3, UiAnimation.approach(0.3, 1.0, 1.0, 0.0));
    }
}
