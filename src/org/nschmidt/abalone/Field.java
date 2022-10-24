package org.nschmidt.abalone;

import static org.nschmidt.abalone.Player.BLACK;
import static org.nschmidt.abalone.Player.EMPTY;
import static org.nschmidt.abalone.Player.WHITE;

import java.util.Arrays;

public class Field {
    
    private Player[] playerPiecesOnField = NO_PIECES;
    
    private Field() {
        // Hidden constructor
    }
    
    private static final Player[] NO_PIECES = new Player[0];
    
    public static final Field INITIAL_FIELD = initJuniorField();
    public static final int FIELD_HEIGHT = 7;
    public static final int FIELD_WIDTH = calculateWidth(FIELD_HEIGHT);
    public static final int FIELD_SIZE = calculateSize(FIELD_HEIGHT);
    public static final int PIECE_COUNT = 9;
    public static final int DIRECTION_COUNT = 6;
    
    public static final Field EMPTY_FIELD = new Field();
    
    private static Field initJuniorField() {
        Field state = new Field();
        for (int i = 0; i < PIECE_COUNT; i++) {
            state = populateField(state, i, BLACK);
            state = populateField(state, i + 28, WHITE);
        }
        
        return state;
    }
    
    private static int calculateWidth(int fieldHeight) {
        return (fieldHeight - 1) + fieldHeight;
    }
    
    private static int calculateSize(int fieldHeight) {
        final int start = (fieldHeight + 1) / 2;
        return sumFromTo(start, fieldHeight) + sumFromTo(start, fieldHeight - 1);
    }
    
    private static int sumFromTo(int from, int to) {
        return sumTo(to) - sumTo(from) + from;
    }
    
    private static int sumTo(int n) {
        // (n·(n+1)) / 2 = 1 + 2 + 3 + ... + n
        return (n * (n + 1)) / 2;
    }

    public static Field populateField(Field state, int fieldIndex, Player player) {
        Field result = new Field();
        Player[] playerPieces = state.playerPiecesOnField;
        Player[] newPlayerPiecesOnField = new Player[Math.max(playerPieces.length, fieldIndex + 1)];
        System.arraycopy(playerPieces, 0, newPlayerPiecesOnField, 0, playerPieces.length);
        newPlayerPiecesOnField[fieldIndex] = player;
        result.playerPiecesOnField = newPlayerPiecesOnField;
        return result;
    }
    
    public static Field move(Field state, Player player, int from, int to) {
        return populateField(populateField(state, to, player), from, EMPTY);
    }
    
    public static Player lookAtField(Field state, int fieldIndex) {
        Player[] playerPieces = state.playerPiecesOnField;
        if (fieldIndex >= playerPieces.length) {
            return EMPTY;
        }
        
        final Player result = playerPieces[fieldIndex];
        return result == null ? EMPTY : result;
    }
    
    public static Field of(Player[] playerPieces) {
        Field result = new Field();
        result.playerPiecesOnField = playerPieces;
        return result;
    }
    
    @Override
    public String toString() {
        return Arrays.toString(playerPiecesOnField);
    }
    
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Field other) && Arrays.equals(playerPiecesOnField, other.playerPiecesOnField);
    }
    
    @Override
    public int hashCode() {
        return Arrays.hashCode(playerPiecesOnField);
    }
    
    public void printField() {
        if (playerPiecesOnField.length < 38) {
            FieldPrinter.printJuniorFieldDelta(this, this);
        } else {
            FieldPrinter.printStandardFieldDelta(this, this);
        }
    }
    
    public void printFieldDelta(Field previousState) {
        if (playerPiecesOnField.length < 38) {
            FieldPrinter.printJuniorFieldDelta(this, previousState);
        } else {
            FieldPrinter.printStandardFieldDelta(this, previousState);
        }
    }
}
