package org.nschmidt.abalone;

import static org.nschmidt.abalone.FieldEvaluator.score;
import static org.nschmidt.abalone.MoveDetector.allMoves;

import java.util.Arrays;

public class AlphaBetaAI {

    private final int maxDepth;
    private final Player player;
    private Field bestMove;

    public AlphaBetaAI(int maxDepth, Player player) {
        this.maxDepth = maxDepth;
        this.bestMove = null;
        this.player = player;
    }

    public Field bestMove(Field board) {
        alphaBetaPruning(board, player, Long.MIN_VALUE, Long.MAX_VALUE, 0);
        return bestMove;
    }

    private long alphaBetaPruning(Field board, Player team, long alpha, long beta, int depth) {
        final boolean maximize = team == player;

        if (depth++ == maxDepth) {
            return score(board, team);
        }
        Field[] moves = allMoves(board, team);
        Arrays.sort(moves, (m1, m2) -> Long.compare(score(m2, team), score(m1, team)));
        
        if (maximize) {
            if (WinningChecker.wins(board, team)) {
                return Long.MAX_VALUE;
            }
                
            Field localBestMove = null;
            for (Field move : moves) {
                long score = alphaBetaPruning(move, team.switchPlayer(), alpha, beta, depth);

                if (score > alpha) {
                    alpha = score;
                    localBestMove = move;
                }

                if (beta <= alpha) {
                    break;
                }
            }

            if (localBestMove != null && depth == 1)
                bestMove = localBestMove;

            return alpha;
        } else {
            if (WinningChecker.wins(board, team)) {
                return Long.MIN_VALUE;
            }
            
            for (Field move : moves) {
                long score = alphaBetaPruning(move, team.switchPlayer(), alpha, beta, depth);

                if (score < beta) {
                    beta = score;
                }

                if (beta <= alpha) {
                    break;
                }
            }

            return beta;
        }
    }
}

