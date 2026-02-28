package com.renzaifei.hextechlib.ui;

import com.renzaifei.hextechlib.card.HCard;
import net.minecraft.client.gui.GuiGraphics;

public class GuiHelper {
    private GuiHelper() {}

    public static void renderTransparentBackground(GuiGraphics guiGraphics, int width, int height) {
        int topColor = 0xAA1A1A1A;
        int bottomColor = 0xBB0A0A0A;

        guiGraphics.fillGradient(0, 0, width, height, topColor, bottomColor);

        int midHeight = height / 2;
        guiGraphics.fillGradient(0, 0, width, midHeight, 0x991A1A1A, 0xAA0F0F0F);
        guiGraphics.fillGradient(0, midHeight, width, height, 0xAA0F0F0F, 0xBB050505);
    }

    public static int brighten(int color, int amount) {
        int r = Math.min(255, (color >> 16 & 255) + amount);
        int g = Math.min(255, (color >> 8 & 255) + amount);
        int b = Math.min(255, (color & 255) + amount);
        return 0xFF000000 | r << 16 | g << 8 | b;
    }

    public static int getRarityColor(HCard.Rarity rarity) {
        return switch (rarity) {
            case COMMON -> 0xFFAAAAAA;
            case RARE -> 0xFF55FF55;
            case EPIC -> 0xFFFF55FF;
            case LEGENDARY -> 0xFFFFAA00;
        };
    }

    public static LayoutInfo calculateCardLayout(int screenWidth, int screenHeight, int cardCount,
                                                 int cardWidth, int cardHeight, int spacing, int minMargin, int titleMargin, int contentMargin) {
        LayoutInfo layout = new LayoutInfo();

        int availableWidth = screenWidth - 2 * minMargin;
        layout.cardsPerRow = Math.max(1, (availableWidth + spacing) / (cardWidth + spacing));
        layout.cardsPerRow = Math.min(layout.cardsPerRow, cardCount);

        layout.rows = (cardCount + layout.cardsPerRow - 1) / layout.cardsPerRow;

        int totalContentWidth = layout.cardsPerRow * cardWidth + (layout.cardsPerRow - 1) * spacing;
        layout.startX = (screenWidth - totalContentWidth) / 2;

        int totalContentHeight = layout.rows * cardHeight + (layout.rows - 1) * spacing;
        int maxContentHeight = screenHeight - contentMargin - minMargin;

        layout.startY = (totalContentHeight <= maxContentHeight)
                ? contentMargin
                : contentMargin;

        return layout;
    }

    public static class LayoutInfo {
        public int cardsPerRow;
        public int rows;
        public int startX;
        public int startY;
    }
}
