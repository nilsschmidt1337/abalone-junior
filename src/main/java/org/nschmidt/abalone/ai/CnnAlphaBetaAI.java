package org.nschmidt.abalone.ai;

import static org.nschmidt.abalone.move.MoveDetector.allMoves;
import static org.nschmidt.abalone.cnn.EvaluationByConvolutedNeuralNetwork.score;

import java.util.Arrays;
import java.util.HashSet;

import org.nschmidt.abalone.playfield.Field;
import org.nschmidt.abalone.playfield.Player;

public class CnnAlphaBetaAI {

    private final int maxDepth;
    private final Player player;
    private Field bestMove;

    public CnnAlphaBetaAI(int maxDepth, Player player) {
        this.maxDepth = maxDepth;
        this.bestMove = null;
        this.player = player;
    }

    public Field bestMove(Field board) {
        alphaBetaPruning(board, player, -Double.MAX_VALUE, Double.MAX_VALUE, 0);
        return bestMove;
    }
    
    public Field bestVariantMove(Field board, int rank) {
        Field localBestMove = null;
        Field[] variants = bestVariantMoves(board, rank);
        if (variants.length >= rank && rank > 0) {
            localBestMove = variants[rank - 1];
        }
        
        if (localBestMove != null)
            bestMove = localBestMove;
        return bestMove;
    }
    
    public Field[] bestVariantMoves(Field board, int count) {
        Field[] tempResult = new Field[count];
        HashSet<Field> movesToExclude = new HashSet<>();
        int variantCount = 0;
        for (int i = 0; i < count; i++) {
            Field[] moves = movesToExclude.toArray(new Field[0]);
            Field variantMove = bestVariantMove(board, moves);
            if (variantMove == null) break;
            movesToExclude.add(variantMove);
            tempResult[i] = variantMove;
            variantCount++;
        }
        
        Field[] result = new Field[variantCount];
        System.arraycopy(tempResult, 0, result, 0, variantCount);
        return result;
    }  
    
    private Field bestVariantMove(Field board, Field... movesToExclude) {
        double alpha = -Double.MAX_VALUE;
        double beta = Double.MAX_VALUE;
        Field[] moves = allMoves(board, player);
        HashSet<Field> movesAsSet = new HashSet<>(Arrays.asList(moves));
        for (Field move : movesToExclude) {
            movesAsSet.remove(move);
        }
        
        moves = movesAsSet.toArray(new Field[0]);
        Arrays.sort(moves, (m1, m2) -> Double.compare(score(m2, player), score(m1, player)));
        
        Field localBestMove = null;
        for (Field move : moves) {
            double score = alphaBetaPruning(move, player.switchPlayer(), alpha, beta, 1);

            if (score > alpha) {
                alpha = score;
                localBestMove = move;
            }

            if (beta <= alpha) {
                break;
            }
        }

        if (localBestMove != null)
            bestMove = localBestMove;
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
