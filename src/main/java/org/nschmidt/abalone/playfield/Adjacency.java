package org.nschmidt.abalone.playfield;

import static org.nschmidt.abalone.playfield.Field.DIRECTION_COUNT;
import static org.nschmidt.abalone.playfield.Field.FIELD_HEIGHT;
import static org.nschmidt.abalone.playfield.Field.FIELD_SIZE;
import static org.nschmidt.abalone.playfield.Field.FIELD_WIDTH;

public enum Adjacency {
    INSTANCE;
    
    private static final int[][] indexArray = new int[FIELD_HEIGHT + 2][FIELD_WIDTH + 4];
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
    
    public static int indexAt(int y, int x) {
        return indexArray[y + 1][x + 2];
    }
    
    public static int[][] initAdjacency() {
        final int fieldWidthPlusMargin = FIELD_WIDTH + 4;
        final int fieldHeightPlusMargin = FIELD_HEIGHT + 2;
        final int fieldHeightHalf = (FIELD_HEIGHT - 1) / 2;
        final int fieldHeightHalfPlusMargin = fieldHeightHalf + 1;
        final int[][] adjacencyArray = new int[FIELD_SIZE][DIRECTION_COUNT];
        int i = 0;
        
        for (int y = 0; y < fieldHeightPlusMargin; y++) {
            for (int x = 0; x < fieldWidthPlusMargin; x++) {
                indexArray[y][x] = -1;
            }
        }
        
        for (int y = 1; y < fieldHeightHalfPlusMargin; y++) {
            int fromX = (fieldHeightHalf - y + 1) + 2;
            int toX = fromX + (fieldHeightHalf + y - 1) * 2 + 2;
            for (int x = fromX; x < toX; x += 2) {
                indexArray[y][x] = i;
                i++;
            }
        }
        
        for (int y = fieldHeightHalfPlusMargin; y < (fieldHeightPlusMargin - 1); y++) {
            int fromX = (y - fieldHeightHalfPlusMargin) + 2;
            int toX = FIELD_WIDTH - (y - fieldHeightHalfPlusMargin) + 2;
            for (int x = fromX; x < toX; x += 2) {
                indexArray[y][x] = i;
                i++;
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
