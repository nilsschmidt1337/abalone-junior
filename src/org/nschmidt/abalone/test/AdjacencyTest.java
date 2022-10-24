package org.nschmidt.abalone.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.nschmidt.abalone.Adjacency.*;

import org.junit.jupiter.api.Test;
import org.nschmidt.abalone.Adjacency;
import org.nschmidt.abalone.Field;

class AdjacencyTest {

    @Test
    void testAdjacencyForNegativeIndex() {
        Throwable ex = assertThrows(ArrayIndexOutOfBoundsException.class, () -> 
            adjacency(-1)
        );
        
        assertEquals("Index -1 out of bounds for length 37", ex.getMessage());
    }

    @Test
    void testAdjacencyForOffByOneIndex() {
        Throwable ex = assertThrows(ArrayIndexOutOfBoundsException.class, () -> 
            adjacency(38)
        );
        
        assertEquals("Index 38 out of bounds for length 37", ex.getMessage());
    }
    
    @Test
    void testAdjacencyForUpperLeftCorner() {
        int[] result = adjacency(0);
        
        assertArrayEquals(new int[] {-1, 5, -1, 4, -1, 1}, result);
    }
    
    @Test
    void testAdjacencyForMiddle() {
        int[] result = adjacency(20);
        
        assertArrayEquals(new int[] {13, 27, 14, 26, 19, 21}, result);
    }
    
    @Test
    void testAdjacencyForLeftSide() {
        int[] result = adjacency(15);
        
        assertArrayEquals(new int[] {-1, 22, 9, -1, -1, 16}, result);
    }
    
    @Test
    void testMoveFromTopLeftToBottomRight() {
        int dir = BOTTOM_RIGHT;
        int index = 0;
        
        index = adjacency(index)[dir];
        index = adjacency(index)[dir];
        index = adjacency(index)[dir];
        index = adjacency(index)[dir];
        index = adjacency(index)[dir];
        index = adjacency(index)[dir];
        
        assertEquals(36, index);
    }
    
    @Test
    void testMoveFromBottomRightToTopLeft() {
        int dir = TOP_LEFT;
        int index = 36;
        
        index = adjacency(index)[dir];
        index = adjacency(index)[dir];
        index = adjacency(index)[dir];
        index = adjacency(index)[dir];
        index = adjacency(index)[dir];
        index = adjacency(index)[dir];
        
        assertEquals(0, index);
    }
    
    @Test
    void testMoveFromTopRightToBottomLeft() {
        int dir = BOTTOM_LEFT;
        int index = 3;
        
        index = adjacency(index)[dir];
        index = adjacency(index)[dir];
        index = adjacency(index)[dir];
        index = adjacency(index)[dir];
        index = adjacency(index)[dir];
        index = adjacency(index)[dir];
        
        assertEquals(33, index);
    }
    
    @Test
    void testMoveFromBottomLeftToTopRight() {
        int dir = TOP_RIGHT;
        int index = 33;
        
        index = adjacency(index)[dir];
        index = adjacency(index)[dir];
        index = adjacency(index)[dir];
        index = adjacency(index)[dir];
        index = adjacency(index)[dir];
        index = adjacency(index)[dir];
        
        assertEquals(3, index);
    }
    
    @Test
    void testMoveFromLeftToRight() {
        int dir = RIGHT;
        int index = 9;
        
        index = adjacency(index)[dir];
        index = adjacency(index)[dir];
        index = adjacency(index)[dir];
        index = adjacency(index)[dir];
        index = adjacency(index)[dir];
        
        assertEquals(14, index);
    }
    
    @Test
    void testMoveFromRightToLeft() {
        int dir = LEFT;
        int index = 14;
        
        index = adjacency(index)[dir];
        index = adjacency(index)[dir];
        index = adjacency(index)[dir];
        index = adjacency(index)[dir];
        index = adjacency(index)[dir];
        
        assertEquals(9, index);
    }
    
    @Test
    void testBorderIndex() {
        int[] result = adjacency(BORDER_INDICES[17]);
        
        assertArrayEquals(new int[] {31, -1, 32, -1, 35, -1}, result);
    }
    
    @Test
    void testBorderForOffByOneIndex() {
        Throwable ex = assertThrows(ArrayIndexOutOfBoundsException.class, () -> 
            adjacency(BORDER_INDICES[18])
        );
        
        assertEquals("Index 18 out of bounds for length 18", ex.getMessage());
    }
    
    @Test
    void testOldAdjacency() {
        assertArrayEquals(initAdjacency(), Adjacency.initAdjacency());
    }
    
    private static int[][] initAdjacency() {
        final int fieldWidthPlusMargin = Field.FIELD_WIDTH + 4;
        final int fieldHeightPlusMargin = Field.FIELD_HEIGHT + 2;
        final int[][] adjacencyArray = new int[Field.FIELD_SIZE][Field.DIRECTION_COUNT];
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
}
