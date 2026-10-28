package com.renzaifei.hextechlib.ui.components;

import com.renzaifei.hextechlib.card.HCard;
import com.renzaifei.hextechlib.ui.CardControlScreen;
import com.renzaifei.hextechlib.ui.CardSelectionScreen;
import com.renzaifei.hextechlib.ui.GuiHelper;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.time.Duration;
import java.util.List;

public class CardWidget extends AbstractButton {

    private final HCard card;
    private final Screen parentScreen;
    private final int index;
    private final long createdAt = System.nanoTime();
    private double hoverProgress;
    private int viewportX = Integer.MIN_VALUE;
    private int viewportY = Integer.MIN_VALUE;
    private int viewportRight = Integer.MAX_VALUE;
    private int viewportBottom = Integer.MAX_VALUE;

    public CardWidget(int x, int y, int width, int height, HCard card, Screen parentScreen, int index) {
        super(x, y, width, height, card.title());
        this.card = card;
        this.index = index;
        this.parentScreen = parentScreen;
        setTooltip(Tooltip.create(Component.empty().append(card.title()).append("\n").append(card.description())));
        setTooltipDelay(Duration.ofMillis(250));
    }

    public void setViewport(int x, int y, int width, int height) {
        viewportX = x;
        viewportY = y;
        viewportRight = x + width;
        viewportBottom = y + height;
    }

    public void updateAnimation(int x, int y, long now, double seconds, int mouseX, int mouseY) {
        setX(x);
        boolean highlighted = active && (isMouseOver(mouseX, mouseY) || isFocused());
        hoverProgress = UiAnimation.approach(hoverProgress, highlighted ? 1.0 : 0.0, seconds, 14.0);
        double entrance = UiAnimation.entranceProgress((now - createdAt) / 1_000_000.0, index);
        setY(y + (int) Math.round((1.0 - entrance) * 12.0 - hoverProgress * 4.0));
        isHovered = isMouseOver(mouseX, mouseY);
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Font font = parentScreen.getMinecraft().font;
        int rarityColor = GuiHelper.getRarityColor(card.getRarity());
        int borderColor = GuiHelper.blend(0xFF35445D, rarityColor, 0.55 + hoverProgress * 0.45);
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(getX(), getY(), 0);
        guiGraphics.pose().scale(width / 112.0F, height / 156.0F, 1.0F);
        try {
            GuiHelper.fillBeveledPanel(guiGraphics, -2, 4, 116, 156, 0x65000000);
            GuiHelper.fillBeveledPanel(guiGraphics, 0, 0, 112, 156, borderColor);
            GuiHelper.fillBeveledPanel(guiGraphics, 1, 1, 110, 154, 0xFF101827);
            guiGraphics.fillGradient(3, 3, 109, 68,
                    GuiHelper.blend(0xFF1D2B40, rarityColor, 0.12 + hoverProgress * 0.12), 0xFF101827);
            guiGraphics.fill(12, 2, 100, 3, GuiHelper.blend(rarityColor, 0xFFFFFFFF, hoverProgress * 0.4));
            guiGraphics.drawCenteredString(font, Component.translatable(rarityKey()), 56, 11, rarityColor);

            GuiHelper.fillBeveledPanel(guiGraphics, 38, 28, 36, 36,
                    GuiHelper.blend(0xFF30415A, rarityColor, hoverProgress * 0.5));
            GuiHelper.fillBeveledPanel(guiGraphics, 39, 29, 34, 34, 0xFF0B1220);
            guiGraphics.pose().pushPose();
            try {
                guiGraphics.pose().translate(44, 34, 0);
                guiGraphics.pose().scale(1.5F, 1.5F, 1.0F);
                guiGraphics.renderItem(card.getIcon(), 0, 0);
            } finally {
                guiGraphics.pose().popPose();
            }

            drawLines(guiGraphics, font, card.title(), 72, 2, 0xFFF0F4FC);
            guiGraphics.fill(18, 96, 94, 97, 0xFF2A3850);
            drawLines(guiGraphics, font, card.description(), 102, 2, 0xFFADBBD0);

            int actionColor = parentScreen instanceof CardControlScreen ? 0xFFFF8F8F : rarityColor;
            GuiHelper.fillBeveledPanel(guiGraphics, 9, 133, 94, 16,
                    GuiHelper.blend(0xFF1B293D, actionColor, hoverProgress * 0.25));
            drawLines(guiGraphics, font, Component.translatable(parentScreen instanceof CardControlScreen
                    ? "gui.hextech_lib.control.choose" : "gui.hextech_lib.selection.choose"),
                    137, 1, GuiHelper.blend(0xFF9EADC2, actionColor, hoverProgress));
            if (isFocused()) {
                guiGraphics.renderOutline(3, 3, 106, 150, 0xFFE9F5FF);
            }
        } finally {
            guiGraphics.pose().popPose();
        }
    }

    private void drawLines(GuiGraphics graphics, Font font, Component text, int y, int maxLines, int color) {
        List<FormattedCharSequence> lines = font.split(text, 94);
        for (int line = 0; line < Math.min(maxLines, lines.size()); line++) {
            graphics.drawCenteredString(font, lines.get(line), 56, y + line * font.lineHeight, color);
        }
        if (lines.size() > maxLines) graphics.drawString(font, "…", 99, y + (maxLines - 1) * font.lineHeight, color, false);
    }

    private String rarityKey() {
        return "gui.hextech_lib.selection.rarity." + card.getRarity().name();
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        defaultButtonNarrationText(narrationElementOutput);
        narrationElementOutput.add(NarratedElementType.HINT,
                Component.translatable(rarityKey()).append(". ").append(card.description()));
    }

    @Override
    public void onPress() {
        if (!active || !visible) return;
        if (parentScreen instanceof CardSelectionScreen selectionScreen) {
            selectionScreen.onCardClicked(index, card);
        } else if (parentScreen instanceof CardControlScreen controlScreen) {
            controlScreen.onCardClicked(index, card);
        }
    }

    @Override
    protected boolean clicked(double mouseX, double mouseY) {
        return isMouseOver(mouseX, mouseY);
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return mouseX >= viewportX && mouseX < viewportRight && mouseY >= viewportY && mouseY < viewportBottom
                && super.isMouseOver(mouseX, mouseY);
    }

    public Component getDescription() {
        return card.description();
    }
}
