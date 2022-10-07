package org.nschmidt.abalone.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.nschmidt.abalone.Adjacency.*;

import org.junit.jupiter.api.Test;

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
        int[] result = adjacency(BORDER_INDICIES[17]);
        
        assertArrayEquals(new int[] {31, -1, 32, -1, 35, -1}, result);
    }
    
    @Test
    void testBorderForOffByOneIndex() {
        Throwable ex = assertThrows(ArrayIndexOutOfBoundsException.class, () -> 
            adjacency(BORDER_INDICIES[18])
        );
        
        assertEquals("Index 18 out of bounds for length 18", ex.getMessage());
    }
}
