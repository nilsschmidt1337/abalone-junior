package org.nschmidt.abalone;

import static org.nschmidt.abalone.FieldPrinter.printField;

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
            state = populateField(state, i, 1);
            state = populateField(state, i + 28, 2);
        }
        
        printField(state);
        System.out.println(state);
        return state;
    }
    
    public static long populateField(long state, int fieldIndex, long player) {
        long factor = fieldDiv[fieldIndex];
        long oldValue = Long.remainderUnsigned(Long.divideUnsigned(state, factor), 3L);
        return state - oldValue * factor + player * factor;
    }
    
    public static long lookAtField(long state, int fieldIndex) {
        long factor = fieldDiv[fieldIndex];
        return Long.remainderUnsigned(Long.divideUnsigned(state, factor), 3L);
    }
}
