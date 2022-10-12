package org.nschmidt.abalone;

public enum Adjacency {
    INSTANCE;
    
    private static final int[][] adjacencyMatrix = initAdjacency();
    
    public static final int[] BORDER_INDICIES = initBorderIndicies();
    
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
    
    private static int[] initBorderIndicies() {
        final int[] borderIndicies = new int[18];
        int i = 0;
        for (int j = 0; j < 37; j++) {
            for (int neighbourIndex : adjacencyMatrix[j]) {
                if (neighbourIndex == -1) {
                    borderIndicies[i] = j;
                    i++;
                    break;
                }
            }
        }
        
        return borderIndicies;
    }
}
