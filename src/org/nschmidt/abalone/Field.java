package org.nschmidt.abalone;

import static org.nschmidt.abalone.Player.BLACK;
import static org.nschmidt.abalone.Player.EMPTY;
import static org.nschmidt.abalone.Player.WHITE;

import java.math.BigInteger;

public enum Field {
    INSTANCE;
    
    public static final BigInteger INITIAL_FIELD = initField();
    
    private static BigInteger initField() {
        BigInteger state = BigInteger.ZERO;
        for (int i = 0; i < 9; i++) {
            state = populateField(state, i, BLACK);
            state = populateField(state, i + 28, WHITE);
        }
        
        return state;
    }
    
    public static BigInteger populateField(BigInteger state, int fieldIndex, Player player) {
        if (state.testBit(fieldIndex)) state = state.clearBit(fieldIndex);
        if (state.testBit(fieldIndex + 37)) state = state.clearBit(fieldIndex + 37);
        return Player.EMPTY == player ? state : state.setBit(fieldIndex + 37 * (player.getNumber() - 1));
    }
    
    public static BigInteger move(BigInteger state, Player player, int from, int to) {
        return populateField(populateField(state, to, player), from, EMPTY);
    }
    
    public static Player lookAtField(BigInteger state, int fieldIndex) {
        return Player.valueOf((state.testBit(fieldIndex) ? 1 : 0) + (state.testBit(fieldIndex + 37) ? 2 : 0));
    }
}
