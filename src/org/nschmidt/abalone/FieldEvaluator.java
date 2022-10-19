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
            if (lookAtField(state, i) == player) score += 1;
        }
        
        for (int i : MIDDLE_INDICES) {
            if (lookAtField(state, i) == player) score += 2;
        }
        
        for (int i : CENTER_INDICES) {
            if (lookAtField(state, i) == player) score += 3;
        }
  
        if (lookAtField(state, CENTRAL_INDEX) == player) score += 4;
        
        score -= MoveDetector.allAttackMoves(state, opponent).length * 10000;
        if (score >= 0 && wins(state, player)) score += 10000;
        
        return score;
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
