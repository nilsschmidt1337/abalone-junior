package org.nschmidt.abalone.ai;

import static org.nschmidt.abalone.ai.AI.bestMove;
import static org.nschmidt.abalone.winning.WinningChecker.wins;
import static org.nschmidt.abalone.winning.WinningInOneMoveChecker.winsInOneMove;
import static org.nschmidt.abalone.winning.WinningInTwoMovesChecker.winsInTwoMoves;
import java.util.HashMap;
import java.util.Map;
import org.nschmidt.abalone.playfield.Field;
import org.nschmidt.abalone.playfield.Player;
import org.nschmidt.abalone.winning.WinningChecker;

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
        
        AlphaBetaAI ai = new AlphaBetaAI(6, player);
        Field nextGenMove = ai.bestMove(state);
        if (nextGenMove != null && !wins(nextGenMove, opponent)) {
            return nextGenMove ;// addToCache(player, state, nextGenMove);
        }
        
        nextGenMove = bestMove(state, player);
        return nextGenMove; // addToCache(player, state, nextGenMove);
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
