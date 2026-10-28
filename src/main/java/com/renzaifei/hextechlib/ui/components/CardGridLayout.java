package com.renzaifei.hextechlib.ui.components;

public record CardGridLayout(int columns, int rows, int contentWidth, int startX, int startY, int maxScroll) {
    public static CardGridLayout calculate(int viewportWidth, int viewportHeight, int cardCount,
                                           int cardWidth, int cardHeight, int spacing) {
        if (viewportWidth < 1 || viewportHeight < 1 || cardWidth < 1 || cardHeight < 1
                || cardCount < 0 || spacing < 0) {
            throw new IllegalArgumentException("Invalid card grid dimensions");
        }
        if (cardCount == 0) return new CardGridLayout(0, 0, 0, 0, 0, 0);

        int visibleColumns = Math.max(1, (viewportWidth + spacing) / (cardWidth + spacing));
        int maxRows = Math.max(1, (viewportHeight + spacing) / (cardHeight + spacing));
        int rows = Math.min(maxRows, Math.ceilDiv(cardCount, visibleColumns));
        int columns = Math.ceilDiv(cardCount, rows);
        rows = Math.ceilDiv(cardCount, columns);
        int contentWidth = columns * cardWidth + (columns - 1) * spacing;
        int contentHeight = rows * cardHeight + (rows - 1) * spacing;
        return new CardGridLayout(columns, rows, contentWidth,
                Math.max(0, (viewportWidth - contentWidth) / 2),
                Math.max(0, (viewportHeight - contentHeight) / 2),
                Math.max(0, contentWidth - viewportWidth));
    }
}
