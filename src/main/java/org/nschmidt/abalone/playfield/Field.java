package org.nschmidt.abalone.playfield;

import static org.nschmidt.abalone.playfield.Player.BLACK;
import static org.nschmidt.abalone.playfield.Player.EMPTY;
import static org.nschmidt.abalone.playfield.Player.WHITE;

import java.util.Objects;

public class Field {
    
    protected long black = 0L;
    protected long white = 0L;
    
    protected Field() {
        // Hidden constructor
    }
    
    public long getBlack() {
        return black;
    }

    public long getWhite() {
        return white;
    }

    public static final int FIELD_HEIGHT = 7;
    public static final int PIECE_COUNT = initPieceCount(FIELD_HEIGHT);
    public static final int FIELD_WIDTH = calculateWidth(FIELD_HEIGHT);
    public static final int FIELD_SIZE = calculateSize(FIELD_HEIGHT);
    public static final Field INITIAL_FIELD = initField(FIELD_HEIGHT);
    public static final int DIRECTION_COUNT = 6;
    public static final int PIECE_COUNT_FOR_WIN = 6;
    public static final int PIECE_COUNT_FOR_WIN_MINUS_ONE = PIECE_COUNT_FOR_WIN - 1;
    
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
            state = populateField(state, i, WHITE);
            state = populateField(state, i + 28, BLACK);
        }
        
        return state;
    }
    
    private static Field populateInDaisyFormation(Field state, int pos, Player player) {
        state = populateField(state,  pos, player);
        for (int i : Adjacency.adjacency(pos)) {
            state = populateField(state,  i, player);
        }
        
        return state;
    }
    
    private static Field initStandardField() {
        Field state = new Field();
        state = populateInDaisyFormation(state,  6, WHITE);
        state = populateInDaisyFormation(state,  9, BLACK);
        state = populateInDaisyFormation(state, 51, BLACK);
        state = populateInDaisyFormation(state, 54, WHITE);
        
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
        long op = 1L << fieldIndex;
        long opNeg = ~op;
        if (player == WHITE) {
            result.white = state.white | op;
            result.black = state.black & opNeg;
        } else if (player == BLACK) {
            result.black = state.black | op;
            result.white = state.white & opNeg;
        } else {
            result.white = state.white & opNeg;
            result.black = state.black & opNeg;
        }
        
        
        return result;
    }
    
    public static Field move(Field state, Player player, int from, int to) {
        Field result = new Field();
        if (player == BLACK) {
            result.black = (state.black | (1L << to)) & ~(1L << from);
            result.white = state.white;
        } else {
            result.black = state.black;
            result.white = (state.white | (1L << to)) & ~(1L << from);
        }
        
        return result;
    }
    
    public static Field override(Field state, Player player, int from, int to) {
        Field result = new Field();
        long opTo = 1L << to;
        long opToNeg = ~opTo;
       
        if (player == BLACK) {
            result.black = (state.black | opTo) & ~(1L << from);
            result.white = state.white & opToNeg;
        } else {
            result.white = (state.white | opTo) & ~(1L << from);
            result.black = state.black & opToNeg;
        }
        
        return result;
    }
    
    public static Player lookAtField(Field state, int fieldIndex) {
        long opBlack = (1L << fieldIndex) & state.black;
        if (opBlack > 0) return BLACK;
        long opWhite = (1L << fieldIndex) & state.white;
        if (opWhite > 0) return WHITE;
        return EMPTY;
    }
    
    public static boolean isNotEmpty(Field state, int fieldIndex) {
        return (((1L << fieldIndex) & state.black) | ((1L << fieldIndex) & state.white)) > 0;
    }
    
    public static int countPieces(Field state, Player player) {
        int result = 0;
        for (int i = 0; i < 64; i++) {
            if (lookAtField(state, i) == player) {
                result++;
            }
        }
        
        return result;
    }
    
    public static Field of(Player[] playerPieces) {
        Field result = new Field();
        for (int i = 0; i < playerPieces.length; i++) {
            Player player = playerPieces[i];
            result = populateField(result, i, player);
        }
        return result;
    }
    
    public static Field of(long black, long white) {
        Field result = new Field();
        result.black = black;
        result.white = white;
        return result;
    }
    
    @Override
    public String toString() {
        if (FIELD_SIZE < 38) {
            return FieldPrinter.buildJuniorFieldDeltaString(this, this);
        } else {
            return FieldPrinter.buildStandardFieldDeltaString(this, this);
        }
    }
    
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Field other) && black == other.black && white == other.white;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(black, white);
    }
    
    public void printField() {
        FieldPrinter.printField(this.toString());
    }
    
    public void printFieldDelta(Field previousState) {
        if (FIELD_SIZE < 38) {
            FieldPrinter.printField(FieldPrinter.buildJuniorFieldDeltaString(this, previousState));
        } else {
            FieldPrinter.printField(FieldPrinter.buildStandardFieldDeltaString(this, previousState));
        }
    }
}
