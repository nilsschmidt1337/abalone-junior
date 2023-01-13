package org.nschmidt.abalone.ai;

import static org.nschmidt.abalone.move.MoveDetector.allMoves;
import static org.nschmidt.abalone.playfield.FieldEvaluator.score;

import java.util.Arrays;
import java.util.HashSet;

import org.nschmidt.abalone.playfield.Field;
import org.nschmidt.abalone.playfield.Player;

public class HeuristicAlphaBetaAI {

    private final int maxDepth;
    private final Player player;
    private Field bestMove;

    public HeuristicAlphaBetaAI(int maxDepth, Player player) {
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
            return score(board, team);
        }
        
        Field[] moves = allMoves(board, team);
        moves = new HashSet<>(Arrays.asList(moves)).toArray(new Field[0]);
        Arrays.sort(moves, (m1, m2) -> Double.compare(score(m2, team), score(m1, team)));
        int count = 0;
        final int maxCount = (int) (moves.length / Math.pow(2, Math.max(1, depth - 3)));
        
        if (maximize) {
            Field localBestMove = null;
            for (Field move : moves) {
                double score = alphaBetaPruning(move, team.switchPlayer(), alpha, beta, depth);

                if (score > alpha) {
                    alpha = score;
                    localBestMove = move;
                }

                count++;
                if (count > maxCount || beta <= alpha) {
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

                count++;
                if (count > maxCount || beta <= alpha) {
                    break;
                }
            }

            return beta;
        }
    }
}