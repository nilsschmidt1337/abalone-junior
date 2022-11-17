package org.nschmidt.abalone.ai;

import java.util.Arrays;
import java.util.Random;

import org.nschmidt.abalone.move.MoveDetector;
import org.nschmidt.abalone.playfield.Field;
import org.nschmidt.abalone.playfield.Player;

public enum NextGenAI {
    INSTANCE;
    
    private static final Random RND = new Random(1337L);
    
    private static int Size = 9;
    //Create a grid with the grade values for best move
    private static int[][] fieldValue = new int[Size][];
    
    private static int[][] gameField = new int[Size][Size];
    static {
    fieldValue[0] = new int[]{0, 0, 0, 0, 45, 45, 45, 45, 45};
    fieldValue[1] = new int[]{0, 0, 0, 45, 50, 50, 50, 50, 45};
    fieldValue[2] = new int[]{0, 0, 45, 50, 53, 53, 53, 50, 45};
    fieldValue[3] = new int[]{0, 45, 50, 53, 54, 54, 53, 50, 45};
    fieldValue[4] = new int[]{45, 50, 53, 54, 54, 54, 53, 50, 45};
    fieldValue[5] = new int[]{45, 50, 53, 54, 54, 53, 50, 45, 0};
    fieldValue[6] = new int[]{45, 50, 53, 53, 53, 50, 45, 0, 0};
    fieldValue[7] = new int[]{45, 50, 50, 50, 50, 45, 0, 0, 0};
    fieldValue[8] = new int[]{45, 45, 45, 45, 45, 0, 0, 0, 0};
    }
    
  //Game grid mapping to find best move
    private static int[][] CI = new int[24][];
    private static int[][] CJ = new int[24][];
    static {
    CI[0] = new int[]{4,-1, 0, 1};
    CJ[0] = new int[]{0, 1, 1, 0};
    CI[1] = new int[]{5,-1, 0};
    CJ[1] = new int[]{0, 1, 1};
    CI[2] = new int[]{6,-1, 0};
    CJ[2] = new int[]{0, 1, 1};
    CI[3] = new int[]{7,-1, 0};
    CJ[3] = new int[]{0, 1, 1};
    CI[4] = new int[]{8,-1,-1, 0};
    CJ[4] = new int[]{0, 0, 1, 1};
    CI[5] = new int[]{8,-1,-1};
    CJ[5] = new int[]{1, 0, 1};
    CI[6] = new int[]{8,-1,-1};
    CJ[6] = new int[]{2, 0, 1};
    CI[7] = new int[]{8,-1,-1};
    CJ[7] = new int[]{3, 0, 1};
    CI[8] = new int[]{8, 0,-1,-1};
    CJ[8] = new int[]{4,-1, 0, 1};
    CI[9] = new int[]{7, 0,-1};
    CJ[9] = new int[]{5,-1, 0};
    CI[10] = new int[]{6, 0,-1};
    CJ[10] = new int[]{6,-1, 0};
    CI[11] = new int[]{5, 0,-1};
    CJ[11] = new int[]{7,-1, 0};
    CI[12] = new int[]{4, 1, 0,-1};
    CJ[12] = new int[]{8,-1,-1, 0};
    CI[13] = new int[]{3, 1, 0};
    CJ[13] = new int[]{8,-1,-1};
    CI[14] = new int[]{2, 1, 0};
    CJ[14] = new int[]{8,-1,-1};
    CI[15] = new int[]{1, 1, 0};
    CJ[15] = new int[]{8,-1,-1};
    CI[16] = new int[]{0, 1, 1, 0};
    CJ[16] = new int[]{8, 0,-1,-1};
    CI[17] = new int[]{0, 1, 1};
    CJ[17] = new int[]{7, 0,-1};
    CI[18] = new int[]{0, 1, 1};
    CJ[18] = new int[]{6, 0,-1};
    CI[19] = new int[]{0, 1, 1};
    CJ[19] = new int[]{5, 0,-1};
    CI[20] = new int[]{0, 0, 1, 1};
    CJ[20] = new int[]{4, 1, 0,-1};
    CI[21] = new int[]{1, 0, 1};
    CJ[21] = new int[]{3, 1, 0};
    CI[22] = new int[]{2, 0, 1};
    CJ[22] = new int[]{2, 1, 0};
    CI[23] = new int[]{3, 0, 1};
    CJ[23] = new int[]{1, 1, 0};
    }
    
    public static Field getBestMove(Field state, Player currentPlayer) {
        Field[] moves = MoveDetector.allMoves(state, currentPlayer);
        Arrays.sort(moves, (m1, m2) -> Integer.compare(evalGameField(m2, currentPlayer), evalGameField(m1, currentPlayer)));
        
        if (moves.length == 0) return state;
        if (moves.length == 1) return moves[0];
        
        if (RND.nextBoolean() && evalGameField(moves[0], currentPlayer) == evalGameField(moves[1], currentPlayer)) {
            return moves[1];
        }
        
        return state;
    }

    private static int evalGameField(Field state, Player currentPlayer) {
        int checkCurrentPlayer = currentPlayer.getNumber();
        int ii = 0;
        int jj = 0;
        int vv = 0;
        for (jj = 0; jj < Size; jj++) {
            for (ii = 0; ii < Size; ii++) {
                if (gameField[ii][jj] >= 0) {
                    vv += fieldValue[ii][jj] * (2 * gameField[ii][jj] - 1);
                }
            }
        }
        if (checkCurrentPlayer == 0) {
            vv *= -1;
        }
        for (ii = 0; ii < 24; ii++)
        {
            if (gameField[CI[ii][0]][CJ[ii][0]] == checkCurrentPlayer) {
                for (jj = 1; jj < CI[ii].length; jj++) {
                    if (gameField[CI[ii][0] + CI[ii][jj]][CJ[ii][0] + CJ[ii][jj]] == 1 - checkCurrentPlayer && gameField[CI[ii][0] + 2 * CI[ii][jj]][CJ[ii][0] + 2 * CJ[ii][jj]] == 1 - checkCurrentPlayer) {
                        vv -= 16;
                    } else {
                        if (gameField[CI[ii][0] + CI[ii][jj]][CJ[ii][0] + CJ[ii][jj]] == checkCurrentPlayer &&
                            gameField[CI[ii][0] + 2 * CI[ii][jj]][CJ[ii][0] + 2 * CJ[ii][jj]] == 1 - checkCurrentPlayer &&
                            gameField[CI[ii][0] + 3 * CI[ii][jj]][CJ[ii][0] + 3 * CJ[ii][jj]] == 1 - checkCurrentPlayer &&
                            gameField[CI[ii][0] + 4 * CI[ii][jj]][CJ[ii][0] + 4 * CJ[ii][jj]] == 1 - checkCurrentPlayer) {
                            vv -= 16;
                        }
                    }
                }
            }
            if (gameField[CI[ii][0]][CJ[ii][0]] == 1 - checkCurrentPlayer) {
                for (jj = 1; jj < CI[ii].length; jj++) {
                    if (gameField[CI[ii][0] + CI[ii][jj]][CJ[ii][0] + CJ[ii][jj]] == checkCurrentPlayer &&
                        gameField[CI[ii][0] + 2 * CI[ii][jj]][CJ[ii][0] + 2 * CJ[ii][jj]] == checkCurrentPlayer)
                        vv += 8;
                    else {
                        if (gameField[CI[ii][0] + CI[ii][jj]][CJ[ii][0] + CJ[ii][jj]] == 1 - checkCurrentPlayer &&
                            gameField[CI[ii][0] + 2 * CI[ii][jj]][CJ[ii][0] + 2 * CJ[ii][jj]] == checkCurrentPlayer &&
                            gameField[CI[ii][0] + 3 * CI[ii][jj]][CJ[ii][0] + 3 * CJ[ii][jj]] == checkCurrentPlayer &&
                            gameField[CI[ii][0] + 4 * CI[ii][jj]][CJ[ii][0] + 4 * CJ[ii][jj]] == checkCurrentPlayer) {
                            vv += 8;
                        }   
                    }
                }
            }
        }
        
        return vv;
    }  
}
