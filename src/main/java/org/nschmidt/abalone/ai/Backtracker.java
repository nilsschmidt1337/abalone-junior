package org.nschmidt.abalone.ai;

import static org.nschmidt.abalone.ai.NextGenAI.bestMove;
import static org.nschmidt.abalone.move.MoveDetector.allMoves;
import static org.nschmidt.abalone.playfield.Field.PIECE_COUNT;
import static org.nschmidt.abalone.playfield.Field.PIECE_COUNT_FOR_WIN;
import static org.nschmidt.abalone.playfield.NextGenFieldEvaluator.score;
import static org.nschmidt.abalone.winning.WinningChecker.wins;
import static org.nschmidt.abalone.winning.WinningInOneMoveChecker.winsInOneMove;
import static org.nschmidt.abalone.winning.WinningInTwoMovesChecker.winsInTwoMoves;
import static org.nschmidt.abalone.winning.GainPieceInOneMoveChecker.gainPieceInOneMove;
import static org.nschmidt.abalone.winning.GainPieceInTwoMovesChecker.gainPieceInTwoMoves;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.nschmidt.abalone.playfield.Field;
import org.nschmidt.abalone.playfield.Player;
import org.nschmidt.abalone.winning.WinningChecker;
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
        
        
        if (WinningChecker.canWin(state, player)) {
            LOGGER.info("Try to find win in one move...");
            Field[] winsInOne = winsInOneMove(state, player);
            if (winsInOne.length == 1) {
                return addToCache(state, winsInOne[0]);
            }
            
            LOGGER.info("Try to find win in two moves...");
            Field[] winsInTwo = winsInTwoMoves(state, player);
            if (winsInTwo.length == 2) {
                return addToCache(state, winsInTwo[0]);
            }
        }
        
        final Player opponent = player.switchPlayer();
        
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
                Arrays.sort(moves2, (m1, m2) -> Integer.compare(score(m2, player), score(m1, player)));
                for (Field move2 : moves2) {
                    if (Field.countPieces(move2, opponent) < Field.countPieces(state, opponent) && !wins(move2, opponent)) {
                        LOGGER.info("Try to find ranked optimum (agressive)");
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
        
        if (player == Player.BLACK) {
            LOGGER.info("Try to find optimum with next gen AI (alpha-beta)...");
            Field nextGenMove = new NextGenAlphaBetaAI(4, player).bestMove(state);
            if (nextGenMove != null) {
                return addToCache(state, nextGenMove);
            }
        }
        
        LOGGER.info("Try to find optimum with next gen AI...");
        Field nextGenMove = bestMove(state, player);
        return addToCache(state, nextGenMove);
    }
    
    
    static Field addToCache(Field state, Field response) {
        MOVE_CACHE.put(state, response);
        return response;
    }
    
    public static void clearCache() {
        MOVE_CACHE.clear();
    }
}
