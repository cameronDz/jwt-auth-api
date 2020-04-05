package org.md.api.auth.utility;

public class GeneralUtility {
    
    /**
     * check if a string is null or empty
     * @param value string being checked
     * @return boolean true if null or empty, false otherwise
     */
    public static boolean isNullOrEmpty(String value) {
        return value == null || "".equals(value.trim());
    }
}
