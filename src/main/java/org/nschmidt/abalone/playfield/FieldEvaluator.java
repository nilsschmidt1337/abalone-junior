package org.nschmidt.abalone.playfield;

import static org.nschmidt.abalone.playfield.Adjacency.adjacency;
import static org.nschmidt.abalone.playfield.Field.FIELD_SIZE;
import static org.nschmidt.abalone.playfield.Field.lookAtField;

public enum FieldEvaluator {
    INSTANCE;
    
    // dy 0.866
    // dx 0.5
    
    private static final double[][] COORDS = createCoords();
    private static final double[] CENTER = COORDS[FIELD_SIZE / 2];
    
    public static double score(Field state, Player player) {
        final Player opponent = player.switchPlayer();
        double score = 0;
        
        double opponentPieceCount = 0; 
        double playerPieceCount = 0;
        
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
        
        referenceX = playerX * 0.2 + opponentX * 0.2 + CENTER[0] * 0.6;
        referenceY = playerY * 0.2 + opponentY * 0.2 + CENTER[1] * 0.6;
        
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
        
        score = sumOpponent - sumPlayer - (Field.PIECE_COUNT - playerPieceCount) * 1000 + (Field.PIECE_COUNT - opponentPieceCount) * 1000;
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
