package com.renzaifei.hextechlib.ui.components;

import com.renzaifei.hextechlib.card.HCard;
import com.renzaifei.hextechlib.ui.CardControlScreen;
import com.renzaifei.hextechlib.ui.CardSelectionScreen;
import com.renzaifei.hextechlib.ui.GuiHelper;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class CardWidget extends AbstractWidget {

    private final HCard card;
    private final Screen parentScreen;
    private final int index;

    public CardWidget(int x, int y, int width, int height, HCard card, Screen parentScreen,int index) {
        super(x, y, width, height,Component.empty());
        this.card = card;
        this.index = index;
        this.parentScreen = parentScreen;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int i, int i1, float v) {
        boolean hovered = isHovered();
        int bgColor = hovered ? 0xCC444444 : 0xBB333333;
        guiGraphics.fill(getX(), getY(), getX() + width, getY() + height, bgColor);
        int borderColor = GuiHelper.getRarityColor(card.getRarity());
        if (hovered) borderColor = GuiHelper.brighten(borderColor, 60);
        guiGraphics.fill(getX(), getY(), getX() + width, getY() + 2, borderColor);
        guiGraphics.fill(getX(), getY(), getX() + 2, getY() + height, borderColor);
        guiGraphics.fill(getX() + width - 2, getY(), getX() + width, getY() + height, borderColor);
        guiGraphics.fill(getX(), getY() + height - 2, getX() + width, getY() + height, borderColor);

        int iconX = getX() + (width - 16) / 2;
        int iconY = getY() + 15;
        guiGraphics.renderItem(card.getIcon(), iconX, iconY);

        Font font = parentScreen.getMinecraft().font;

        int titleY = getY() + 40;
        guiGraphics.drawCenteredString(font, card.title(), getX() + width / 2, titleY, 0xFFFFFF);

        int rarityY = getY() + 55;
        guiGraphics.drawCenteredString(
                font,
                Component.translatable("gui.hextech_lib.selection.rarity." + card.getRarity().name()),
                getX() + width / 2,
                rarityY,
                borderColor
        );
        if (hovered) {
            if (parentScreen instanceof CardSelectionScreen) {
                int selectY = getY() + 75;
                guiGraphics.drawCenteredString(font,
                        Component.translatable("gui.hextech_lib.selection.choose"),
                        getX() + width / 2, selectY, 0xFFFF00);
            }
            if (parentScreen instanceof CardControlScreen){
                int selectY = getY() + 75;
                guiGraphics.drawCenteredString(font,
                        Component.translatable("gui.hextech_lib.control.choose"),
                        getX() + width / 2, selectY, 0xFF0000);
            }
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        defaultButtonNarrationText(narrationElementOutput);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isMouseOver(mouseX, mouseY)) {
            if (parentScreen instanceof CardSelectionScreen) {
                ((CardSelectionScreen) parentScreen).onCardClicked(index, card);
            }
            if (parentScreen instanceof CardControlScreen) {
                ((CardControlScreen) parentScreen).onCardClicked(index, card);
            }
            return true;
        }
        return false;
    }

    public Component getDescription() {
        return card.description();
    }
}
