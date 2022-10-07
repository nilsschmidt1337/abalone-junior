package org.nschmidt.abalone;

import static org.nschmidt.abalone.FieldEvaluator.score;
import static org.nschmidt.abalone.FieldPrinter.printField;
import static org.nschmidt.abalone.FieldPrinter.printFieldDelta;
import static org.nschmidt.abalone.Field.populateField;
import static org.nschmidt.abalone.Field.INITIAL_FIELD;
import static org.nschmidt.abalone.Adjacency.BORDER_INDICIES;
import static org.nschmidt.abalone.WinningChecker.wins;

import java.util.Random;

import static org.nschmidt.abalone.SingleMover.moveSingleMarble;
import static org.nschmidt.abalone.DoubleMover.moveTwoMarbles;
import static org.nschmidt.abalone.Attacker.performAttack;
import static org.nschmidt.abalone.MoveDetector.allMoves;
import static org.nschmidt.abalone.Backtracker.backtrack;

public class Main {
    
    public static void main(String[] args) {
        long state = 0L;
        state = populateField(state, 0, 2);
        state = populateField(state, 1, 1);
        state = populateField(state, 2, 2);
        state = populateField(state, 36, 2);
        
        for (int i : BORDER_INDICIES) {
            state = populateField(state, i, 2);
        }
        
        printField(state);
        
        state = populateField(state, 5, 1);
        state = populateField(state, 6, 1);
        System.out.println(wins(state, 1));
        
        
        state = moveSingleMarble(state, 1)[0];
        printField(state);
        
        state = moveTwoMarbles(state, 2)[0];
        printField(state);
        
        state = performAttack(state, 2)[0];
        printField(state);
        
        System.out.println("Moves for player 2 : " + allMoves(state, 2).length);
        
        state = INITIAL_FIELD;
        System.out.println("Moves for player 2 : " + allMoves(state, 2).length);
        
        long currentPlayer = 1;
        
        Random rnd = new Random(1337L);
        long previousState = state;
        while (true) {
            printFieldDelta(state, previousState);
            previousState = state;
            long currentScore = score(state, currentPlayer);
            System.out.println("Player " + currentPlayer + " moves (Score " + currentScore +  "):");
            long[] moves = allMoves(state, currentPlayer);
            long randomMove = moves[rnd.nextInt(moves.length)];
            
            if (currentPlayer == 1) {
                state = backtrack(state, currentPlayer, 10);
            } else {
                long maxScore = Long.MIN_VALUE;
                long maxMove = randomMove;
                
                if (currentScore == Long.MIN_VALUE) {
                    for (long move : moves) {
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
            
            currentPlayer = 1 + currentPlayer % 2;
            if (wins(state, currentPlayer)) break;
        }
        
        printFieldDelta(state, previousState);
        System.out.println("Player " + currentPlayer + " wins!");
        
        int[] wins = new int[3];
        for (int i = 0; i < 100; i++) {
            wins[(int) playRound(1L, rnd)] += 1;
        }
        
        for (int i = 0; i < 100; i++) {
            wins[(int) playRound(2L, rnd)] += 1;
        }
        
        System.out.println("Player 1 " + wins[1] + " wins.");
        System.out.println("Player 2 " + wins[2] + " wins.");
        System.out.println(" draw    " + wins[0]);
    }

    private static long playRound(long currentPlayer, Random rnd) {
        long state;
        state = INITIAL_FIELD;
        int moveCount = 0;
        while (moveCount < 100) {
            moveCount += 1;
            long currentScore = score(state, currentPlayer);
            long[] moves = allMoves(state, currentPlayer);
            long randomMove = moves[rnd.nextInt(moves.length)];
            
            long maxScore = Long.MIN_VALUE;
            long maxMove = randomMove;
            
            if (currentPlayer == 1) {
                maxMove = backtrack(state, currentPlayer, 10);
            } else if (currentScore == Long.MIN_VALUE) {
                for (long move : moves) {
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
            
            currentPlayer = 1 + currentPlayer % 2;
            if (wins(state, currentPlayer)) break;
        }
        
        if (moveCount == 100) {
            return 0;
        }
        
        return currentPlayer;
    }
}
