package com.renzaifei.hextechlib.ui;

import com.renzaifei.hextechlib.card.HCard;
import net.minecraft.client.gui.GuiGraphics;

public class GuiHelper {
    private GuiHelper() {}

    public static void renderTransparentBackground(GuiGraphics guiGraphics, int width, int height) {
        guiGraphics.fillGradient(0, 0, width, height, 0xE5101829, 0xF0060B14);
        guiGraphics.fillGradient(0, 0, width, height / 2, 0x244885A0, 0x004885A0);
        guiGraphics.fill(0, 0, width, 1, 0xFF557A91);
    }

    public static int brighten(int color, int amount) {
        int r = Math.clamp((color >> 16 & 255) + amount, 0, 255);
        int g = Math.clamp((color >> 8 & 255) + amount, 0, 255);
        int b = Math.clamp((color & 255) + amount, 0, 255);
        return 0xFF000000 | r << 16 | g << 8 | b;
    }

    public static int blend(int from, int to, double progress) {
        double fraction = Math.clamp(progress, 0.0, 1.0);
        int color = 0;
        for (int shift = 0; shift <= 24; shift += 8) {
            int start = from >>> shift & 255;
            int end = to >>> shift & 255;
            color |= (int) Math.round(start + (end - start) * fraction) << shift;
        }
        return color;
    }

    public static void fillBeveledPanel(GuiGraphics graphics, int x, int y, int width, int height, int color) {
        if (width <= 0 || height <= 0) return;
        int corner = Math.min(3, Math.min(width, height) / 2);
        graphics.fill(x + corner, y, x + width - corner, y + height, color);
        graphics.fill(x, y + corner, x + width, y + height - corner, color);
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

        if (cardCount < 0 || cardWidth < 1 || cardHeight < 1 || spacing < 0) {
            throw new IllegalArgumentException("Invalid card layout dimensions");
        }
        layout.startY = Math.max(titleMargin + 9, contentMargin);
        if (cardCount == 0) {
            layout.startX = screenWidth / 2;
            return layout;
        }

        int availableWidth = screenWidth - 2 * minMargin;
        layout.cardsPerRow = Math.max(1, (availableWidth + spacing) / (cardWidth + spacing));
        layout.cardsPerRow = Math.min(layout.cardsPerRow, cardCount);

        layout.rows = (cardCount + layout.cardsPerRow - 1) / layout.cardsPerRow;

        int totalContentWidth = layout.cardsPerRow * cardWidth + (layout.cardsPerRow - 1) * spacing;
        layout.startX = (screenWidth - totalContentWidth) / 2;

        return layout;
    }

    public static class LayoutInfo {
        public int cardsPerRow;
        public int rows;
        public int startX;
        public int startY;
    }
}
