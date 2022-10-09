package org.nschmidt.abalone;

import static org.nschmidt.abalone.Attacker.performAttack;
import static org.nschmidt.abalone.TripleMover.moveThreeMarbles;
import static org.nschmidt.abalone.DoubleMover.moveTwoMarbles;
import static org.nschmidt.abalone.SingleMover.moveSingleMarble;

public enum MoveDetector {
    INSTANCE;
    
    public static long[] allMoves(long state, long player) {
        final long[] singleMoves = moveSingleMarble(state, player);
        final long[] doubleMoves = moveTwoMarbles(state, player);
        final long[] tripleMoves = moveThreeMarbles(state, player);
        final long[] attacks = performAttack(state, player);
        
        final int singleMoveCount = singleMoves.length;
        final int doubleMoveCount = doubleMoves.length;
        final int tripleMoveCount = tripleMoves.length;
        final int attackCount = attacks.length;
        final int singleAndDoubleMoveCount = singleMoveCount + doubleMoveCount;
        final int moveCount = singleAndDoubleMoveCount + tripleMoveCount;
        
        final long[] result = new long[moveCount + attackCount];
        System.arraycopy(singleMoves, 0, result, 0, singleMoveCount);
        System.arraycopy(doubleMoves, 0, result, singleMoveCount, doubleMoveCount);
        System.arraycopy(tripleMoves, 0, result, singleAndDoubleMoveCount, tripleMoveCount);
        System.arraycopy(attacks, 0, result, moveCount, attackCount);
        
        return result;
    }
}
