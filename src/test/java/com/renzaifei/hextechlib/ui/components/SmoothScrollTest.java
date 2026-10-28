package com.renzaifei.hextechlib.ui.components;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SmoothScrollTest {
    @Test
    void wheelInputIsClampedAtBothEdges() {
        SmoothScroll scroll = new SmoothScroll(200);
        assertFalse(scroll.scrollBy(-35));
        assertTrue(scroll.scrollBy(1000));
        assertEquals(200, scroll.target());
        assertFalse(scroll.scrollBy(35));
        assertTrue(scroll.scrollBy(-1000));
        assertEquals(0, scroll.target());
    }

    @Test
    void rapidInputAccumulatesAgainstTargetNotAnimatedPosition() {
        SmoothScroll scroll = new SmoothScroll(200);
        scroll.scrollBy(35);
        scroll.scrollBy(35);
        assertEquals(70, scroll.target());
        assertEquals(0, scroll.position());
        scroll.update(0.016);
        assertTrue(scroll.position() > 0 && scroll.position() < 70);
        scroll.update(1.0);
        assertEquals(70, scroll.position());
    }

    @Test
    void reversingDirectionNeverOvershootsTheScrollBounds() {
        SmoothScroll scroll = new SmoothScroll(200);
        scroll.scrollBy(200);
        scroll.update(0.1);
        double previous = scroll.position();
        scroll.scrollBy(-200);
        scroll.update(0.1);
        assertTrue(scroll.position() >= 0 && scroll.position() < previous);
    }

    @Test
    void keyboardFocusRevealsOnlyTheNecessaryDistance() {
        SmoothScroll scroll = new SmoothScroll(500);
        scroll.reveal(350, 440, 300);
        assertEquals(140, scroll.target());
        scroll.reveal(200, 290, 300);
        assertEquals(140, scroll.target());
        scroll.reveal(20, 110, 300);
        assertEquals(20, scroll.target());
        scroll.reveal(900, 990, 300);
        assertEquals(500, scroll.target());
        scroll.reveal(-20, 70, 300);
        assertEquals(0, scroll.target());
    }

    @Test
    void nonScrollableContentIgnoresAllMovement() {
        SmoothScroll scroll = new SmoothScroll(-10);
        assertFalse(scroll.scrollBy(35));
        scroll.reveal(300, 390, 200);
        scroll.update(1.0);
        assertEquals(0, scroll.position());
        assertEquals(0, scroll.target());
    }
}
