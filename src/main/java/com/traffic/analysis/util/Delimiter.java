package com.traffic.analysis.util;

/**
 * Represent a CSV file delimiter.
 */
public class Delimiter {
    private final String name;
    private final char delimiterChar;

    /**
     * Constructor.
     * @param name The name of the character.
     * @param delimiterChar The character itself.
     */
    public Delimiter(String name, char delimiterChar) {
        this.name = name;
        this.delimiterChar = delimiterChar;
    }

    /**
     * @return The delimiter character.
     */
    public char getDelimiterChar() {
        return delimiterChar;
    }

    /**
     * @return String representation of the delimiter character.
     */
    @Override
    public String toString(){
        return this.name.toUpperCase() + "(" + this.delimiterChar + ")";
    }
}
