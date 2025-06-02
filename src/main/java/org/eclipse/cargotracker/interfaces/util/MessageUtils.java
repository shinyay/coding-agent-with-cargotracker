package org.eclipse.cargotracker.interfaces.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import jakarta.faces.context.FacesContext;

/**
 * Utility class for accessing internationalized messages.
 */
public class MessageUtils {

    private static final String BUNDLE_NAME = "org.eclipse.cargotracker.messages";

    /**
     * Get a message from the resource bundle.
     * 
     * @param key the message key
     * @return the localized message
     */
    public static String getMessage(String key) {
        return getMessage(key, (Object[]) null);
    }

    /**
     * Get a formatted message from the resource bundle.
     * 
     * @param key the message key
     * @param params the parameters for message formatting
     * @return the localized and formatted message
     */
    public static String getMessage(String key, Object... params) {
        Locale locale = getCurrentLocale();
        ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME, locale);
        String message = bundle.getString(key);
        
        if (params != null && params.length > 0) {
            return MessageFormat.format(message, params);
        }
        
        return message;
    }

    /**
     * Get the current locale from JSF context or default to Japanese.
     * 
     * @return the current locale
     */
    private static Locale getCurrentLocale() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context != null) {
            return context.getViewRoot().getLocale();
        }
        return Locale.JAPANESE; // Default to Japanese
    }
}