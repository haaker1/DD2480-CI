package com.group21.ci;

/**
 * A class for sanitizing text.
 */
public class TextSanitizer {

    /**
     * Sanitizes  text by removing illegal characters
     * Allowed characters: a-z, A-Z, numbers, - and _
     * @param text a string to be sanitized 
     * @throws IllegalArgumentException if text is null
     * @return the sanitized string
     */
    public static String sanitize(String text) {
        if (text == null) {
            throw new IllegalArgumentException("Input cannot be null");
        }

        // May create double spaces because of character removal
        String sanitizedText = text.replaceAll("[^a-zA-Z0-9-_\\s]", "");
        // Deletes double spaces introduced above
        sanitizedText = sanitizedText.replaceAll("\\s+", " ");
        return sanitizedText;
    }
}
