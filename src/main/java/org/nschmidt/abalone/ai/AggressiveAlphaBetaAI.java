package org.nschmidt.abalone.ai;

import static org.nschmidt.abalone.move.MoveDetector.allMoves;

import java.util.Arrays;

import org.nschmidt.abalone.playfield.Field;
import org.nschmidt.abalone.playfield.Player;
import org.nschmidt.abalone.winning.WinningChecker;

public class AggressiveAlphaBetaAI {

    private final int maxDepth;
    private final Player player;
    private Field bestMove;

    public AggressiveAlphaBetaAI(int maxDepth, Player player) {
        this.maxDepth = maxDepth;
        this.bestMove = null;
        this.player = player;
    }

    public Field bestMove(Field board) {
        alphaBetaPruning(board, player, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
        return bestMove;
    }

    private int alphaBetaPruning(Field board, Player team, int alpha, int beta, int depth) {
        final boolean maximize = team == player;

        if (depth++ == maxDepth) {
            return score(board, team);
        }
        Field[] moves = allMoves(board, team);
        Arrays.sort(moves, (m1, m2) -> Integer.compare(score(m2, team), score(m1, team)));
        
        if (maximize) {
            if (WinningChecker.wins(board, team)) {
                return Integer.MAX_VALUE;
            }
                
            Field localBestMove = null;
            for (Field move : moves) {
                int score = alphaBetaPruning(move, team.switchPlayer(), alpha, beta, depth);
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
                return Integer.MIN_VALUE;
            }
            
            for (Field move : moves) {
                int score = alphaBetaPruning(move, team.switchPlayer(), alpha, beta, depth);

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

    private int score(Field board, Player team) {
        return Field.countPieces(board, team) - Field.countPieces(board, team.switchPlayer());
    }
}

