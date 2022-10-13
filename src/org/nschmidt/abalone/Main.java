package org.nschmidt.abalone;

import static org.nschmidt.abalone.FieldEvaluator.score;
import static org.nschmidt.abalone.Field.populateField;
import static org.nschmidt.abalone.Field.INITIAL_FIELD;
import static org.nschmidt.abalone.Adjacency.BORDER_INDICIES;
import static org.nschmidt.abalone.WinningChecker.wins;

import java.util.Random;

import static org.nschmidt.abalone.SingleMover.moveSingleMarble;
import static org.nschmidt.abalone.DoubleMover.moveTwoMarbles;
import static org.nschmidt.abalone.DoubleAttacker.performDoubleAttack;
import static org.nschmidt.abalone.MoveDetector.allMoves;
import static org.nschmidt.abalone.Player.BLACK;
import static org.nschmidt.abalone.Player.WHITE;
import static org.nschmidt.abalone.Backtracker.backtrack;

public class Main {
    
    public static void main(String[] args) {
        Field state = Field.EMPTY_FIELD;
        state = populateField(state, 0, BLACK);
        state = populateField(state, 1, WHITE);
        state = populateField(state, 2, BLACK);
        state = populateField(state, 36, BLACK);
        
        for (int i : BORDER_INDICIES) {
            state = populateField(state, i, BLACK);
        }
        
        state.printField();
        
        state = populateField(state, 5, WHITE);
        state = populateField(state, 6, WHITE);
        System.out.println(wins(state, WHITE));
        
        
        state = moveSingleMarble(state, WHITE)[0];
        state.printField();
        
        state = moveTwoMarbles(state, BLACK)[0];
        state.printField();
        
        state = performDoubleAttack(state, BLACK)[0];
        state.printField();
        
        System.out.println("Moves for player 2 : " + allMoves(state, BLACK).length);
        
        state = INITIAL_FIELD;
        System.out.println("Moves for player 2 : " + allMoves(state, BLACK).length);
        
        Player currentPlayer = WHITE;
        
        Random rnd = new Random(1337L);
        Field previousState = state;
        while (true) {
            state.printFieldDelta(previousState);
            previousState = state;
            long currentScore = score(state, currentPlayer);
            System.out.println("Player " + currentPlayer + " moves (Score " + currentScore +  "):");
            Field[] moves = allMoves(state, currentPlayer);
            Field randomMove = moves[rnd.nextInt(moves.length)];
            
            if (currentPlayer == WHITE) {
                state = backtrack(state, currentPlayer, 10);
            } else {
                long maxScore = Long.MIN_VALUE;
                Field maxMove = randomMove;
                
                if (currentScore == Long.MIN_VALUE) {
                    for (Field move : moves) {
                        long score = score(move, currentPlayer);
                        if (score > maxScore) {
                            maxScore = score;
                            maxMove = move;
                        }
                    }
                }
                
                for (int i = 0; i < 100; i++) {
                    randomMove = moves[rnd.nextInt(moves.length)];
                    long score = score(randomMove, currentPlayer);
                    if (score > maxScore) {
                        maxScore = score;
                        maxMove = randomMove;
                    }
                }
                
                state = maxMove;
            }
            
            currentPlayer = currentPlayer.switchPlayer();
            if (wins(state, currentPlayer)) break;
        }
        
        state.printFieldDelta(previousState);
        System.out.println("Player " + currentPlayer + " wins!");
        
        int[] wins = new int[3];
        for (int i = 0; i < 100; i++) {
            wins[(int) playRound(Player.WHITE, rnd)] += 1;
        }
        
        for (int i = 0; i < 100; i++) {
            wins[(int) playRound(Player.BLACK, rnd)] += 1;
        }
        
        System.out.println("Player 1 " + wins[1] + " wins.");
        System.out.println("Player 2 " + wins[2] + " wins.");
        System.out.println(" draw    " + wins[0]);
    }

    private static int playRound(Player currentPlayer, Random rnd) {
        Field state;
        state = INITIAL_FIELD;
        int moveCount = 0;
        while (moveCount < 100) {
            moveCount += 1;
            long currentScore = score(state, currentPlayer);
            Field[] moves = allMoves(state, currentPlayer);
            Field randomMove = moves[rnd.nextInt(moves.length)];
            
            long maxScore = Long.MIN_VALUE;
            Field maxMove = randomMove;
            
            if (currentPlayer == WHITE) {
                maxMove = backtrack(state, currentPlayer, 10);
            } else if (currentScore == Long.MIN_VALUE) {
                for (Field move : moves) {
                    long score = score(move, currentPlayer);
                    if (score > maxScore) {
                        maxScore = score;
                        maxMove = move;
                    }
                }
            } else {
                for (int i = 0; i < 100; i++) {
                    randomMove = moves[rnd.nextInt(moves.length)];
                    long score = score(randomMove, currentPlayer);
                    if (score > maxScore) {
                        maxScore = score;
                        maxMove = randomMove;
                    }
                }
            }
            
            state = maxMove;
            
            currentPlayer = currentPlayer.switchPlayer();
            if (wins(state, currentPlayer)) break;
        }
        
        if (moveCount == 100) {
            return 0;
        }
        
        return currentPlayer.getNumber();
    }
}
