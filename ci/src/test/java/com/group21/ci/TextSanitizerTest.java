package com.group21.ci;

import static org.junit.Assert.*;

import org.junit.Test;

public class TextSanitizerTest {

    /*
     * Positive Case, for a valid input, no changes are expected
     */

    @Test
    public void testSanitizePositiveCase() {
        String input = "This is a test";
        String expected = "This is a test";
        assertEquals(expected, TextSanitizer.sanitize(input));
    }

    /*
     * Negative test, if all character are invalid, it returns an empty string
     */

    @Test
    public void testSanitizeNegativeCase() {
        String input = "!@#$%^&*()";
        String expected = "";
        assertEquals(expected, TextSanitizer.sanitize(input));
    }

    /*
     * Edge-case test, if an empty string is sent, it is expected to receive
     * an empty string too
     */

    @Test
    public void testSanitizeEdgeCase() {
        String input = "";
        String expected = "";
        assertEquals(expected, TextSanitizer.sanitize(input));
    }

    /*
     * Normal-case test, if a string contains some invalid characters, it is
     * expected to receive a sanitized string
     */

    @Test
    public void testSanitizeNormalCase() {
        String input = "hello @ world";
        String expected = "hello world";
        assertEquals(expected, TextSanitizer.sanitize(input));
    }

    /**
     * Invalid inout test for TextSanitizer. Should throw an exception if
     * provided a null string to sanitise.
     */
    @Test
    public void testNullString() {
        String input = null;
        assertThrows(
            IllegalArgumentException.class, 
            () -> { TextSanitizer.sanitize(input); }
        );
    }



}
