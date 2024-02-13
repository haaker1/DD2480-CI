package com.group21.ci;

public class TextSanitizer {

    /**
     * sanitize method
     * This method sanitizes the text by removing not allowed characters
     * before sending it to the repository.
     * Allowed characters: a-z, A-Z, numbers, - and _
     * 
     * @param text the text to be sanitized (throws an IllegalArgumentException if text is null)
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
