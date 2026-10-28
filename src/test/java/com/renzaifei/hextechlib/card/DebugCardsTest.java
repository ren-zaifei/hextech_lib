package com.renzaifei.hextechlib.card;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class DebugCardsTest {
    @ParameterizedTest
    @EnumSource(HCard.Rarity.class)
    void everyRarityHasExactlyThreePreviewCards(HCard.Rarity rarity) {
        assertEquals(3, DebugCards.definitions().stream().filter(definition -> definition.rarity() == rarity).count());
    }

    @Test
    void twelveCardsHaveUniqueNamespacedPathsAndDistinctIcons() {
        assertEquals(12, DebugCards.definitions().size());
        assertEquals(12, DebugCards.definitions().stream().map(DebugCards.Definition::id).distinct().count());
        assertEquals(12, DebugCards.definitions().stream().map(DebugCards.Definition::icon).distinct().count());
        for (DebugCards.Definition definition : DebugCards.definitions()) {
            assertTrue(definition.id().startsWith("test_"));
            assertTrue(definition.id().matches("[a-z0-9_]+"));
            assertTrue(definition.icon().matches("[a-z0-9_]+"));
        }
    }

    @Test
    void catalogCannotBeMutated() {
        assertThrows(UnsupportedOperationException.class, () -> DebugCards.definitions().clear());
    }

    @ParameterizedTest
    @ValueSource(strings = {"en_us", "zh_cn", "zh_tw"})
    void everyPreviewCardHasALocalizedTitleAndDescription(String locale) throws IOException {
        try (InputStream stream = getClass().getResourceAsStream("/assets/hextech_lib/lang/" + locale + ".json")) {
            assertNotNull(stream);
            JsonObject translations = JsonParser.parseReader(new InputStreamReader(stream, StandardCharsets.UTF_8)).getAsJsonObject();
            for (DebugCards.Definition definition : DebugCards.definitions()) {
                assertTrue(translations.has(definition.titleKey()), locale + ": " + definition.titleKey());
                assertTrue(translations.has(definition.descriptionKey()), locale + ": " + definition.descriptionKey());
                assertFalse(translations.get(definition.titleKey()).getAsString().isBlank());
                assertFalse(translations.get(definition.descriptionKey()).getAsString().isBlank());
            }
        }
    }
}
