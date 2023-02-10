package org.nschmidt.abalone.playfield;

import static org.nschmidt.abalone.playfield.Adjacency.adjacency;
import static org.nschmidt.abalone.playfield.Field.FIELD_SIZE;
import static org.nschmidt.abalone.playfield.Field.lookAtField;

import java.util.HashMap;
import java.util.Map;

import org.nschmidt.abalone.move.MoveDetector;
import org.nschmidt.abalone.winning.WinningChecker;

public enum FieldEvaluator {
    INSTANCE;
    
    // dy 0.866
    // dx 0.5
    
    private static final double[][] COORDS = createCoords();
    private static final double[] CENTER = COORDS[FIELD_SIZE / 2];
    
    private static final double CENTER_WEIGHT = 0.41;
    private static final double PLAYER_WEIGHT = (1.0 - FieldEvaluator.CENTER_WEIGHT) / 2.0;
    
    public static boolean firstBloodPenalty = false;
    public static double pieceValue = 1000;
    
    private static final Map<ScoreCacheEntry, Double> CACHE = new HashMap<>();
    
    public static double score(Field state, Player player, Player toMove) {
        ScoreCacheEntry key = ScoreCacheEntry.of(state, player, toMove);
        if (CACHE.size() > 10_000_000) {
            System.err.println("Cache cleared.");
            CACHE.clear();
        }
        Double result = CACHE.get(key);
        if (result == null) {
            result = scoreCalc(state, player, toMove);
            CACHE.put(new ScoreCacheEntry(state, player, toMove), result);
        }
        return result;
    }
    
    public static double scoreCalc(Field state, Player player, Player toMove) {
        final Player opponent = player.switchPlayer();
        double score = 0;
        
        int opponentPieceCount = 0; 
        int playerPieceCount = 0;
        
        double playerX = 0;
        double playerY = 0;
        
        double opponentX = 0;
        double opponentY = 0;
        
        for (int i = 0; i < FIELD_SIZE; i++) {
            final Player currentPiece = lookAtField(state, i);
            if (currentPiece == player) {
                playerX += COORDS[i][0];
                playerY += COORDS[i][1];
                playerPieceCount += 1;
            } else if (currentPiece == opponent) {
                opponentX += COORDS[i][0];
                opponentY += COORDS[i][1];
                opponentPieceCount += 1;
            }
        }
        
        playerX /= playerPieceCount;
        playerY /= playerPieceCount;
        opponentX /= opponentPieceCount;
        opponentY /= opponentPieceCount;
        
        double referenceX = 0;
        double referenceY = 0;
        
        referenceX = playerX * PLAYER_WEIGHT + opponentX * PLAYER_WEIGHT + CENTER[0] * CENTER_WEIGHT;
        referenceY = playerY * PLAYER_WEIGHT + opponentY * PLAYER_WEIGHT + CENTER[1] * CENTER_WEIGHT;
        
        double sumPlayer = 0;
        double sumOpponent = 0;
        for (int i = 0; i < FIELD_SIZE; i++) {
            final Player currentPiece = lookAtField(state, i);
            if (currentPiece == player) {
                sumPlayer += Math.abs(referenceX - COORDS[i][0]) + Math.abs(referenceY - COORDS[i][1]);
            } else if (currentPiece == opponent) {
                sumOpponent += Math.abs(referenceX - COORDS[i][0]) + Math.abs(referenceY - COORDS[i][1]);
            }
        }
        
        int lostPieces = Field.PIECE_COUNT - playerPieceCount;
        int lostOpponentPieces = Field.PIECE_COUNT - opponentPieceCount;
        int lostDelta = lostOpponentPieces - lostPieces;
        score = sumOpponent - sumPlayer + lostDelta * pieceValue;
        
        if (player == toMove) {
            if (firstBloodPenalty && lostDelta == 0 && WinningChecker.gainPiece(state, player)) score -= pieceValue;
            return detectEndgame(state, score, player, opponent, lostPieces, lostOpponentPieces, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        } else {
            if (firstBloodPenalty && lostDelta == 0 && WinningChecker.gainPiece(state, opponent)) score += pieceValue;
            return detectEndgame(state, score, opponent, player, lostOpponentPieces, lostPieces, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
        }
    }
    
    private static double detectEndgame(Field state, double score, Player playerA, Player playerB, int lostPiecesA, int lostPiecesB, double looseResult, double winResult) {
        if (lostPiecesA >= Field.PIECE_COUNT_FOR_WIN_MINUS_ONE && WinningChecker.wins(state, playerB)) {
            return looseResult;
        } else if (lostPiecesB >= Field.PIECE_COUNT_FOR_WIN_MINUS_ONE && WinningChecker.wins(state, playerA)) {
            for (Field move : MoveDetector.allMoves(state, playerB)) {
                if (!WinningChecker.wins(move, playerA)) {
                    return score;
                }
            }
            
            return winResult;
        }
        
        return score;
    }

    static double[][] createCoords() {
        double[][] result = new double[FIELD_SIZE][2];
        for (int i = 0; i < FIELD_SIZE; i++) {
            double x = 0;
            double y = 0;
            
            int j = i;
            while (adjacency(j)[Adjacency.TOP_RIGHT] != -1) {
                j = adjacency(j)[Adjacency.TOP_RIGHT];
                y += 0.866;
                x -= 0.5;
            }
            
            while (adjacency(j)[Adjacency.TOP_LEFT] != -1) {
                j = adjacency(j)[Adjacency.TOP_LEFT];
                y += 0.866;
                x += 0.5;
            }
            
            while (adjacency(j)[Adjacency.LEFT] != -1) {
                j = adjacency(j)[Adjacency.LEFT];
                x += 1;
            }
            
            result[i][0] = x;
            result[i][1] = y;
        }
        
        return result;
    }
}
