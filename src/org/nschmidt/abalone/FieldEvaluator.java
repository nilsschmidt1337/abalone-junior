package org.nschmidt.abalone;

import static org.nschmidt.abalone.Adjacency.BORDER_INDICIES;
import static org.nschmidt.abalone.Adjacency.adjacency;
import static org.nschmidt.abalone.Field.lookAtField;

import java.math.BigInteger;
import java.util.Set;
import java.util.TreeSet;

public enum FieldEvaluator {
    INSTANCE;
    
    private static final int[] MIDDLE_INDICIES = initMiddle();
    private static final int[] CENTER_INDICIES = {11, 12, 17, 19, 24, 25};
    
    public static long score(BigInteger state, Player player) {
        final Player opponent = player.switchPlayer();
        if (WinningChecker.wins(state, opponent)) return Long.MIN_VALUE;
        long score = 0;
        if (WinningChecker.wins(state, player)) score += 1_000_000;
        
        for (int i : BORDER_INDICIES) {
            if (lookAtField(state, i) == player) score -= 1000;
        }
        
        for (int i : MIDDLE_INDICIES) {
            if (lookAtField(state, i) == player) score += 50;
        }
        
        for (int i : CENTER_INDICIES) {
            if (lookAtField(state, i) == player) score += 70;
        }
        
        if (lookAtField(state, 18) == player) score += 100;

        return score;
    }
    
    private static int[] initMiddle() {
        Set<Integer> border = new TreeSet<>();
        for (int i : BORDER_INDICIES) {
            border.add(i);
        }
        
        int[] result = new int[12];
        int index = 0;
        for (int i = 0; i < 37; i++) {
            if (border.contains(i)) continue;
            int[] neighbours = adjacency(i);
            for (int d = 0; d < 6; d++) {
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
