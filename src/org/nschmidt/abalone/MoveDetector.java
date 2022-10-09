package org.nschmidt.abalone;

import static org.nschmidt.abalone.DoubleAttacker.performDoubleAttack;
import static org.nschmidt.abalone.TripleAttacker.performTripleAttack;
import static org.nschmidt.abalone.TripleMover.moveThreeMarbles;
import static org.nschmidt.abalone.DoubleMover.moveTwoMarbles;
import static org.nschmidt.abalone.SingleMover.moveSingleMarble;

public enum MoveDetector {
    INSTANCE;
    
    public static long[] allMoves(long state, long player) {
        final long[] singleMoves = moveSingleMarble(state, player);
        final long[] doubleMoves = moveTwoMarbles(state, player);
        final long[] tripleMoves = moveThreeMarbles(state, player);
        final long[] doubleAttacks = performDoubleAttack(state, player);
        final long[] tripleAttacks = performTripleAttack(state, player);
        
        final int singleMoveCount = singleMoves.length;
        final int doubleMoveCount = doubleMoves.length;
        final int tripleMoveCount = tripleMoves.length;
        final int doubleAttackCount = doubleAttacks.length;
        final int tripleAttackCount = tripleAttacks.length;
        final int singleAndDoubleMoveCount = singleMoveCount + doubleMoveCount;
        final int totalMoveCount = singleAndDoubleMoveCount + tripleMoveCount;
        final int totalMoveAndDoubleAttackCount = totalMoveCount + doubleAttackCount;
        
        final long[] result = new long[totalMoveAndDoubleAttackCount + tripleAttackCount];
        System.arraycopy(singleMoves, 0, result, 0, singleMoveCount);
        System.arraycopy(doubleMoves, 0, result, singleMoveCount, doubleMoveCount);
        System.arraycopy(tripleMoves, 0, result, singleAndDoubleMoveCount, tripleMoveCount);
        System.arraycopy(doubleAttacks, 0, result, totalMoveCount, doubleAttackCount);
        System.arraycopy(tripleAttacks, 0, result, totalMoveAndDoubleAttackCount, tripleAttackCount);
        
        return result;
    }
}
