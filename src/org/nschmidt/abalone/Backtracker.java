package org.nschmidt.abalone;

import static org.nschmidt.abalone.FieldEvaluator.score;
import static org.nschmidt.abalone.MoveDetector.allMoves;
import static org.nschmidt.abalone.WinningChecker.wins;

import java.math.BigInteger;

public enum Backtracker {
    INSTANCE;
    
    public static BigInteger backtrack(BigInteger state, Player player, int depth) {
        BigInteger[] moves = allMoves(state, player);
        long maxScore = Long.MIN_VALUE;
        BigInteger maxMove = moves[0];
        
        final Player opponent = player.switchPlayer();
        for (BigInteger move : moves) {
            long initialScore = score(move, player);
            BigInteger result = playRound(move, opponent, depth);
            long score = score(result, player);
            if (score > maxScore && initialScore > maxScore) {
                maxScore = initialScore;
                maxMove = move;
            }
        }
        
        return maxMove;
    }
    
    
    private static BigInteger playRound(BigInteger state, Player currentPlayer, int maxDepth) {
        int depth = 0;
        while (depth < maxDepth) {
            BigInteger[] moves = allMoves(state, currentPlayer);
            
            long maxScore = Long.MIN_VALUE;
            BigInteger maxMove = moves[0];
            
            for (BigInteger move : moves) {
                long score = score(move, currentPlayer);
                if (score > maxScore) {
                    maxScore = score;
                    maxMove = move;
                }
            }
            
            state = maxMove;
            
            currentPlayer = currentPlayer.switchPlayer();
            if (wins(state, currentPlayer)) break;
            depth++;
        }
        
        return state;
    }
}
