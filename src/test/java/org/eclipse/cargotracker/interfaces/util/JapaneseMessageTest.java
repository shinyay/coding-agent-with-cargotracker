package org.eclipse.cargotracker.interfaces.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.Locale;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test to verify Japanese message bundle loading.
 */
public class JapaneseMessageTest {

    @Test
    public void testJapaneseMessagesExist() {
        // Test that Japanese message bundle can be loaded
        ResourceBundle japaneseBundle = ResourceBundle.getBundle(
            "org.eclipse.cargotracker.messages", Locale.JAPANESE);
        
        assertNotNull(japaneseBundle);
        
        // Test some key messages
        assertEquals("Eclipse Cargo Tracker", japaneseBundle.getString("app.title"));
        assertEquals("貨物追跡", japaneseBundle.getString("track.title"));
        assertEquals("ホーム", japaneseBundle.getString("nav.home"));
        assertEquals("について", japaneseBundle.getString("nav.about"));
    }

    @Test
    public void testEnglishMessagesExist() {
        // Test that English message bundle can be loaded
        ResourceBundle englishBundle = ResourceBundle.getBundle(
            "org.eclipse.cargotracker.messages", Locale.ENGLISH);
        
        assertNotNull(englishBundle);
        
        // Test some key messages
        assertEquals("Eclipse Cargo Tracker", englishBundle.getString("app.title"));
        assertEquals("Track Cargo", englishBundle.getString("track.title"));
        assertEquals("Home", englishBundle.getString("nav.home"));
        assertEquals("About", englishBundle.getString("nav.about"));
    }

    @Test
    public void testCargoStatusMessages() {
        ResourceBundle japaneseBundle = ResourceBundle.getBundle(
            "org.eclipse.cargotracker.messages", Locale.JAPANESE);
        
        // Test cargo status messages in Japanese
        assertEquals("未受領", japaneseBundle.getString("cargo.status.NOT_RECEIVED"));
        assertEquals("受取済み", japaneseBundle.getString("cargo.status.CLAIMED"));
        assertEquals("不明", japaneseBundle.getString("cargo.status.UNKNOWN"));
    }
}