package com.renzaifei.hextechlib.ui;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class UiTranslationsTest {
    @ParameterizedTest
    @ValueSource(strings = {"en_us", "zh_cn", "zh_tw"})
    void allLocalesHaveMatchingNonEmptyKeysAndCountPlaceholder(String locale) throws IOException {
        JsonObject reference = readLocale("en_us");
        JsonObject translations = readLocale(locale);
        assertEquals(reference.keySet(), translations.keySet());
        for (String key : reference.keySet()) {
            assertFalse(translations.get(key).getAsString().isBlank(), locale + ": " + key);
        }
        assertTrue(translations.get("gui.hextech_lib.cards").getAsString().contains("%s"));
    }

    private JsonObject readLocale(String locale) throws IOException {
        try (InputStream stream = getClass().getResourceAsStream("/assets/hextech_lib/lang/" + locale + ".json")) {
            assertNotNull(stream, "Missing locale: " + locale);
            return JsonParser.parseReader(new InputStreamReader(stream, StandardCharsets.UTF_8)).getAsJsonObject();
        }
    }
}
