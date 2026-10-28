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

    private static final int PADDING = 8;
    private CardGridLayout layout;
    private SmoothScroll scroll = new SmoothScroll(0);
    private long lastFrame;

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
        clear();
        if (cards.isEmpty()) return;
        layout = CardGridLayout.calculate(contentViewportWidth(), Math.max(1, viewportHeight - PADDING * 2),
                cards.size(), cardWidth, cardHeight, cardSpacing);
        scroll = new SmoothScroll(layout.maxScroll());
        lastFrame = System.nanoTime();

        for (int i = 0; i < cards.size(); i++) {
            int x = viewportX + PADDING + layout.startX() + i % layout.columns() * (cardWidth + cardSpacing);
            int y = viewportY + PADDING + layout.startY() + i / layout.columns() * (cardHeight + cardSpacing);
            CardWidget widget = new CardWidget(x, y, cardWidth, cardHeight, cards.get(i), parentScreen, i);
            widget.setViewport(viewportX, viewportY, viewportWidth, viewportHeight);
            cardWidgets.add(widget);
            addWidget.accept(widget);
        }
    }

    public void renderCards(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        if (layout == null) return;
        long now = System.nanoTime();
        double seconds = (now - lastFrame) / 1_000_000_000.0;
        lastFrame = now;
        scroll.update(seconds);
        graphics.enableScissor(viewportX, viewportY, viewportX + viewportWidth, viewportY + viewportHeight);
        try {
            updatePositions(now, seconds, mouseX, mouseY);
            for (CardWidget widget : cardWidgets) {
                if (widget.getRight() + 3 > viewportX && widget.getX() - 3 < viewportX + viewportWidth) {
                    widget.render(graphics, mouseX, mouseY, partialTick);
                }
            }
        } finally {
            graphics.disableScissor();
        }
    }

    private void updatePositions(long now, double seconds, int mouseX, int mouseY) {
        for (int i = 0; i < cardWidgets.size(); i++) {
            int x = viewportX + PADDING + layout.startX() + i % layout.columns() * (cardWidth + cardSpacing)
                    - (int) Math.round(scroll.position());
            int y = viewportY + PADDING + layout.startY() + i / layout.columns() * (cardHeight + cardSpacing);
            cardWidgets.get(i).updateAnimation(x, y, now, seconds, mouseX, mouseY);
        }
    }

    public void revealFocused() {
        if (layout == null) return;
        for (int i = 0; i < cardWidgets.size(); i++) {
            if (cardWidgets.get(i).isFocused()) {
                int left = layout.startX() + i % layout.columns() * (cardWidth + cardSpacing);
                scroll.reveal(left, left + cardWidth, contentViewportWidth());
                return;
            }
        }
    }

    private int contentViewportWidth() {
        return Math.max(1, viewportWidth - PADDING * 2);
    }

    public void renderScrollBar(GuiGraphics guiGraphics) {
        if (layout == null || layout.maxScroll() <= 0) return;

        int trackWidth = contentViewportWidth();
        int barWidth = Math.min(trackWidth, Math.max(24, trackWidth * trackWidth / layout.contentWidth()));
        int barX = viewportX + PADDING + (int) Math.round(scroll.position() / layout.maxScroll() * (trackWidth - barWidth));

        guiGraphics.fill(viewportX + PADDING, viewportY + viewportHeight + 4,
                viewportX + PADDING + trackWidth, viewportY + viewportHeight + 7, 0xFF26364D);
        guiGraphics.fill(barX, viewportY + viewportHeight + 4,
                barX + barWidth, viewportY + viewportHeight + 7, 0xFF94B8CE);
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double deltaX, double deltaY) {
        if (layout == null || mouseX < viewportX || mouseX >= viewportX + viewportWidth
                || mouseY < viewportY || mouseY >= viewportY + viewportHeight) return false;
        return scroll.scrollBy(-(deltaX != 0.0 ? deltaX : deltaY) * 35.0);
    }

    public void clear() {
        cardWidgets.forEach(removeWidget::accept);
        cardWidgets.clear();
        layout = null;
        scroll = new SmoothScroll(0);
    }
}
