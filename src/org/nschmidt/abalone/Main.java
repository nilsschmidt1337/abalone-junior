package org.nschmidt.abalone;

public class Main {

    private static final long[] fieldDiv = initFieldDiv();
    private static final long INITIAL_FIELD = initField();
    
    public static void main(String[] args) {
        long state = 0L;
        state = populateField(state, 0, 2);
        state = populateField(state, 1, 1);
        state = populateField(state, 2, 2);
        state = populateField(state, 36, 2);
        printField(state);
    }
    
    private static long initField() {
        long state = 0L;
        for (int i = 0; i < 9; i++) {
            state = populateField(state, i, 1);
            state = populateField(state, i + 28, 2);
        }
        
        printField(state);
        return state;
    }

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

    public static long populateField(long state, int fieldIndex, long value) {
        long factor = fieldDiv[fieldIndex];
        long oldValue = Long.remainderUnsigned(Long.divideUnsigned(state, factor), 3L);
        return state - oldValue * factor + value * factor;
    }
    
    public static void printField(long state) {
        long nextValue = state;
        printSpaces(3);
        for (int i = 0; i < 37; i++) {
            long fieldValue = Long.remainderUnsigned(nextValue, 3L);
            nextValue = Long.divideUnsigned(nextValue, 3L);
            breakLine(i);
            System.out.print(fieldValue);
            printSpaces(1);
        }
        
        System.out.println();
    }

    private static void printSpaces(int count) {
        for (int i = 0; i < count; i++) {
            System.out.print(' ');
        }
    }

    private static void breakLine(int i) {
        switch (i) {
        case 4, 28:
            System.out.println();
            printSpaces(2);
            break;
        case 9, 22:
            System.out.println();
            printSpaces(1);
            break;
        case 15:
            System.out.println();
            printSpaces(0);
            break;
        case 33:
            System.out.println();
            printSpaces(3);
            break;
        default:
        }
    }
}
