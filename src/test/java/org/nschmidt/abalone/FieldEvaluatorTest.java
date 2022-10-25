package org.nschmidt.abalone;

import static org.junit.jupiter.api.Assertions.*;
import static org.nschmidt.abalone.Adjacency.BORDER_INDICES;
import static org.nschmidt.abalone.Adjacency.adjacency;
import static org.nschmidt.abalone.Field.DIRECTION_COUNT;
import static org.nschmidt.abalone.Field.FIELD_SIZE;
import static org.nschmidt.abalone.Field.INITIAL_FIELD;
import static org.nschmidt.abalone.FieldEvaluator.*;
import static org.nschmidt.abalone.Player.BLACK;
import static org.nschmidt.abalone.Player.WHITE;

import java.util.Set;
import java.util.TreeSet;

import org.junit.jupiter.api.Test;

class FieldEvaluatorTest {

    @Test
    void testScoreOfInitialFieldForPlayer1() {
        assertEquals(score(INITIAL_FIELD, WHITE), score(INITIAL_FIELD, BLACK));
        assertEquals(121L, score(INITIAL_FIELD, BLACK));
    }
    
    @Test
    void testRing() {
        assertArrayEquals(ring2(-1), ring(-1));
        assertArrayEquals(ring2(0), ring(0));
        assertArrayEquals(ring2(1), ring(1));
        assertArrayEquals(ring2(2), ring(2));
        assertArrayEquals(ring2(3), ring(3));
        assertArrayEquals(ring2(4), ring(4));
    }
    
    private static int[] ring2(int n) {
        if (n <= 0) return BORDER_INDICES;
        if (n == 1) return initMiddle();
        if (n == 2) return new int[] {11, 12, 17, 19, 24, 25};
        if (n == 3) return new int[] {18};
        return new int[] {};
    }
    
    private static int[] initMiddle() {
        Set<Integer> border = new TreeSet<>();
        for (int i : BORDER_INDICES) {
            border.add(i);
        }
        
        int[] result = new int[12];
        int index = 0;
        for (int i = 0; i < FIELD_SIZE; i++) {
            if (border.contains(i)) continue;
            int[] neighbours = adjacency(i);
            for (int d = 0; d < DIRECTION_COUNT; d++) {
                if (border.contains(neighbours[d])) {
                    result[index] = i;
                    index++;
                    break;
                }
            }
        }
        
        return result;
    }
}
