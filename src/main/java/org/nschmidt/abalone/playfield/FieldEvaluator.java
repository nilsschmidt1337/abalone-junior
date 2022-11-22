package org.nschmidt.abalone.playfield;

import static org.nschmidt.abalone.playfield.Adjacency.BORDER_INDICES;
import static org.nschmidt.abalone.playfield.Adjacency.adjacency;
import static org.nschmidt.abalone.playfield.Field.DIRECTION_COUNT;
import static org.nschmidt.abalone.playfield.Field.FIELD_HEIGHT;
import static org.nschmidt.abalone.playfield.Field.lookAtField;

import java.util.Set;
import java.util.TreeSet;

public enum FieldEvaluator {
    INSTANCE;
    
    private static final int[][] FIELD_RINGS = createRingArray();
    private static final int[] RING_SCORE = new int[] {45, 50, 53, 54, 54};
    
    public static int score(Field state, Player player) {
        final Player opponent = player.switchPlayer();
        int score = 0;
        
        
        for (int ringCount = 0; ringCount < FIELD_RINGS.length; ringCount++) {
            final int[] ringIndices = FIELD_RINGS[ringCount];
            final int bonus = RING_SCORE[ringCount];
            for (int i : ringIndices) {
                if (lookAtField(state, i) == player) {
                    score += bonus;
                } else if (lookAtField(state, i) == opponent) {
                    score -= bonus;
                }
            }
        }
        
        for (int from : BORDER_INDICES) {
            Player playerAtField = lookAtField(state, from);
            if (opponent == playerAtField) {
                final int[] neighbourIndices = adjacency(from);
                for (int d = 0; d < DIRECTION_COUNT; d+=2) {
                    score += eval(state, opponent, player, d, neighbourIndices);
                }
            } else if (player == playerAtField) {
                final int[] neighbourIndices = adjacency(from);
                for (int d = 0; d < DIRECTION_COUNT; d+=2) {
                    score -= eval(state, player, opponent, d, neighbourIndices) * 2;
                }
            }
        }
        
        return score;
    }
    
    static int eval(Field state, Player player, Player opponent, int dir, int[] neighbourIndices) {
        if (neighbourIndices[dir] != -1 && neighbourIndices[dir + 1] != -1) {
            return 0;
        }
        if (neighbourIndices[dir] == -1) dir++;
        final int secondMarbleIndex = neighbourIndices[dir];
        if (secondMarbleIndex == -1) return 0;
        final Player secondMarble = lookAtField(state, secondMarbleIndex);
        final int[] secondMarbleNeighbourIndices = adjacency(secondMarbleIndex);
        if (secondMarble == player) {
            final int thirdMarbleIndex = secondMarbleNeighbourIndices[dir];
            if (thirdMarbleIndex == -1) return 0;
            final Player thirdMarble = lookAtField(state, thirdMarbleIndex);
            final int[] thirdMarbleNeighbourIndices = adjacency(thirdMarbleIndex);
            
            if (thirdMarble != opponent) {
                return 0;
            }
            
            final int fourthMarbleIndex = thirdMarbleNeighbourIndices[dir];
            if (fourthMarbleIndex == -1) return 0;
            final Player fourthMarble = lookAtField(state, fourthMarbleIndex);
            final int[] fourthMarbleNeighbourIndices = adjacency(fourthMarbleIndex);
            
            if (fourthMarble != opponent) {
                return 0;
            }
            
            final int fifthMarbleIndex = fourthMarbleNeighbourIndices[dir];
            if (fifthMarbleIndex == -1) return 0;
            final Player fifthMarble = lookAtField(state, fifthMarbleIndex);
            
            if (fifthMarble == opponent) {
                return 8;
            }
        } else if (secondMarble == opponent) {
            final int thirdMarbleIndex = secondMarbleNeighbourIndices[dir];
            if (thirdMarbleIndex == -1) return 0;
            final Player thirdMarble = lookAtField(state, thirdMarbleIndex);
            
            if (thirdMarble == opponent) {
                return 8;
            }
        }
        
        
        return 0;
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
