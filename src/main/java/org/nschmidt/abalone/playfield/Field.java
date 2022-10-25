package org.nschmidt.abalone.playfield;

import static org.nschmidt.abalone.playfield.Player.BLACK;
import static org.nschmidt.abalone.playfield.Player.EMPTY;
import static org.nschmidt.abalone.playfield.Player.WHITE;

import java.util.Arrays;

public class Field {
    
    private Player[] playerPiecesOnField = NO_PIECES;
    
    private Field() {
        // Hidden constructor
    }
    
    private static final Player[] NO_PIECES = new Player[0];
    
    public static final int FIELD_HEIGHT = 7;
    public static final int PIECE_COUNT = initPieceCount(FIELD_HEIGHT);
    public static final Field INITIAL_FIELD = initField(FIELD_HEIGHT);
    public static final int FIELD_WIDTH = calculateWidth(FIELD_HEIGHT);
    public static final int FIELD_SIZE = calculateSize(FIELD_HEIGHT);
    public static final int DIRECTION_COUNT = 6;
    
    public static final Field EMPTY_FIELD = new Field();
    
    
    private static Field initField(int height) {
        return height == 7 ? initJuniorField() : initStandardField();
    }
    
    private static int initPieceCount(int height) {
        return height == 7 ? 9 : 14;
    }
    
    private static Field initJuniorField() {
        Field state = new Field();
        for (int i = 0; i < PIECE_COUNT; i++) {
            state = populateField(state, i, BLACK);
            state = populateField(state, i + 28, WHITE);
        }
        
        return state;
    }
    
    private static Field initStandardField() {
        Field state = new Field();
        state = populateField(state,  0, WHITE);
        state = populateField(state,  1, WHITE);
        state = populateField(state,  5, WHITE);
        state = populateField(state,  6, WHITE);
        state = populateField(state,  7, WHITE);
        state = populateField(state, 12, WHITE);
        state = populateField(state, 13, WHITE);
        
        state = populateField(state,  3, BLACK);
        state = populateField(state,  4, BLACK);
        state = populateField(state,  8, BLACK);
        state = populateField(state,  9, BLACK);
        state = populateField(state, 10, BLACK);
        state = populateField(state, 15, BLACK);
        state = populateField(state, 16, BLACK);
        
        state = populateField(state, 44, BLACK);
        state = populateField(state, 45, BLACK);
        state = populateField(state, 50, BLACK);
        state = populateField(state, 51, BLACK);
        state = populateField(state, 52, BLACK);
        state = populateField(state, 56, BLACK);
        state = populateField(state, 57, BLACK);
        
        state = populateField(state, 47, WHITE);
        state = populateField(state, 48, WHITE);
        state = populateField(state, 53, WHITE);
        state = populateField(state, 54, WHITE);
        state = populateField(state, 55, WHITE);
        state = populateField(state, 59, WHITE);
        state = populateField(state, 60, WHITE);
        
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
        if (playerPiecesOnField.length < 38) {
            return FieldPrinter.buildJuniorFieldDeltaString(this, this);
        } else {
            return FieldPrinter.buildStandardFieldDeltaString(this, this);
        }
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
        FieldPrinter.printField(this.toString());
    }
    
    public void printFieldDelta(Field previousState) {
        if (playerPiecesOnField.length < 38) {
            FieldPrinter.printField(FieldPrinter.buildJuniorFieldDeltaString(this, previousState));
        } else {
            FieldPrinter.printField(FieldPrinter.buildStandardFieldDeltaString(this, previousState));
        }
    }
}
