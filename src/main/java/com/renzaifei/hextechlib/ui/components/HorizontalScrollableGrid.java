package com.renzaifei.hextechlib.ui.components;

import com.renzaifei.hextechlib.card.HCard;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class HorizontalScrollableGrid {

    private final Screen parentScreen;
    private final List<HCard> cards;
    private final List<CardWidget> cardWidgets = new ArrayList<>();

    private final Consumer<AbstractWidget> addWidget;
    private final Consumer<AbstractWidget> removeWidget;

    private double scrollAmount = 0.0;
    private int maxScroll = 0;
    private int virtualStartX = 0;
    private int cardsPerRow = 0;

    private final int viewportX, viewportY, viewportWidth, viewportHeight;
    private final int cardWidth, cardHeight, cardSpacing;

    public HorizontalScrollableGrid(Screen parentScreen, List<HCard> cards,
                                    Consumer<AbstractWidget> addWidget,
                                    Consumer<AbstractWidget> removeWidget,
                                    int viewportX, int viewportY, int viewportWidth, int viewportHeight,
                                    int cardWidth, int cardHeight, int cardSpacing) {
        this.parentScreen = parentScreen;
        this.cards = cards;
        this.addWidget = addWidget;
        this.removeWidget = removeWidget;
        this.viewportX = viewportX;
        this.viewportY = viewportY;
        this.viewportWidth = viewportWidth;
        this.viewportHeight = viewportHeight;
        this.cardWidth = cardWidth;
        this.cardHeight = cardHeight;
        this.cardSpacing = cardSpacing;
    }

    public void init() {
        cardWidgets.clear();
        if (cards.isEmpty()) return;

        int availableHeight = viewportHeight;
        int rowHeight = cardHeight + cardSpacing;
        int maxRows = Math.max(1, availableHeight / rowHeight);

        cardsPerRow = (int) Math.ceil((double) cards.size() / maxRows);

        int totalContentWidth = cardsPerRow * cardWidth + (cardsPerRow - 1) * cardSpacing;
        if (totalContentWidth <= viewportWidth) {
            virtualStartX = viewportX + (viewportWidth - totalContentWidth) / 2;
        } else {
            virtualStartX = viewportX;
        }

        for (int i = 0; i < cards.size(); i++) {
            int row = i / cardsPerRow;
            int col = i % cardsPerRow;

            int x = virtualStartX + col * (cardWidth + cardSpacing);
            int y = viewportY + row * rowHeight;

            CardWidget widget = new CardWidget(x, y, cardWidth, cardHeight, cards.get(i), parentScreen,i);
            cardWidgets.add(widget);
            addWidget.accept(widget);
        }

        int visibleWidth = viewportWidth;
        maxScroll = Math.max(0, totalContentWidth - visibleWidth);

        updatePositions();
    }

    private void updatePositions() {
        for (int i = 0; i < cardWidgets.size(); i++) {
            int col = i % cardsPerRow;
            int originalX = virtualStartX + col * (cardWidth + cardSpacing);
            cardWidgets.get(i).setX((int) (originalX - scrollAmount));
        }
    }

    public void renderScrollBar(GuiGraphics guiGraphics) {
        if (maxScroll <= 0) return;

        int barWidth = (int) ((double) viewportWidth / (maxScroll + viewportWidth) * viewportWidth);
        int barX = viewportX + (int) (scrollAmount / maxScroll * (viewportWidth - barWidth));

        guiGraphics.fill(viewportX, viewportY + viewportHeight + 4,
                viewportX + viewportWidth, viewportY + viewportHeight + 10, 0x40000000);
        guiGraphics.fill(barX, viewportY + viewportHeight + 4,
                barX + barWidth, viewportY + viewportHeight + 10, 0x80FFFFFF);
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double deltaX, double deltaY) {
        if (maxScroll <= 0) return false;
        double old = scrollAmount;
        scrollAmount -= deltaY * 35;
        scrollAmount = Math.max(0, Math.min(scrollAmount, maxScroll));
        if (old != scrollAmount) {
            updatePositions();
            return true;
        }
        return false;
    }

    public void clear() {
        cardWidgets.forEach(removeWidget::accept);
        cardWidgets.clear();
    }
}