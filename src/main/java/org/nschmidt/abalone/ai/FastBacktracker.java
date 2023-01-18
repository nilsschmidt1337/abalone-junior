package org.nschmidt.abalone.ai;

import static org.nschmidt.abalone.ai.AI.bestMove;
import static org.nschmidt.abalone.move.MoveDetector.allMoves;
import static org.nschmidt.abalone.playfield.Field.PIECE_COUNT;
import static org.nschmidt.abalone.playfield.Field.PIECE_COUNT_FOR_WIN;
import static org.nschmidt.abalone.playfield.FieldEvaluator.score;
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
import org.nschmidt.abalone.winning.WinningInOneMoveChecker;
import org.nschmidt.abalone.winning.WinningInTwoMovesChecker;

public enum FastBacktracker {
    INSTANCE;
    
    private static final Map<Field, Field> MOVE_CACHE_BLACK = new HashMap<>();
    private static final Map<Field, Field> MOVE_CACHE_WHITE = new HashMap<>();
    
    public static Field backtrack(Field state, Player player) {
        if (player == Player.WHITE && MOVE_CACHE_WHITE.containsKey(state)) {
            return MOVE_CACHE_WHITE.get(state);
        } else if (player == Player.BLACK && MOVE_CACHE_BLACK.containsKey(state)) {
            return MOVE_CACHE_BLACK.get(state);
        }
        
        
        if (WinningChecker.canWin(state, player)) {
            Field[] winsInOne = winsInOneMove(state, player);
            if (winsInOne.length == 1) {
                return addToCache(player, state, winsInOne[0]);
            }
            
            Field[] winsInTwo = winsInTwoMoves(state, player);
            if (winsInTwo.length == 2) {
                return addToCache(player, state, winsInTwo[0]);
            }
        }
        
        final Player opponent = player.switchPlayer();
        
        final Field[] moves = allMoves(state, player);
        for (Field move : moves) {
            if (Field.countPieces(move, opponent) < Field.countPieces(state, opponent)) {
                Field aggressiveMove = new AggressiveAlphaBetaAI(2, player).bestMove(state);
                if (aggressiveMove != null && Field.countPieces(aggressiveMove, opponent) < Field.countPieces(state, opponent)) {
                    if (!wins(aggressiveMove, opponent) 
                            && WinningInOneMoveChecker.winsInOneMove(aggressiveMove, opponent).length == 0
                            && WinningInTwoMovesChecker.winsInTwoMoves(aggressiveMove, opponent).length == 0) {
                        return addToCache(player, state, aggressiveMove);
                    }
                }
                Field[] moves2 = allMoves(state, player);
                Arrays.sort(moves2, (m1, m2) -> Integer.compare(score(m2, player), score(m1, player)));
                for (Field move2 : moves2) {
                    if (Field.countPieces(move2, opponent) < Field.countPieces(state, opponent) 
                            && !wins(move2, opponent)
                            && WinningInOneMoveChecker.winsInOneMove(move2, opponent).length == 0
                            && WinningInTwoMovesChecker.winsInTwoMoves(move2, opponent).length == 0) {
                        
                        return addToCache(player, state, move2);
                    }
                }
                
                break;
            }
        }
        
        if (PIECE_COUNT_FOR_WIN > 1) {
            int lostPieces = PIECE_COUNT - Field.countPieces(state, opponent) + 1;
            if (lostPieces < PIECE_COUNT_FOR_WIN) {
                Field[] gainInOne = gainPieceInOneMove(state, player);
                if (gainInOne.length == 1) {
                    return addToCache(player, state, gainInOne[0]);
                }
                
                Field[] gainInTwo = gainPieceInTwoMoves(state, player);
                if (gainInTwo.length == 2) {
                    return addToCache(player, state, gainInTwo[0]);
                }
            }
        }
        
        Field nextGenMove = new AlphaBetaAI(2, player).bestMove(state);
        if (nextGenMove != null) {
            return addToCache(player, state, nextGenMove);
        }
        
        nextGenMove = bestMove(state, player);
        return addToCache(player, state, nextGenMove);
    }
    
    
    static Field addToCache(Player player, Field state, Field response) {
        if (player == Player.WHITE) {
            MOVE_CACHE_WHITE.put(state, response);
        } else if (player == Player.BLACK) {
            MOVE_CACHE_BLACK.put(state, response);
        }
        
        return response;
    }
    
    public static void clearCache() {
        MOVE_CACHE_WHITE.clear();
        MOVE_CACHE_BLACK.clear();
    }
}
