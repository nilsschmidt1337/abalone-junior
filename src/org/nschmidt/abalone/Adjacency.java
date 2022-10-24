package org.nschmidt.abalone;

import static org.nschmidt.abalone.Field.DIRECTION_COUNT;
import static org.nschmidt.abalone.Field.FIELD_HEIGHT;
import static org.nschmidt.abalone.Field.FIELD_SIZE;
import static org.nschmidt.abalone.Field.FIELD_WIDTH;

public enum Adjacency {
    INSTANCE;
    
    private static final int[][] adjacencyMatrix = initAdjacency();
    
    public static final int[] BORDER_INDICES = initBorderIndices();
    
    public static final int TOP_LEFT = 0;
    public static final int BOTTOM_RIGHT = 1;
    public static final int TOP_RIGHT = 2;
    public static final int BOTTOM_LEFT = 3;
    public static final int LEFT = 4;
    public static final int RIGHT = 5;
    
    public static int[] adjacency(int i) {
        return adjacencyMatrix[i];
    }
    
    private static int[][] initAdjacency() {
        final int fieldWidthPlusMargin = FIELD_WIDTH + 4;
        final int fieldHeightPlusMargin = FIELD_HEIGHT + 2;
        final int[][] adjacencyArray = new int[FIELD_SIZE][DIRECTION_COUNT];
        final int[][] indexArray = new int[fieldHeightPlusMargin][fieldWidthPlusMargin];
        int i = 0;
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 17; x++) {
                i = setIndexArrayUpperHalf(x, y, indexArray, i);
                i = setIndexArrayLowerHalf(x, y, indexArray, i);
            }
        }
        
        for (int y = 0; y < fieldHeightPlusMargin; y++) {
            for (int x = 0; x < fieldWidthPlusMargin; x++) {
                final int index = indexArray[y][x];
                if (index != -1) {
                    adjacencyArray[index][TOP_LEFT]     = indexArray[y - 1][x - 1];
                    adjacencyArray[index][BOTTOM_RIGHT] = indexArray[y + 1][x + 1];
                    adjacencyArray[index][TOP_RIGHT]    = indexArray[y - 1][x + 1];
                    adjacencyArray[index][BOTTOM_LEFT]  = indexArray[y + 1][x - 1];
                    adjacencyArray[index][LEFT]         = indexArray[y][x - 2];
                    adjacencyArray[index][RIGHT]        = indexArray[y][x + 2];
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
    
    private static int[] initBorderIndices() {
        final int[] borderIndices = new int[3 * (FIELD_HEIGHT - 1)];
        int i = 0;
        for (int j = 0; j < FIELD_SIZE; j++) {
            for (int neighbourIndex : adjacencyMatrix[j]) {
                if (neighbourIndex == -1) {
                    borderIndices[i] = j;
                    i++;
                    break;
                }
            }
        }
        
        return borderIndices;
    }
}
