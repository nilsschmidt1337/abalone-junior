package org.nschmidt.abalone.ai;

import static org.nschmidt.abalone.ai.NextGenAI.evalGameField;
import static org.nschmidt.abalone.move.MoveDetector.allMoves;

import java.util.Arrays;

import org.nschmidt.abalone.playfield.Field;
import org.nschmidt.abalone.playfield.Player;

public class NextGenAlphaBetaAI {

    private final int maxDepth;
    private final Player player;
    private Field bestMove;

    public NextGenAlphaBetaAI(int maxDepth, Player player) {
        this.maxDepth = maxDepth;
        this.bestMove = null;
        this.player = player;
    }

    public Field bestMove(Field board) {
        alphaBetaPruning(board, player, -Double.MAX_VALUE, Double.MAX_VALUE, 0);
        return bestMove;
    }

    private double alphaBetaPruning(Field board, Player team, double alpha, double beta, int depth) {
        final boolean maximize = team == player;

        if (depth++ == maxDepth) {
            return evalGameField(board, team);
        }
        Field[] moves = allMoves(board, team);
        Arrays.sort(moves, (m1, m2) -> Double.compare(evalGameField(m2, team), evalGameField(m1, team)));
        
        if (maximize) {
            Field localBestMove = null;
            for (Field move : moves) {
                double score = alphaBetaPruning(move, team.switchPlayer(), alpha, beta, depth);

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
            for (Field move : moves) {
                double score = alphaBetaPruning(move, team.switchPlayer(), alpha, beta, depth);

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

