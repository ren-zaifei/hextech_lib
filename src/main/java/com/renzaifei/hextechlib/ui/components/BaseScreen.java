package com.renzaifei.hextechlib.ui.components;

import com.renzaifei.hextechlib.card.HCard;
import com.renzaifei.hextechlib.ui.GuiHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.List;

public abstract class BaseScreen extends Screen {
    private final List<HCard> cards;
    protected int selectedIndex = -1;
    private HorizontalScrollableGrid grid;

    private static final int CARD_SPACING = 12;
    private static final int MIN_MARGIN = 16;
    private static final int CONTENT_MARGIN_TOP = 52;

    protected BaseScreen(Component title, List<HCard> cards) {
        super(title);
        this.cards = List.copyOf(cards);
    }

    @Override
    protected void init() {
        if (grid != null) grid.clear();
        grid = null;
        if (cards.isEmpty()) return;
        int viewportWidth = Math.max(1, this.width - 2 * MIN_MARGIN);
        int viewportHeight = Math.max(1, this.height - CONTENT_MARGIN_TOP - 32);
        int cardWidth = Math.max(1, Math.min(112,
                (viewportWidth - 16 - CARD_SPACING * 2) / Math.min(3, cards.size())));
        int cardHeight = Math.max(1, Math.min(156, viewportHeight - 24));
        grid = new HorizontalScrollableGrid(
                this, cards,
                this::addWidget,
                this::removeWidget,
                MIN_MARGIN, CONTENT_MARGIN_TOP,
                viewportWidth, viewportHeight,
                cardWidth, cardHeight, CARD_SPACING);

        grid.init();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        drawCenteredText(guiGraphics, this.title, 15, 0xFFE9D5A5, Math.max(1, width - 128));
        drawCenteredText(guiGraphics, subtitle(), 32, 0xFF9CAFC7, Math.max(1, width - 32));
        if (width >= 220) {
            Component count = Component.translatable("gui.hextech_lib.cards", cards.size());
            guiGraphics.drawString(font, count, width - MIN_MARGIN - font.width(count), 17, 0xFF9CAFC7, false);
        }
        guiGraphics.fill(width / 2 - 28, 46, width / 2 + 28, 47, 0xFF58788F);
        if (cards.isEmpty()) {
            int panelWidth = Math.max(1, Math.min(248, width - 32));
            GuiHelper.fillBeveledPanel(guiGraphics, (width - panelWidth) / 2, height / 2 - 26, panelWidth, 66, 0xFF30415B);
            GuiHelper.fillBeveledPanel(guiGraphics, (width - panelWidth) / 2 + 1, height / 2 - 25, panelWidth - 2, 64, 0xFF131F31);
            drawCenteredText(guiGraphics, Component.translatable("gui.hextech_lib.error.empty"), height / 2 - 8, 0xFFE9D5A5, panelWidth - 16);
            drawCenteredText(guiGraphics, Component.translatable("gui.hextech_lib.empty.hint"), height / 2 + 12, 0xFF9CAFC7, panelWidth - 16);
        } else if (grid != null) {
            grid.renderCards(guiGraphics, mouseX, mouseY, partialTick);
            grid.renderScrollBar(guiGraphics);
        }
        drawCenteredText(guiGraphics, Component.translatable(cards.isEmpty()
                ? "gui.hextech_lib.footer.empty" : "gui.hextech_lib.footer"), height - 13, 0xFF8195AE, width - 24);
    }

    private void drawCenteredText(GuiGraphics graphics, Component text, int y, int color, int maxWidth) {
        String label = text.getString();
        if (font.width(label) > maxWidth) {
            label = font.plainSubstrByWidth(label, Math.max(0, maxWidth - font.width("…"))) + "…";
        }
        graphics.drawCenteredString(font, label, width / 2, y, color);
    }

    protected Component subtitle() {
        return Component.translatable("gui.hextech_lib.selection.subtitle");
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        GuiHelper.renderTransparentBackground(guiGraphics, width, height);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        boolean handled = super.keyPressed(keyCode, scanCode, modifiers);
        if (grid != null && (keyCode == 258 || keyCode >= 262 && keyCode <= 265)) {
            grid.revealFocused();
            return true;
        }
        return handled;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double deltaX, double deltaY) {
        return grid != null && grid.mouseScrolled(mouseX, mouseY, deltaX, deltaY) || super.mouseScrolled(mouseX, mouseY, deltaX, deltaY);
    }

    @Override
    public void removed() {
        if (grid != null) grid.clear();
        super.removed();
    }
}
