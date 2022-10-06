package org.nschmidt.abalone;

public class Main {

    private static final long[] fieldDiv = initFieldDiv();
    private static final int[][] adjacency = initAdjacency();
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
        System.out.println(state);
        return state;
    }
    
    private static int[][] initAdjacency() {
        final int[][] indexArray = new int[9][17];
        final int[][] adjacencyArray = new int[37][6];
        int i = 0;
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 17; x++) {
                i = setIndexArrayUpperHalf(x, y, indexArray, i);
                i = setIndexArrayLowerHalf(x, y, indexArray, i);
            }
        }
        
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 17; x++) {
                final int index = indexArray[y][x];
                if (index != -1) {
                    adjacencyArray[index][0] = indexArray[y - 1][x - 1];
                    adjacencyArray[index][1] = indexArray[y + 1][x + 1];
                    adjacencyArray[index][2] = indexArray[y - 1][x + 1];
                    adjacencyArray[index][3] = indexArray[y + 1][x - 1];
                    adjacencyArray[index][4] = indexArray[y][x - 2];
                    adjacencyArray[index][5] = indexArray[y][x + 2];
                }
            }
        }
        
        
        return adjacencyArray;
    }

    private static int setIndexArrayUpperHalf(int x, int y, int[][] indexArray, int i) {
        indexArray[y][x] = -1;
        
        if (y == 1 && x > 4 && x < 12 && x % 2 == 1) {
            indexArray[y][x] = i;
            i++;
        }
        
        if (y == 2 && x > 3 && x < 13 && x % 2 == 0) {
            indexArray[y][x] = i;
            i++;
        }
        
        if (y == 3 && x > 2 && x < 14 && x % 2 == 1) {
            indexArray[y][x] = i;
            i++;
        }
        
        if (y == 4 && x > 1 && x < 15 && x % 2 == 0) {
            indexArray[y][x] = i;
            i++;
        }
        
        return i;
    }
    
    private static int setIndexArrayLowerHalf(int x, int y, int[][] indexArray, int i) {
        if (y == 5 && x > 2 && x < 14 && x % 2 == 1) {
            indexArray[y][x] = i;
            i++;
        }
        
        if (y == 6 && x > 3 && x < 13 && x % 2 == 0) {
            indexArray[y][x] = i;
            i++;
        }
        
        if (y == 7 && x > 4 && x < 12 && x % 2 == 1) {
            indexArray[y][x] = i;
            i++;
        }
        
        return i;
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
    
    public static long lookAtField(long state, int fieldIndex) {
        long factor = fieldDiv[fieldIndex];
        return Long.remainderUnsigned(Long.divideUnsigned(state, factor), 3L);
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
