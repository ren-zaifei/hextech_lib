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

    private static final int CARD_WIDTH = 90;
    private static final int CARD_HEIGHT = 130;
    private static final int CARD_SPACING = 20;
    private static final int MIN_MARGIN = 20;
    private static final int TITLE_MARGIN_TOP = 20;
    private static final int CONTENT_MARGIN_TOP = 60;

    protected BaseScreen(Component title,List<HCard> cards) {
        super(title);
        this.cards = cards;
    }

    @Override
    protected void init() {
        if (cards.isEmpty()) return;
        grid = new HorizontalScrollableGrid(
                this, cards,
                this::addRenderableWidget,
                this::removeWidget,
                MIN_MARGIN, CONTENT_MARGIN_TOP,
                this.width - 2 * MIN_MARGIN,
                this.height - CONTENT_MARGIN_TOP - MIN_MARGIN,
                CARD_WIDTH, CARD_HEIGHT, CARD_SPACING);

        grid.init();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        GuiHelper.renderTransparentBackground(guiGraphics, this.width, this.height);
        if (cards.isEmpty()) {
            guiGraphics.drawCenteredString(this.font, Component.translatable("gui.hextech_lib.error.empty"), this.width / 2, this.height / 2, 0xFF0000);
            return;
        }
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, TITLE_MARGIN_TOP, 0xFFFFFF);
        grid.renderScrollBar(guiGraphics);
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
