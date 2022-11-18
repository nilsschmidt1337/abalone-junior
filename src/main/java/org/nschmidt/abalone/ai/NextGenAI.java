package org.nschmidt.abalone.ai;

import java.util.Arrays;
import java.util.Random;

import org.nschmidt.abalone.move.MoveDetector;
import org.nschmidt.abalone.playfield.Field;
import org.nschmidt.abalone.playfield.Player;

public enum NextGenAI {
    INSTANCE;
    
    private static final Random RND = new Random(1337L);
    
    private static int size = 9;
    //Create a grid with the grade values for best move
    private static int[][] fieldValue = new int[size][];
    
    private static int[][] gameField = new int[size][size];
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
    
    for (int jj = 0; jj < size; jj++) {
        for (int ii = 0; ii < size; ii++) {
            if (ii + jj < 4 || ii + jj > 12) {
                gameField[ii][jj] = -2;
            } else {
                gameField[ii][jj] = -1;
            }
        }   
    }
    }
    
    private static void populateGameField(Field state) {
        gameField[0][4] = Field.lookAtField(state, 0).getNumber() - 1;
        gameField[0][5] = Field.lookAtField(state, 1).getNumber() - 1;
        gameField[0][6] = Field.lookAtField(state, 2).getNumber() - 1;
        gameField[0][7] = Field.lookAtField(state, 3).getNumber() - 1;
        gameField[0][8] = Field.lookAtField(state, 4).getNumber() - 1;
        
        gameField[1][3] = Field.lookAtField(state, 5).getNumber() - 1;
        gameField[1][4] = Field.lookAtField(state, 6).getNumber() - 1;
        gameField[1][5] = Field.lookAtField(state, 7).getNumber() - 1;
        gameField[1][6] = Field.lookAtField(state, 8).getNumber() - 1;
        gameField[1][7] = Field.lookAtField(state, 9).getNumber() - 1;
        gameField[1][8] = Field.lookAtField(state, 10).getNumber() - 1;
        
        gameField[2][2] = Field.lookAtField(state, 11).getNumber() - 1;
        gameField[2][3] = Field.lookAtField(state, 12).getNumber() - 1;
        gameField[2][4] = Field.lookAtField(state, 13).getNumber() - 1;
        gameField[2][5] = Field.lookAtField(state, 14).getNumber() - 1;
        gameField[2][6] = Field.lookAtField(state, 15).getNumber() - 1;
        gameField[2][7] = Field.lookAtField(state, 16).getNumber() - 1;
        gameField[2][8] = Field.lookAtField(state, 17).getNumber() - 1;
        
        gameField[3][1] = Field.lookAtField(state, 18).getNumber() - 1;
        gameField[3][2] = Field.lookAtField(state, 19).getNumber() - 1;
        gameField[3][3] = Field.lookAtField(state, 20).getNumber() - 1;
        gameField[3][4] = Field.lookAtField(state, 21).getNumber() - 1;
        gameField[3][5] = Field.lookAtField(state, 22).getNumber() - 1;
        gameField[3][6] = Field.lookAtField(state, 23).getNumber() - 1;
        gameField[3][7] = Field.lookAtField(state, 24).getNumber() - 1;
        gameField[3][8] = Field.lookAtField(state, 25).getNumber() - 1;
        
        gameField[4][0] = Field.lookAtField(state, 26).getNumber() - 1;
        gameField[4][1] = Field.lookAtField(state, 27).getNumber() - 1;
        gameField[4][2] = Field.lookAtField(state, 28).getNumber() - 1;
        gameField[4][3] = Field.lookAtField(state, 29).getNumber() - 1;
        gameField[4][4] = Field.lookAtField(state, 30).getNumber() - 1;
        gameField[4][5] = Field.lookAtField(state, 31).getNumber() - 1;
        gameField[4][6] = Field.lookAtField(state, 32).getNumber() - 1;
        gameField[4][7] = Field.lookAtField(state, 33).getNumber() - 1;
        gameField[4][8] = Field.lookAtField(state, 34).getNumber() - 1;
        
        gameField[5][0] = Field.lookAtField(state, 35).getNumber() - 1;
        gameField[5][1] = Field.lookAtField(state, 36).getNumber() - 1;
        gameField[5][2] = Field.lookAtField(state, 37).getNumber() - 1;
        gameField[5][3] = Field.lookAtField(state, 38).getNumber() - 1;
        gameField[5][4] = Field.lookAtField(state, 39).getNumber() - 1;
        gameField[5][5] = Field.lookAtField(state, 40).getNumber() - 1;
        gameField[5][6] = Field.lookAtField(state, 41).getNumber() - 1;
        gameField[5][7] = Field.lookAtField(state, 42).getNumber() - 1;
        
        gameField[6][0] = Field.lookAtField(state, 43).getNumber() - 1;
        gameField[6][1] = Field.lookAtField(state, 44).getNumber() - 1;
        gameField[6][2] = Field.lookAtField(state, 45).getNumber() - 1;
        gameField[6][3] = Field.lookAtField(state, 46).getNumber() - 1;
        gameField[6][4] = Field.lookAtField(state, 47).getNumber() - 1;
        gameField[6][5] = Field.lookAtField(state, 48).getNumber() - 1;
        gameField[6][6] = Field.lookAtField(state, 49).getNumber() - 1;
        
        gameField[7][0] = Field.lookAtField(state, 50).getNumber() - 1;
        gameField[7][1] = Field.lookAtField(state, 51).getNumber() - 1;
        gameField[7][2] = Field.lookAtField(state, 52).getNumber() - 1;
        gameField[7][3] = Field.lookAtField(state, 53).getNumber() - 1;
        gameField[7][4] = Field.lookAtField(state, 54).getNumber() - 1;
        gameField[7][5] = Field.lookAtField(state, 55).getNumber() - 1;
        
        gameField[8][0] = Field.lookAtField(state, 56).getNumber() - 1;
        gameField[8][1] = Field.lookAtField(state, 57).getNumber() - 1;
        gameField[8][2] = Field.lookAtField(state, 58).getNumber() - 1;
        gameField[8][3] = Field.lookAtField(state, 59).getNumber() - 1;
        gameField[8][4] = Field.lookAtField(state, 60).getNumber() - 1;
    }
    
    //Game grid mapping to find best move
    private static final int[][] CI = new int[24][];
    private static final int[][] CJ = new int[24][];
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
        populateGameField(state);
        Field[] moves = MoveDetector.allMoves(state, currentPlayer);
        Arrays.sort(moves, (m1, m2) -> Integer.compare(evalGameField(m2, currentPlayer), evalGameField(m1, currentPlayer)));
        
        if (moves.length == 0) return state;
        if (moves.length == 1) return moves[0];
        
        if (RND.nextBoolean() && evalGameField(moves[0], currentPlayer) == evalGameField(moves[1], currentPlayer)) {
            return moves[1];
        }
        
        return  moves[0];
    }

    public static int evalGameField(Field state, Player currentPlayer) {
        populateGameField(state);
        int checkCurrentPlayer = currentPlayer.getNumber() - 1;
        int ii = 0;
        int jj = 0;
        int vv = 0;
        for (jj = 0; jj < size; jj++) {
            for (ii = 0; ii < size; ii++) {
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
