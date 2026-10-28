package com.renzaifei.hextechlib.ui;

import com.renzaifei.hextechlib.card.HCard;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class GuiHelperTest {
    @Test
    void colorBlendIncludesAlphaAndClampsProgress() {
        assertEquals(0xFF808080, GuiHelper.blend(0xFF000000, 0xFFFFFFFF, 0.5));
        assertEquals(0x80000000, GuiHelper.blend(0x00000000, 0xFF000000, 0.5));
        assertEquals(0x12345678, GuiHelper.blend(0x12345678, 0xFFEEDDCC, -1));
        assertEquals(0xFFEEDDCC, GuiHelper.blend(0x12345678, 0xFFEEDDCC, 2));
    }

    @Test
    void brightnessCannotOverflowOrUnderflowChannels() {
        assertEquals(0xFFFFFFFF, GuiHelper.brighten(0xFFFFF0E0, 100));
        assertEquals(0xFF000000, GuiHelper.brighten(0xFF102030, -100));
        assertEquals(0xFF1A2A3A, GuiHelper.brighten(0xFF102030, 10));
    }

    @Test
    void everyRarityHasADistinctOpaqueAccent() {
        int[] colors = Arrays.stream(HCard.Rarity.values()).mapToInt(GuiHelper::getRarityColor).toArray();
        assertEquals(colors.length, Arrays.stream(colors).distinct().count());
        for (int color : colors) assertEquals(255, color >>> 24);
    }

    @Test
    void legacyLayoutHandlesEmptyPoolsWithoutDivisionByZero() {
        GuiHelper.LayoutInfo layout = GuiHelper.calculateCardLayout(320, 240, 0, 90, 130, 20, 20, 20, 60);
        assertEquals(0, layout.cardsPerRow);
        assertEquals(0, layout.rows);
        assertEquals(160, layout.startX);
        assertEquals(60, layout.startY);
    }

    @Test
    void legacyLayoutStillWrapsCardsAndReservesTitleSpace() {
        GuiHelper.LayoutInfo layout = GuiHelper.calculateCardLayout(320, 240, 5, 90, 130, 20, 20, 20, 60);
        assertEquals(2, layout.cardsPerRow);
        assertEquals(3, layout.rows);
        assertEquals(60, layout.startX);
        assertEquals(60, layout.startY);
    }
}
