package org.nschmidt.abalone.ai;

import static org.nschmidt.abalone.move.MoveDetector.allMoves;
import static org.nschmidt.abalone.playfield.Field.FIELD_SIZE;
import static org.nschmidt.abalone.playfield.Field.PIECE_COUNT;
import static org.nschmidt.abalone.playfield.Field.PIECE_COUNT_FOR_WIN;
import static org.nschmidt.abalone.playfield.FieldEvaluator.score;
import static org.nschmidt.abalone.winning.WinningChecker.wins;
import static org.nschmidt.abalone.winning.WinningInOneMoveChecker.winsInOneMove;
import static org.nschmidt.abalone.winning.WinningInThreeMovesChecker.winsInThreeMoves;
import static org.nschmidt.abalone.winning.WinningInTwoMovesChecker.winsInTwoMoves;
import static org.nschmidt.abalone.winning.GainPieceInOneMoveChecker.gainPieceInOneMove;
import static org.nschmidt.abalone.winning.GainPieceInTwoMovesChecker.gainPieceInTwoMoves;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.nschmidt.abalone.move.MoveDetector;
import org.nschmidt.abalone.playfield.Field;
import org.nschmidt.abalone.playfield.FieldEvaluator;
import org.nschmidt.abalone.playfield.Player;
import org.nschmidt.abalone.winning.WinningInOneMoveChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum Backtracker {
    INSTANCE;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Backtracker.class);
    
    private static final Map<Field, Field> MOVE_CACHE = new HashMap<>();
    
    public static Field backtrack(Field state, Player player, int depth) {
        LOGGER.info("Try to find opening move...");
        Field openingMove = HardcodedOpenings.findOpeningMove(state, player);
        if (openingMove != null) {
            return openingMove;
        }
        
        LOGGER.info("Try to find a cached move...");
        if (MOVE_CACHE.containsKey(state)) {
            return MOVE_CACHE.get(state);
        }
        
        LOGGER.info("Try to find win in one move...");
        final Player opponent = player.switchPlayer();
        Field[] winsInOne = winsInOneMove(state, player);
        if (winsInOne.length == 1) {
            return addToCache(state, winsInOne[0]);
        }
        
        LOGGER.info("Try to find win in two moves...");
        Field[] winsInTwo = winsInTwoMoves(state, player);
        if (winsInTwo.length == 2) {
            return addToCache(state, winsInTwo[0]);
        }
        
        Field[] moves = allMoves(state, player);
        Field alphaBetaMoveV2 = null;
        for (Field move : moves) {
            if (Field.countPieces(move, opponent) < Field.countPieces(state, opponent)) {
                LOGGER.info("Try to find optimum with agressive alpha-beta V2 search...");
                alphaBetaMoveV2 = new AlphaBetaAIV2(6, player).bestMove(state);
                if (alphaBetaMoveV2 != null && Field.countPieces(alphaBetaMoveV2, opponent) < Field.countPieces(state, opponent)) {
                    return addToCache(state, alphaBetaMoveV2);
                }
                Field[] moves2 = allMoves(state, player);
                Arrays.sort(moves2, (m1, m2) -> Double.compare(score(m2, player), score(m1, player)));
                for (Field move2 : moves2) {
                    if (Field.countPieces(move2, opponent) < Field.countPieces(state, opponent) && !wins(move2, opponent)) {
                        LOGGER.info("Try to find ranked optimum with agressive alpha-beta V2 search...");
                        return addToCache(state, move2);
                    }
                }
                
                break;
            }
        }
        
        if (PIECE_COUNT_FOR_WIN > 1) {
            int lostPieces = PIECE_COUNT - Field.countPieces(state, opponent) + 1;
            if (lostPieces < PIECE_COUNT_FOR_WIN) {
                LOGGER.info("Try to gain piece in one move...");
                Field[] gainInOne = gainPieceInOneMove(state, player);
                if (gainInOne.length == 1) {
                    return addToCache(state, gainInOne[0]);
                }
                
                LOGGER.info("Try to gain piece in two moves...");
                Field[] gainInTwo = gainPieceInTwoMoves(state, player);
                if (gainInTwo.length == 2) {
                    return addToCache(state, gainInTwo[0]);
                }
            }
        }
        
        LOGGER.info("Try to find optimum with alpha-beta search...");
        Field alphaBetaMove = new AlphaBetaAI(4, player).bestMove(state);
        if (alphaBetaMove != null) {
            return addToCache(state, alphaBetaMove);
        }
        
        if (alphaBetaMoveV2 != null) {
            LOGGER.info("Using fallback alpha-beta V2 search...");
            return addToCache(state, alphaBetaMoveV2);
        }
            
        
        LOGGER.info("Try to find optimum in two moves...");
        Field[] optimumInThree = OptimisationInTwoMovesChecker.optimisationInTwoMoves(state, player);
        if (optimumInThree.length == 2) {
            return addToCache(state, optimumInThree[0]);
        }
        
        
        double maxScore = -Double.MAX_VALUE;
        Field maxMove = null;
        for (Field move : moves) {
            // Mache keinen Zug, bei dem der Gegner in einem Zug gewinnt
            Field[] oppenentWinsInOne = WinningInOneMoveChecker.winsInOneMove(move, opponent);
            if (oppenentWinsInOne.length == 1) {
                continue;
            }
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
        
        if (score(state, player) == -Double.MAX_VALUE) {
            LOGGER.info("Try to escape a dangerous situation...");
            boolean notEscaped = true;
            Arrays.sort(moves, (m1, m2) -> Double.compare(score(m2, player), score(m1, player)));
            for (Field move : moves) {
                double score = score(move, player);
                if (score > maxScore) {
                    // Mache keinen Zug, bei dem der Gegner in einem Zug gewinnt
                    Field[] oppenentWinsInOne = WinningInOneMoveChecker.winsInOneMove(move, opponent);
                    if (oppenentWinsInOne.length == 1) {
                        LOGGER.info("Opponent can win in one step...");
                        continue;
                    }
                    // Mache keinen Zug, bei dem der Gegner in zwei Zügen gewinnt
                    Field[] oppenentWinsInTwo = winsInTwoMoves(move, opponent);
                    if (oppenentWinsInTwo.length == 2) {
                        LOGGER.info("Opponent can win in two steps...");
                        continue;
                    }
                    
                    LOGGER.info("Able to escape... (Score {})", score);
                    notEscaped = false;
                    maxScore = score;
                    maxMove = move;
                }
            }
            
            if (notEscaped) {
                for (Field move : moves) {
                    double score = score(move, player);
                    if (score > maxScore) {
                        // Mache keinen Zug, bei dem der Gegner in einem Zug gewinnt
                        Field[] oppenentWinsInOne = WinningInOneMoveChecker.winsInOneMove(move, opponent);
                        if (oppenentWinsInOne.length == 1) {
                            LOGGER.info("Opponent can win in one step...");
                            continue;
                        }
                        
                        LOGGER.info("Able to escape, but opponent can win in two steps... (Score {})", score);
                        maxScore = score;
                        maxMove = move;
                    }
                }
            }
            
            return addToCache(state, maxMove);
        }
        
        LOGGER.info("Try to find fallback optimum with agressive alpha-beta V2 search...");
        alphaBetaMoveV2 = new AlphaBetaAIV2(4, player).bestMove(state);
        if (alphaBetaMoveV2 != null) {
            return addToCache(state, alphaBetaMoveV2);
        }
        
        boolean foundStrategicSolution = false;
        LOGGER.info("Try to find a strategic solution...");
        Arrays.sort(moves, (m1, m2) -> Double.compare(score(m2, player), score(m1, player)));
        for (Field move : moves) {
            double initialScore = score(move, player);
            Field result = playRound(move, opponent, depth);
            double score = score(result, player);
            if (score > maxScore && initialScore > maxScore) {
                // Mache keinen Zug, bei dem der Gegner eine zusätzliche Figur isolieren kann
                final Set<Integer> isolatedPieces = new TreeSet<>();
                final int originalIsolatedPiecesCount;
                for (int i = 0; i < FIELD_SIZE; i++) {
                    if (FieldEvaluator.isIsolated(move, player, i)) {
                        isolatedPieces.add(i);
                    }
                }
                originalIsolatedPiecesCount = isolatedPieces.size();
                for (Field attack : MoveDetector.allAttackMoves(move, opponent)) {
                    for (int i = 0; i < FIELD_SIZE; i++) {
                        if (FieldEvaluator.isIsolated(attack, player, i)) {
                            isolatedPieces.add(i);
                        }
                    }   
                }
                
                if (isolatedPieces.size() > originalIsolatedPiecesCount) {
                    continue;
                }
                
                // Mache keinen Zug, bei dem der Gegner in einem Zug gewinnt
                Field[] oppenentWinsInOne = winsInOneMove(move, opponent);
                if (oppenentWinsInOne.length == 1) {
                    continue;
                }
                // Mache keinen Zug, bei dem der Gegner in zwei Zügen gewinnt
                Field[] oppenentWinsInTwo = winsInTwoMoves(move, opponent);
                if (oppenentWinsInTwo.length == 2) {
                    continue;
                }
                // Mache keinen Zug, bei dem der Gegner in drei Zügen gewinnt
                Field[] oppenentWinsInThree = winsInThreeMoves(move, opponent);
                if (oppenentWinsInThree.length == 3) {
                    continue;
                }
                
                LOGGER.info("Found a solution... (Score {})", score);
                foundStrategicSolution = true;
                maxScore = initialScore;
                maxMove = move;
            }
        }
        
        if (!foundStrategicSolution) {
            LOGGER.info("Try to find a classic solution...");
            for (Field move : moves) {
                double score = score(move, player);
                if (score > maxScore) {
                    // Mache keinen Zug, bei dem der Gegner in einem Zug gewinnt
                    Field[] oppenentWinsInOne = WinningInOneMoveChecker.winsInOneMove(move, opponent);
                    if (oppenentWinsInOne.length == 1) {
                        continue;
                    }
                    // Lasse zu, dass der Gegner in zwei Zügen gewinnen kann, um den Handlungspielraum zu vergrößern
                    
                    LOGGER.info("Found a classic solution... (Score {})", score);
                    maxScore = score;
                    maxMove = move;
                }
            }
        }
        
        return maxMove;
    }
    
    
    static Field addToCache(Field state, Field response) {
        MOVE_CACHE.put(state, response);
        return response;
    }


    private static Field playRound(Field state, Player currentPlayer, int maxDepth) {
        int depth = 0;
        while (depth < maxDepth) {
            Field[] moves = allMoves(state, currentPlayer);
            
            double maxScore = -Double.MAX_VALUE;
            Field maxMove = moves[0];
            
            for (Field move : moves) {
                double score = score(move, currentPlayer);
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
