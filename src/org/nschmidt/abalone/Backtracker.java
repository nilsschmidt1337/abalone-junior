package org.nschmidt.abalone;

import static org.nschmidt.abalone.FieldEvaluator.score;
import static org.nschmidt.abalone.MoveDetector.allMoves;
import static org.nschmidt.abalone.WinningChecker.wins;
import static org.nschmidt.abalone.WinningInTwoMovesChecker.winsInTwoMoves;

public enum Backtracker {
    INSTANCE;
    
    public static Field backtrack(Field state, Player player, int depth) {
        final Player opponent = player.switchPlayer();
        Field[] winsInTwo = winsInTwoMoves(state, player);
        if (winsInTwo.length == 2) {
            return winsInTwo[0];
        }
        
        Field[] moves = allMoves(state, player);
        long maxScore = Long.MIN_VALUE;
        Field maxMove = null;
        for (Field move : moves) {
            // Mache keinen Zug, bei dem der Gegner in zwei Zügen gewinnt
            Field[] oppenentWinsInTwo = winsInTwoMoves(move, opponent);
            if (oppenentWinsInTwo.length == 0) {
                maxMove = move;
                break;
            }
        }
        
        if (maxMove == null) {
            maxMove = moves[0];
        }
        
        if (score(state, player) == Long.MIN_VALUE) {
            for (Field move : moves) {
                long score = score(move, player);
                if (score > maxScore) {
                    maxScore = score;
                    maxMove = move;
                }
            }
            
            return maxMove;
        }

        
        for (Field move : moves) {
            long initialScore = score(move, player);
            Field result = playRound(move, opponent, depth);
            long score = score(result, player);
            if ((score > maxScore || score == Long.MIN_VALUE) && initialScore > maxScore) {
                // Mache keinen Zug, bei dem der Gegner in zwei Zügen gewinnt
                Field[] oppenentWinsInTwo = winsInTwoMoves(move, opponent);
                if (oppenentWinsInTwo.length == 2) {
                    continue;
                }
                maxScore = initialScore;
                maxMove = move;
            }
        }
        
        return maxMove;
    }
    
    
    private static Field playRound(Field state, Player currentPlayer, int maxDepth) {
        int depth = 0;
        while (depth < maxDepth) {
            Field[] moves = allMoves(state, currentPlayer);
            
            long maxScore = Long.MIN_VALUE;
            Field maxMove = moves[0];
            
            for (Field move : moves) {
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
