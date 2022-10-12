package org.nschmidt.abalone;

import static org.nschmidt.abalone.FieldEvaluator.score;
import static org.nschmidt.abalone.FieldPrinter.printField;
import static org.nschmidt.abalone.FieldPrinter.printFieldDelta;
import static org.nschmidt.abalone.Field.populateField;
import static org.nschmidt.abalone.Field.INITIAL_FIELD;
import static org.nschmidt.abalone.Adjacency.BORDER_INDICIES;
import static org.nschmidt.abalone.WinningChecker.wins;

import java.math.BigInteger;
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
        BigInteger state = BigInteger.ZERO;
        state = populateField(state, 0, BLACK);
        state = populateField(state, 1, WHITE);
        state = populateField(state, 2, BLACK);
        state = populateField(state, 36, BLACK);
        
        for (int i : BORDER_INDICIES) {
            state = populateField(state, i, BLACK);
        }
        
        printField(state);
        
        state = populateField(state, 5, WHITE);
        state = populateField(state, 6, WHITE);
        System.out.println(wins(state, WHITE));
        
        
        state = moveSingleMarble(state, WHITE)[0];
        printField(state);
        
        state = moveTwoMarbles(state, BLACK)[0];
        printField(state);
        
        state = performDoubleAttack(state, BLACK)[0];
        printField(state);
        
        System.out.println("Moves for player 2 : " + allMoves(state, BLACK).length);
        
        state = INITIAL_FIELD;
        System.out.println("Moves for player 2 : " + allMoves(state, BLACK).length);
        
        Player currentPlayer = WHITE;
        
        Random rnd = new Random(1337L);
        BigInteger previousState = state;
        while (true) {
            printFieldDelta(state, previousState);
            previousState = state;
            long currentScore = score(state, currentPlayer);
            System.out.println("Player " + currentPlayer + " moves (Score " + currentScore +  "):");
            BigInteger[] moves = allMoves(state, currentPlayer);
            BigInteger randomMove = moves[rnd.nextInt(moves.length)];
            
            if (currentPlayer == WHITE) {
                state = backtrack(state, currentPlayer, 10);
            } else {
                long maxScore = Long.MIN_VALUE;
                BigInteger maxMove = randomMove;
                
                if (currentScore == Long.MIN_VALUE) {
                    for (BigInteger move : moves) {
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
        
        printFieldDelta(state, previousState);
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
        BigInteger state;
        state = INITIAL_FIELD;
        int moveCount = 0;
        while (moveCount < 100) {
            moveCount += 1;
            long currentScore = score(state, currentPlayer);
            BigInteger[] moves = allMoves(state, currentPlayer);
            BigInteger randomMove = moves[rnd.nextInt(moves.length)];
            
            long maxScore = Long.MIN_VALUE;
            BigInteger maxMove = randomMove;
            
            if (currentPlayer == WHITE) {
                maxMove = backtrack(state, currentPlayer, 10);
            } else if (currentScore == Long.MIN_VALUE) {
                for (BigInteger move : moves) {
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
