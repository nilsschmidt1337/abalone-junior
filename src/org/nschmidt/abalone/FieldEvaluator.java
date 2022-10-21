package org.nschmidt.abalone;

import static org.nschmidt.abalone.Adjacency.BORDER_INDICES;
import static org.nschmidt.abalone.Adjacency.adjacency;
import static org.nschmidt.abalone.Field.lookAtField;
import static org.nschmidt.abalone.WinningChecker.wins;

import java.util.Set;
import java.util.TreeSet;

public enum FieldEvaluator {
    INSTANCE;
    
    private static final int[] MIDDLE_INDICES = ring(1);
    private static final int[] CENTER_INDICES = ring(2);
    private static final int CENTRAL_INDEX = ring(3)[0];
    
    public static long score(Field state, Player player) {
        final Player opponent = player.switchPlayer();
        if (wins(state, opponent)) return Long.MIN_VALUE;
        long score = 0;
        
        for (int i : BORDER_INDICES) {
            if (lookAtField(state, i) == player) {
                if (isIsolated(state, player, i)) {
                    score -= 100_000;
                }
                
                score += 1;
            }
        }
        
        for (int i : MIDDLE_INDICES) {
            if (lookAtField(state, i) == player) {
                if (isIsolated(state, player, i)) {
                    score -= 100_000;
                }
                
                score += 2;
            }
        }
        
        for (int i : CENTER_INDICES) {
            if (lookAtField(state, i) == player) {
                if (isIsolated(state, player, i)) {
                    score -= 100_000;
                }
                
                score += 3;
            }
        }
  
        if (lookAtField(state, CENTRAL_INDEX) == player) score += 4;
        
        score -= MoveDetector.allAttackMoves(state, opponent).length * 10000;
        if (score >= 0) {
            if (wins(state, player)) score += 10000;
            
            for (int[] indices : new int[][] {BORDER_INDICES, MIDDLE_INDICES, CENTER_INDICES, new int[] {CENTRAL_INDEX}}) {
                for (int i : indices) {
                    int bonus = 1;
                    for (int neighbour : adjacency(i)) {
                        if (neighbour != -1 && lookAtField(state, neighbour) == player) {
                            score += bonus;
                            bonus *= 2;
                        }
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
}
