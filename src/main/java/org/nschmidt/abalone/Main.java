package org.nschmidt.abalone;

import static org.nschmidt.abalone.ai.Backtracker.backtrack;
import static org.nschmidt.abalone.move.MoveDetector.allMoves;
import static org.nschmidt.abalone.playfield.Adjacency.BORDER_INDICES;
import static org.nschmidt.abalone.playfield.Field.INITIAL_FIELD;
import static org.nschmidt.abalone.playfield.Field.populateField;
import static org.nschmidt.abalone.playfield.FieldEvaluator.score;
import static org.nschmidt.abalone.playfield.Player.BLACK;
import static org.nschmidt.abalone.playfield.Player.WHITE;
import static org.nschmidt.abalone.winning.WinningChecker.wins;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.nschmidt.abalone.playfield.Field;
import org.nschmidt.abalone.playfield.Player;

public class Main {
    
    public static void main(String[] args) {
        
        Field state = Field.EMPTY_FIELD;
        state = populateField(state, 0, BLACK);
        state = populateField(state, 1, WHITE);
        state = populateField(state, 2, BLACK);
        state = populateField(state, 36, BLACK);
        
        for (int i : BORDER_INDICES) {
            state = populateField(state, i, BLACK);
        }
        
        state.printField();
        
        state = populateField(state, 5, WHITE);
        state = populateField(state, 6, WHITE);
        System.out.println(wins(state, WHITE));
        
        System.out.println("Moves for player 2 : " + allMoves(state, BLACK).length);
        
        state = INITIAL_FIELD;
        System.out.println("Moves for player 2 : " + allMoves(state, BLACK).length);
        
        Random rnd = new Random(1337L);
 
        int[] wins = new int[3];
        for (int i = 0; i < 10; i++) {
            System.out.println("Game " + i + " of 9 (WHITE begins) -> results: " +  Arrays.toString(wins));
            wins[(int) playRound(Player.WHITE, rnd)] += 1;
        }
        
        for (int i = 0; i < 10; i++) {
            System.out.println("Game " + i + " of 9 (BLACK begins) -> results: " +  Arrays.toString(wins));
            wins[(int) playRound(Player.BLACK, rnd)] += 1;
        }
        
        System.out.println("Player 1 " + wins[1] + " wins.");
        System.out.println("Player 2 " + wins[2] + " wins.");
        System.out.println(" draw    " + wins[0]);
    }

    private static int playRound(Player currentPlayer, Random rnd) {
        final List<Field> round = new ArrayList<>();
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
            round.add(state);
            if (wins(state, currentPlayer)) break;
        }
        
        if (moveCount == 100) {
            return 0;
        }
        
        if (currentPlayer == BLACK) {
            System.out.println("-----------------------------");
            Field previous = Field.INITIAL_FIELD;
            for (Field field : round) {
                System.out.println(field);
                field.printFieldDelta(previous);
                previous = field;
            }
            System.out.println("-----------------------------");
        }
        
        return currentPlayer.getNumber();
    }
}
