package org.nschmidt.abalone;

import static org.nschmidt.abalone.Player.BLACK;
import static org.nschmidt.abalone.Player.EMPTY;
import static org.nschmidt.abalone.Player.WHITE;

import java.math.BigInteger;

public enum Field {
    INSTANCE;
    
    private static final BigInteger THREE = BigInteger.valueOf(3L);
    private static final BigInteger[] fieldDiv = initFieldDiv();
    
    public static final BigInteger INITIAL_FIELD = initField();
    
    private static BigInteger[] initFieldDiv() {
        final BigInteger[] fieldDiv = new BigInteger[37];
        for (int i = 0; i < 37; i++) {
            BigInteger index = BigInteger.ONE;
            for (int j = 0; j < i; j++) {
                index = index.multiply(THREE);
            }
            
            fieldDiv[i] = index;
        }
        
        return fieldDiv;
    }
    
    private static BigInteger initField() {
        BigInteger state = BigInteger.ZERO;
        for (int i = 0; i < 9; i++) {
            state = populateField(state, i, BLACK);
            state = populateField(state, i + 28, WHITE);
        }
        
        return state;
    }
    
    public static BigInteger populateField(BigInteger state, int fieldIndex, Player player) {
        BigInteger factor = fieldDiv[fieldIndex];
        BigInteger oldValue = state.divide(factor).remainder(THREE);
        return state.subtract(oldValue.multiply(factor)).add(player.getNumber().multiply(factor));
    }
    
    public static BigInteger move(BigInteger state, Player player, int from, int to) {
        return populateField(populateField(state, to, player), from, EMPTY);
    }
    
    public static Player lookAtField(BigInteger state, int fieldIndex) {
        BigInteger factor = fieldDiv[fieldIndex];
        return Player.valueOf(state.divide(factor).remainder(THREE));
    }
}
