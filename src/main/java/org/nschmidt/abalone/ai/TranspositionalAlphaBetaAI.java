package org.nschmidt.abalone.ai;

import static org.nschmidt.abalone.move.MoveDetector.allMoves;
import static org.nschmidt.abalone.playfield.FieldEvaluator.score;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.nschmidt.abalone.playfield.Field;
import org.nschmidt.abalone.playfield.Player;
import org.nschmidt.abalone.playfield.Transposition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TranspositionalAlphaBetaAI {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TranspositionalAlphaBetaAI.class);
    private static final Map<Transposition, Double> TRANSPOSITION = new HashMap<>();
    private static final Double[] INITIAL = new Double[16];
    private static final double[] BEST = new double[16];
    private static final double[] WORST = new double[16];
    
    private final int maxDepth;
    private final int maxDepthMinusTwo;
    private final Player player;
    private Field bestMove;
    
    public TranspositionalAlphaBetaAI(int maxDepth, Player player) {
        this.maxDepth = maxDepth;
        this.maxDepthMinusTwo = Math.max(0, maxDepth - 2);
        this.bestMove = null;
        this.player = player;
    }

    public Field bestMove(Field board) {
        alphaBetaPruning(board, player, -Double.MAX_VALUE, Double.MAX_VALUE, 0);
        for (int i = 1; i <= maxDepth; i++) {
            // LOGGER.info("D{} WORST {} BEST {}", i, WORST[i], BEST[i]);
            WORST[i] = Double.MAX_VALUE;
            BEST[i] = -Double.MAX_VALUE;
            INITIAL[i] = null;
        }
        
        return bestMove;
    }
    
    private double alphaBetaPruning(Field board, Player team, double alpha, double beta, int depth) {
        final boolean maximize = team == player;

        if (depth++ == maxDepth) {
            return score(board, player, team);
        }
        
        Field[] moves = allMoves(board, team);
        Set<Field> uniqueMoves = new HashSet<>(Arrays.asList(moves));
         
        if (maximize) {
            Set<Field> sortedMoves = new TreeSet<>((m1, m2) -> Double.compare(score(m2, player, team), score(m1, player, team)));
            sortedMoves.addAll(uniqueMoves);
            
            Field localBestMove = null;
            int count = 0;
            for (Field move : sortedMoves) {
                double score = alphaBetaPruning(move, team.switchPlayer(), alpha, beta, depth);
                
                if (score > alpha) {
                    alpha = score;
                    localBestMove = move;
                }
                
                if (beta <= alpha) {
                    break;
                }
                
                if (depth == 1) {
                    // LOGGER.info("Progress {}%", count * 100.0 / moves.length);
                    count++;
                }
            }

            if (localBestMove != null && depth == 1) {
                bestMove = localBestMove;
            }
            
            if (localBestMove != null) {
                double s = score(localBestMove, player, team);
                if (INITIAL[depth] == null) INITIAL[depth] = s;
                BEST[depth] = Math.max(BEST[depth], s - INITIAL[depth]);
                WORST[depth] = Math.min(WORST[depth], s - INITIAL[depth]);
            }

            return alpha;
        } else {
            Set<Field> sortedMoves = new TreeSet<>((m1, m2) -> Double.compare(score(m1, player, team), score(m2, player, team)));
            sortedMoves.addAll(uniqueMoves);
            
            Field localBestMove = null;
            final boolean twoMoves = depth >= 4 && depth < maxDepthMinusTwo;
            for (Field move : sortedMoves) {
                Transposition t =  twoMoves ? Transposition.of(depth, move) : null;
                Double score = twoMoves ? TRANSPOSITION.get(t) : null;
                if (score == null) {
                    score = alphaBetaPruning(move, team.switchPlayer(), alpha, beta, depth);
                    if (twoMoves) TRANSPOSITION.put(new Transposition(depth, move), score);
                } else {
                    if (TRANSPOSITION.size() > 1_000_000) TRANSPOSITION.clear();
                    TRANSPOSITION.remove(t);
                } 

                if (score < beta) {
                    localBestMove = move;
                    beta = score;
                }
                
                if (beta <= alpha) {
                    break;
                }
            }
            
            if (localBestMove != null) {
                double s = score(localBestMove, player, team);
                if (INITIAL[depth] == null) INITIAL[depth] = s;
                BEST[depth] = Math.max(BEST[depth], s - INITIAL[depth]);
                WORST[depth] = Math.min(WORST[depth], s - INITIAL[depth]);
            }

            return beta;
        }
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
        Arrays.sort(moves, (m1, m2) -> Double.compare(score(m2, player, player), score(m1, player, player)));
        
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
}
