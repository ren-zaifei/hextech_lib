package com.renzaifei.hextechlib.ui;

import com.renzaifei.hextechlib.card.HCard;
import com.renzaifei.hextechlib.network.PackChooseCard;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

public class CardSelectionScreen extends Screen {
    private final List<HCard> cards;
    private int selectedIndex = -1;

    private static final int CARD_WIDTH = 90;
    private static final int CARD_HEIGHT = 130;
    private static final int CARD_SPACING = 20;
    private static final int MIN_MARGIN = 20;
    private static final int TITLE_MARGIN_TOP = 20;
    private static final int CONTENT_MARGIN_TOP = 60;

    public CardSelectionScreen(List<HCard> cards) {
        super(Component.translatable("gui.hextech_lib.selection.title"));
        this.cards = cards;
    }

    @Override
    protected void init() {}

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderTransparentBackground(guiGraphics);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, TITLE_MARGIN_TOP, 0xFFFFFF);

        if (cards.isEmpty()) {
            guiGraphics.drawCenteredString(this.font, Component.translatable("gui.hextech_lib.error.empty"), this.width / 2, this.height / 2, 0xFF0000);
            return;
        }

        LayoutInfo layout = calculateLayout();

        selectedIndex = -1;


        for (int i = 0; i < cards.size(); i++) {
            HCard card = cards.get(i);
            int row = i / layout.cardsPerRow;
            int col = i % layout.cardsPerRow;

            int x = layout.startX + col * (CARD_WIDTH + CARD_SPACING);
            int y = layout.startY + row * (CARD_HEIGHT + CARD_SPACING);

            renderCard(guiGraphics, card, x, y, mouseX, mouseY, i);
        }

        renderHoverTooltip(guiGraphics, mouseX, mouseY, layout);

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    private LayoutInfo calculateLayout() {
        LayoutInfo layout = new LayoutInfo();

        int availableWidth = this.width - 2 * MIN_MARGIN;
        layout.cardsPerRow = Math.max(1, (availableWidth + CARD_SPACING) / (CARD_WIDTH + CARD_SPACING));
        layout.cardsPerRow = Math.min(layout.cardsPerRow, cards.size());

        layout.rows = (cards.size() + layout.cardsPerRow - 1) / layout.cardsPerRow;

        int totalContentWidth = layout.cardsPerRow * CARD_WIDTH + (layout.cardsPerRow - 1) * CARD_SPACING;
        int totalContentHeight = layout.rows * CARD_HEIGHT + (layout.rows - 1) * CARD_SPACING;

        layout.startX = (this.width - totalContentWidth) / 2;

        int maxContentHeight = this.height - CONTENT_MARGIN_TOP - MIN_MARGIN;
        if (totalContentHeight <= maxContentHeight) {
            layout.startY = CONTENT_MARGIN_TOP;
        } else {
            layout.startY = CONTENT_MARGIN_TOP;
        }

        return layout;
    }

    private void renderCard(GuiGraphics guiGraphics, HCard card, int x, int y, int mouseX, int mouseY, int index) {
        boolean hovered = isMouseOverCard(x, y, mouseX, mouseY);
        if (hovered) selectedIndex = index;

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0, 0, 200);

        int bgColor = hovered ? 0xCC444444 : 0xBB333333;
        guiGraphics.fill(x, y, x + CARD_WIDTH, y + CARD_HEIGHT, bgColor);

        int borderColor = getRarityColor(card.rarity());
        if (hovered) borderColor = brighten(borderColor, 60);

        guiGraphics.fill(x, y, x + CARD_WIDTH, y + 2, borderColor); // 上
        guiGraphics.fill(x, y, x + 2, y + CARD_HEIGHT, borderColor); // 左
        guiGraphics.fill(x + CARD_WIDTH - 2, y, x + CARD_WIDTH, y + CARD_HEIGHT, borderColor); // 右
        guiGraphics.fill(x, y + CARD_HEIGHT - 2, x + CARD_WIDTH, y + CARD_HEIGHT, borderColor); // 下

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0, 0, 100);
        int iconX = x + (CARD_WIDTH - 16) / 2;
        int iconY = y + 15;
        guiGraphics.renderItem(card.icon(), iconX, iconY);
        guiGraphics.pose().popPose();

        int titleY = y + 40;
        guiGraphics.drawCenteredString(this.font, card.title(), x + CARD_WIDTH / 2, titleY, 0xFFFFFF);

        int rarityY = y + 55;
        guiGraphics.drawCenteredString(
                this.font,
                Component.translatable("gui.hextech_lib.selection.rarity." + card.rarity().name()),
                x + CARD_WIDTH / 2,
                rarityY,
                borderColor
        );

        if (hovered) {
            int selectY = y + 75;
            guiGraphics.drawCenteredString(this.font, Component.translatable("gui.hextech_lib.selection.choose"), x + CARD_WIDTH / 2, selectY, 0xFFFF00);
        }

        guiGraphics.pose().popPose();
    }

    private boolean isMouseOverCard(int cardX, int cardY, int mouseX, int mouseY) {
        return mouseX >= cardX && mouseX <= cardX + CARD_WIDTH &&
                mouseY >= cardY && mouseY <= cardY + CARD_HEIGHT;
    }

    private void renderHoverTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY, LayoutInfo layout) {
        if (selectedIndex != -1) {
            HCard hoveredCard = cards.get(selectedIndex);
            int row = selectedIndex / layout.cardsPerRow;
            int col = selectedIndex % layout.cardsPerRow;

            int x = layout.startX + col * (CARD_WIDTH + CARD_SPACING);
            int y = layout.startY + row * (CARD_HEIGHT + CARD_SPACING);

            if (isMouseOverCard(x, y, mouseX, mouseY)) {
                guiGraphics.pose().pushPose();
                guiGraphics.pose().translate(0, 0, 400);
                guiGraphics.renderTooltip(this.font, hoveredCard.description(), mouseX, mouseY);
                guiGraphics.pose().popPose();
            }
        }
    }

    public void renderTransparentBackground(GuiGraphics guiGraphics) {
        int topColor = 0xAA1A1A1A;
        int bottomColor = 0xBB0A0A0A;

        guiGraphics.fillGradient(0, 0, this.width, this.height, topColor, bottomColor);

        int midHeight = this.height / 2;
        guiGraphics.fillGradient(0, 0, this.width, midHeight, 0x991A1A1A, 0xAA0F0F0F);
        guiGraphics.fillGradient(0, midHeight, this.width, this.height, 0xAA0F0F0F, 0xBB050505);
    }


    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {}

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (cards.isEmpty() || selectedIndex == -1) {
            return super.mouseClicked(mouseX, mouseY, button);
        }
        HCard card = cards.get(selectedIndex);
        PacketDistributor.sendToServer(new PackChooseCard(1,card.id(),null));
        this.onClose();
        this.minecraft.getSoundManager().play(
                SimpleSoundInstance.forUI(SoundEvents.PLAYER_LEVELUP, 1.0F, 1.2F)
        );
        return true;
    }

    private int brighten(int color, int amount) {
        int r = Math.min(255, (color >> 16 & 255) + amount);
        int g = Math.min(255, (color >> 8 & 255) + amount);
        int b = Math.min(255, (color & 255) + amount);
        return 0xFF000000 | r << 16 | g << 8 | b;
    }

    private int getRarityColor(HCard.Rarity rarity) {
        return switch (rarity) {
            case COMMON -> 0xFFAAAAAA;
            case RARE -> 0xFF55FF55;
            case EPIC -> 0xFFFF55FF;
            case LEGENDARY -> 0xFFFFAA00;
        };
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private static class LayoutInfo {
        int cardsPerRow;
        int rows;
        int startX;
        int startY;
    }
}