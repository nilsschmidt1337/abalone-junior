package org.nschmidt.abalone.playfield;

import static org.nschmidt.abalone.playfield.Field.lookAtField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum FieldPrinter {
    INSTANCE;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(FieldPrinter.class);
    
    public static String buildJuniorFieldDeltaString(Field state, Field previousState) {
        final StringBuilder sb = new StringBuilder();
        sb.append(String.format("%n      G  %s%s%s%s", charsAt(state, previousState, 0), charsAt(state, previousState, 1), charsAt(state, previousState, 2), charsAt(state, previousState, 3)));
        sb.append(String.format("%n     F  %s%s%s%s%s", charsAt(state, previousState, 4), charsAt(state, previousState, 5), charsAt(state, previousState, 6), charsAt(state, previousState, 7), charsAt(state, previousState, 8)));
        sb.append(String.format("%n    E  %s%s%s%s%s%s", charsAt(state, previousState, 9), charsAt(state, previousState, 10), charsAt(state, previousState, 11), charsAt(state, previousState, 12), charsAt(state, previousState, 13), charsAt(state, previousState, 14)));
        sb.append(String.format("%n   D  %s%s%s%s%s%s%s", charsAt(state, previousState, 15), charsAt(state, previousState, 16), charsAt(state, previousState, 17), charsAt(state, previousState, 18), charsAt(state, previousState, 19), charsAt(state, previousState, 20), charsAt(state, previousState, 21)));
        sb.append(String.format("%n    C  %s%s%s%s%s%s7", charsAt(state, previousState, 22), charsAt(state, previousState, 23), charsAt(state, previousState, 24), charsAt(state, previousState, 25), charsAt(state, previousState, 26), charsAt(state, previousState, 27)));
        sb.append(String.format("%n     B  %s%s%s%s%s6", charsAt(state, previousState, 28), charsAt(state, previousState, 29), charsAt(state, previousState, 30), charsAt(state, previousState, 31), charsAt(state, previousState, 32)));
        sb.append(String.format("%n      A  %s%s%s%s5", charsAt(state, previousState, 33), charsAt(state, previousState, 34), charsAt(state, previousState, 35), charsAt(state, previousState, 36)));
        sb.append(String.format("%n          1 2 3 4%n%n"));
        return sb.toString();
    }
    
    public static String buildStandardFieldDeltaString(Field state, Field previousState) {
        final StringBuilder sb = new StringBuilder();
        sb.append(String.format("%n       I  %s%s%s%s%s", charsAt(state, previousState, 0), charsAt(state, previousState, 1), charsAt(state, previousState, 2), charsAt(state, previousState, 3), charsAt(state, previousState, 4)));
        sb.append(String.format("%n      H  %s%s%s%s%s%s", charsAt(state, previousState, 5), charsAt(state, previousState, 6), charsAt(state, previousState, 7), charsAt(state, previousState, 8), charsAt(state, previousState, 9), charsAt(state, previousState, 10)));
        sb.append(String.format("%n     G  %s%s%s%s%s%s%s", charsAt(state, previousState, 11), charsAt(state, previousState, 12), charsAt(state, previousState, 13), charsAt(state, previousState, 14), charsAt(state, previousState, 15), charsAt(state, previousState, 16), charsAt(state, previousState, 17)));
        sb.append(String.format("%n    F  %s%s%s%s%s%s%s%s", charsAt(state, previousState, 18), charsAt(state, previousState, 19), charsAt(state, previousState, 20), charsAt(state, previousState, 21), charsAt(state, previousState, 22), charsAt(state, previousState, 23), charsAt(state, previousState, 24), charsAt(state, previousState, 25)));
        sb.append(String.format("%n   E  %s%s%s%s%s%s%s%s%s", charsAt(state, previousState, 26), charsAt(state, previousState, 27), charsAt(state, previousState, 28), charsAt(state, previousState, 29), charsAt(state, previousState, 30), charsAt(state, previousState, 31), charsAt(state, previousState, 32), charsAt(state, previousState, 33), charsAt(state, previousState, 34)));
        sb.append(String.format("%n    D  %s%s%s%s%s%s%s%s9", charsAt(state, previousState, 35), charsAt(state, previousState, 36), charsAt(state, previousState, 37), charsAt(state, previousState, 38), charsAt(state, previousState, 39), charsAt(state, previousState, 40), charsAt(state, previousState, 41), charsAt(state, previousState, 42)));
        sb.append(String.format("%n     C  %s%s%s%s%s%s%s8", charsAt(state, previousState, 43), charsAt(state, previousState, 44), charsAt(state, previousState, 45), charsAt(state, previousState, 46), charsAt(state, previousState, 47), charsAt(state, previousState, 48), charsAt(state, previousState, 49)));
        sb.append(String.format("%n      B  %s%s%s%s%s%s7", charsAt(state, previousState, 50), charsAt(state, previousState, 51), charsAt(state, previousState, 52), charsAt(state, previousState, 53), charsAt(state, previousState, 54), charsAt(state, previousState, 55)));
        sb.append(String.format("%n       A  %s%s%s%s%s6", charsAt(state, previousState, 56), charsAt(state, previousState, 57), charsAt(state, previousState, 58), charsAt(state, previousState, 59), charsAt(state, previousState, 60)));
        sb.append(String.format("%n           1 2 3 4 5%n%n"));
        return sb.toString();
    }
    
    public static void printField(String fieldString) {
        LOGGER.info(fieldString);
    }
    
    private static String charsAt(Field state, Field previousState, int index) {
        final char fieldValue = lookAtField(state, index).getChar();
        final char previousFieldValue = lookAtField(previousState, index).getChar();
        return fieldValue + (fieldValue == previousFieldValue ? " " : "_");
    }
}
