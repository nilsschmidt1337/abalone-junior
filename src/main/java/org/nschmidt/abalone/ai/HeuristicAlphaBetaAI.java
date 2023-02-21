package org.nschmidt.abalone.ai;

import static org.nschmidt.abalone.move.MoveDetector.allMoves;
import static org.nschmidt.abalone.playfield.FieldEvaluator.score;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.nschmidt.abalone.move.MoveDetector;
import org.nschmidt.abalone.playfield.Field;
import org.nschmidt.abalone.playfield.FieldEvaluator;
import org.nschmidt.abalone.playfield.FieldPrinter;
import org.nschmidt.abalone.playfield.Player;
import org.nschmidt.abalone.playfield.Transposition;
import org.nschmidt.abalone.winning.WinningChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HeuristicAlphaBetaAI {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(HeuristicAlphaBetaAI.class);
    private static final Map<Transposition, Double> TRANSPOSITION = new HashMap<>();
    private static double[][] FUNNEL = new double[][] {{0.0, 0.0},
        {-9.4, 9.4}, {-9.2, 9.2}, {-8.9, 8.9}, 
        {-8.0, 8.0}, {-7.0, 7.0}, {-6.0, 6.0},
        {-5.0, 5.0}};
        
    private static double[][] NO_FUNNEL = new double[][] {{0.0, 0.0},
            {-1000.0, 1000.0},{-1000.0, 1000.0},{-1000.0, 1000.0},
            {-1000.0, 1000.0},{-1000.0, 1000.0},{-1000.0, 1000.0},
            {-1000.0, 1000.0},{-1000.0, 1000.0},{-1000.0, 1000.0}};
            
    private static double[][] HEURISTIC_FUNNEL = new double[][] {{0.0, 0.0},
        {-9.4, 9.4}, {-9.2, 9.2}, {-8.9, 8.9}, 
        {-8.0, 8.0}, {-7.0, 7.0}, {-6.0, 6.0},
        {-5.0, 5.0}, {-8.0, 8.0}, {-8.0, 8.0}};
        
    private static final Double[] INITIAL = new Double[16];
    private static final double[] BEST = new double[16];
    private static final double[] WORST = new double[16];
    private static final Random RND = new Random();
    
    private final int maxDepth;
    private final int maxDepthMinusTwo;
    private final Player player;
    private Field bestMove;
    
    private long filteredOut = 0;
    private long passed = 0;
    private long endgame = 0;

    public HeuristicAlphaBetaAI(int maxDepth, Player player) {
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
        
     // LOGGER.info("filteredOut {}", filteredOut);
     // LOGGER.info("passed      {}", passed);
     // LOGGER.info("endgame     {}", endgame);
        filteredOut = 0;
        passed = 0;
        return bestMove;
    }
    
    private double alphaBetaPruning(Field board, Player team, double alpha, double beta, int depth) {
        final boolean maximize = team == player;

        if (depth++ == maxDepth) {
            return score(board, player, team);
        }
        
        Field[] moves = allMoves(board, team);
        final int d = depth;
        Set<Field> uniqueMoves = new HashSet<>(Arrays.asList(moves));
         
        if (maximize) {
            Set<Field> sortedMoves = new TreeSet<>((m1, m2) -> Double.compare(score(m2, player, team), score(m1, player, team)));
            sortedMoves.addAll(uniqueMoves);
            
            Field localBestMove = null;
            int count = 0;
            for (Field move : sortedMoves) {
                double score = score(move, player, team);
                if (between(FUNNEL[d][0], FUNNEL[d][1], INITIAL[depth], score)) {
                    score = alphaBetaPruning(move, team.switchPlayer(), alpha, beta, depth);
                }
                
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
                    score = score(move, player, team);
                    if (between(FUNNEL[d][0], FUNNEL[d][1], INITIAL[depth], score)) {
                        score = alphaBetaPruning(move, team.switchPlayer(), alpha, beta, depth);
                    }
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

    private boolean between(double dmin, double dmax, Double initialScore, double score) {
        if (initialScore == null) return true;
        if (score < dmax + initialScore && score >= initialScore + dmin) {
            passed++;
            return true;
        } else if (score == Double.NEGATIVE_INFINITY || score == Double.POSITIVE_INFINITY) {
            endgame++;
            return false;
        } else {
            filteredOut++;
            return false;
        }
    }
    
    public static void main(String[] args) {
        Map<String, Integer> wins = new TreeMap<>();
        wins.put("Variant A", 0);
        wins.put("Variant B", 0);
        wins.put("draw", 0);
        for (int j = 0; j < 5; j++)
            for (int i = 0; i < 100; i++) {
                
                Player currentPlayer;
                Field currentField;
                currentField = Field.INITIAL_FIELD;
                currentPlayer = Player.BLACK;
                Field[] allMoves = MoveDetector.allMoves(currentField, currentPlayer);
                currentField = allMoves[RND.nextInt(allMoves.length)];
                currentPlayer = Player.WHITE;
                if (RND.nextBoolean()) {
                    allMoves = MoveDetector.allMoves(currentField, currentPlayer);
                    currentField = allMoves[RND.nextInt(allMoves.length)];
                    currentPlayer = Player.BLACK;
                }
                Player start = currentPlayer;
                Player r = playGame(currentPlayer, currentField, i);
                
                if (start == Player.BLACK) {
                    switch (r) {
                    case BLACK:
                        wins.put("Variant A", wins.getOrDefault("Variant A", 0) + 1);
                        break;
                    case WHITE:
                        wins.put("Variant B", wins.get("Variant B") + 1);
                        break;
                    case EMPTY:
                        wins.put("draw", wins.getOrDefault("draw" , 0) + 1);
                        break;
                    }
                } else {
                    switch (r) {
                    case BLACK:
                        wins.put("Variant B", wins.get("Variant B") + 1);
                        break;
                    case WHITE:
                        wins.put("Variant A", wins.getOrDefault("Variant A", 0) + 1);
                        break;
                    case EMPTY:
                        wins.put("draw", wins.getOrDefault("draw" , 0) + 1);
                        break;
                    }
                }
                
                for (Entry<String, Integer> entry : wins.entrySet()) {
                    LOGGER.info("{} {}", entry.getKey(), entry.getValue());
                }
            }
        
        for (Entry<String, Integer> entry : wins.entrySet()) {
            LOGGER.info("{} {}", entry.getKey(), entry.getValue());
        }
    }
    
    private static Player playGame(Player currentPlayer, Field currentField, int gameNumber) {
        FUNNEL = HEURISTIC_FUNNEL;
        HEURISTIC_FUNNEL[0][0] = 0.0;
        HEURISTIC_FUNNEL[0][1] = 0.0;
        HEURISTIC_FUNNEL[1][0] = -9.4;
        HEURISTIC_FUNNEL[1][1] = 9.4;
        HEURISTIC_FUNNEL[2][0] = -9.2;
        HEURISTIC_FUNNEL[2][1] = 9.2;
        HEURISTIC_FUNNEL[3][0] = -8.9;
        HEURISTIC_FUNNEL[3][1] = 8.9;
        HEURISTIC_FUNNEL[4][0] = -8.0;
        HEURISTIC_FUNNEL[4][1] = 8.0;
        HEURISTIC_FUNNEL[5][0] = -7.0;
        HEURISTIC_FUNNEL[5][1] = 7.0;
        HEURISTIC_FUNNEL[6][0] = -6.0;
        HEURISTIC_FUNNEL[6][1] = 6.0;
        HEURISTIC_FUNNEL[7][0] = -5.0;
        HEURISTIC_FUNNEL[7][1] = 5.0;
        HEURISTIC_FUNNEL[8][0] = -4.0;
        HEURISTIC_FUNNEL[8][1] = 4.0;
        int moves = 1;
        FieldEvaluator.firstBloodPenalty = true;
        Set<Field> previousMoves = new HashSet<>();
        final Player startPlayer = currentPlayer;
        Field previousField;
        while (!WinningChecker.wins(currentField, currentPlayer)) {
            Field answer;
            final String variant;
            if (startPlayer == currentPlayer) { // Variant A
                variant = "Variant A";
                answer = doVariationNoHeuristics(currentPlayer, currentField, previousMoves, 7);
            } else { // Variant B
                variant = "Variant B";
                answer = doVariation(currentPlayer, currentField, previousMoves, 8);
            }
            
            previousField = currentField;
            currentField = answer;
            // LOGGER.info("iteration {}; move {}; player {}; score {}; {}; board {}", gameNumber, moves, currentPlayer, score(currentField, currentPlayer, currentPlayer), variant, FieldPrinter.buildStandardFieldDeltaString(currentField, previousField));
            currentPlayer = currentPlayer.switchPlayer();
            moves++;
            
            if (moves > 10) {
                break;
            }
        }
        
        if (WinningChecker.wins(currentField, currentPlayer)) {
            return currentPlayer;
        }
        
        if (score(currentField, currentPlayer, currentPlayer) > score(currentField, currentPlayer.switchPlayer(), currentPlayer.switchPlayer()) ) {
            return currentPlayer;
        } else {
            return currentPlayer.switchPlayer();
        }
        
        // return Player.EMPTY;
    }
    
    private static Field doVariation(Player currentPlayer, Field currentField, Set<Field> previousMoves, int level) {
        Field answer = null;
        if (previousMoves.contains(currentField)) {
            HeuristicAlphaBetaAI ai = new HeuristicAlphaBetaAI(level, currentPlayer);
            Field[] variants = ai.bestVariantMoves(currentField, 2);
                        
            for (Field variant : variants) {
                if (!previousMoves.contains(variant)) {
                    answer = variant;
                    break;
                }
            }
            
            if (answer == null && variants.length > 0) {
                answer = variants[0];
            }
        } else {
            previousMoves.add(currentField);
            HeuristicAlphaBetaAI ai = new HeuristicAlphaBetaAI(level, currentPlayer);
            answer = ai.bestMove(currentField);
        }
        
        if (answer == null) {
            answer = AI.bestMove(currentField, currentPlayer);
        }
        return answer;
    }
    
    private static Field doVariationNoHeuristics(Player currentPlayer, Field currentField, Set<Field> previousMoves, int level) {
        Field answer = null;
        if (previousMoves.contains(currentField)) {
            TranspositionalAlphaBetaAI ai = new TranspositionalAlphaBetaAI(level, currentPlayer);
            Field[] variants = ai.bestVariantMoves(currentField, 2);
                        
            for (Field variant : variants) {
                if (!previousMoves.contains(variant)) {
                    answer = variant;
                    break;
                }
            }
            
            if (answer == null && variants.length > 0) {
                answer = variants[0];
            }
        } else {
            previousMoves.add(currentField);
            TranspositionalAlphaBetaAI ai = new TranspositionalAlphaBetaAI(level, currentPlayer);
            answer = ai.bestMove(currentField);
        }
        
        if (answer == null) {
            answer = AI.bestMove(currentField, currentPlayer);
        }
        return answer;
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
