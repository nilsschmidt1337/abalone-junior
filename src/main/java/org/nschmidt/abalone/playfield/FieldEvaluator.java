package org.nschmidt.abalone.playfield;

import static org.nschmidt.abalone.playfield.Adjacency.BORDER_INDICES;
import static org.nschmidt.abalone.playfield.Adjacency.adjacency;
import static org.nschmidt.abalone.playfield.Field.FIELD_HEIGHT;
import static org.nschmidt.abalone.playfield.Field.FIELD_SIZE;
import static org.nschmidt.abalone.playfield.Field.lookAtField;
import static org.nschmidt.abalone.winning.WinningChecker.wins;

import java.util.Set;
import java.util.TreeSet;

import org.nschmidt.abalone.move.MoveDetector;

public enum FieldEvaluator {
    INSTANCE;
    
    private static final int[][] FIELD_RINGS = createRingArray();
    
    public static long score(Field state, Player player) {
        final Player opponent = player.switchPlayer();
        if (wins(state, opponent)) return Long.MIN_VALUE;
        long score = 0;
        long bonus = 0;
        
        for (int[] ringIndices : FIELD_RINGS) {
            bonus += 1;
            for (int i : ringIndices) {
                if (lookAtField(state, i) == player) {
                    if (isIsolated(state, player, i)) {
                        score -= 100_000;
                    }
                    
                    score += bonus;
                }
            }
        }
        
        score -= MoveDetector.allAttackMoves(state, opponent).length * 10000;
        if (score >= 0) {
            if (wins(state, player)) score += 10000;
            
            for (int i = 0; i < FIELD_SIZE; i++) {
                bonus = 1;
                for (int neighbour : adjacency(i)) {
                    if (neighbour != -1 && lookAtField(state, neighbour) == player) {
                        score += bonus;
                        bonus *= 2;
                    }
                }
            }
        }
        
        return score;
    }

    public static boolean isIsolated(Field state, Player player, int i) {
        boolean isIsolated = true;
        for (int neighbour : adjacency(i)) {
            if (neighbour != -1 && lookAtField(state, neighbour) == player) {
                isIsolated = false;
                break;
            }
        }
        
        return isIsolated;
    }
    
    public static int[] ring(int n) {
        final Set<Integer> newRing = new TreeSet<>();
        final Set<Integer> allPrevousRings = new TreeSet<>();
        for (int i : BORDER_INDICES) {
            newRing.add(i);
        }
        
        for (int i = 0; i < n; i++) {
            allPrevousRings.addAll(newRing);
            newRing.clear();
            
            for (int p : allPrevousRings) {
                for (int neighbour : adjacency(p)) {
                    if (neighbour != -1 && !allPrevousRings.contains(neighbour)) {
                        newRing.add(neighbour);
                    }
                }
            }
        }
        
        return newRing.stream().mapToInt(i -> i).toArray();
    }
    
    private static int[][] createRingArray() {
        int ringCount = (FIELD_HEIGHT - 1) / 2 + 1;
        int[][] result = new int[ringCount][];
        for (int i = 0; i < ringCount; i++) {
            result[i] = ring(i);
        }
        
        return result;
    }
}
