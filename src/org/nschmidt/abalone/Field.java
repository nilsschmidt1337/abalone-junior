package org.nschmidt.abalone;

import static org.nschmidt.abalone.Player.BLACK;
import static org.nschmidt.abalone.Player.EMPTY;
import static org.nschmidt.abalone.Player.WHITE;

public enum Field {
    INSTANCE;
    
    private static final long[] fieldDiv = initFieldDiv();
    
    public static final long INITIAL_FIELD = initField();
    
    private static long[] initFieldDiv() {
        final long[] fieldDiv = new long[37];
        for (int i = 0; i < 37; i++) {
            long index = 1L;
            for (int j = 0; j < i; j++) {
                index = index * 3L;
            }
            
            fieldDiv[i] = index;
        }
        
        return fieldDiv;
    }
    
    private static long initField() {
        long state = 0L;
        for (int i = 0; i < 9; i++) {
            state = populateField(state, i, BLACK);
            state = populateField(state, i + 28, WHITE);
        }
        
        return state;
    }
    
    public static long populateField(long state, int fieldIndex, Player player) {
        long factor = fieldDiv[fieldIndex];
        long oldValue = Long.remainderUnsigned(Long.divideUnsigned(state, factor), 3L);
        return state - oldValue * factor + player.getNumber() * factor;
    }
    
    public static long move(long state, Player player, int from, int to) {
        return populateField(populateField(state, to, player), from, EMPTY);
    }
    
    public static Player lookAtField(long state, int fieldIndex) {
        long factor = fieldDiv[fieldIndex];
        return Player.valueOf(Long.remainderUnsigned(Long.divideUnsigned(state, factor), 3L));
    }
}
