package org.nschmidt.abalone;

import static org.nschmidt.abalone.FieldPrinter.printField;
import static org.nschmidt.abalone.Field.populateField;
import static org.nschmidt.abalone.Field.INITIAL_FIELD;
import static org.nschmidt.abalone.Adjacency.BORDER_INDICIES;
import static org.nschmidt.abalone.WinningChecker.wins;

import java.util.Random;

import static org.nschmidt.abalone.SingleMover.moveSingleMarble;
import static org.nschmidt.abalone.DoubleMover.moveTwoMarbles;
import static org.nschmidt.abalone.Attacker.performAttack;
import static org.nschmidt.abalone.MoveDetector.allMoves;

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
        
        Random rnd = new Random();
        while (true) {
            printField(state);
            System.out.println("Player " + currentPlayer + " moves:");
            long[] moves = allMoves(state, currentPlayer);
            long randomMove = moves[rnd.nextInt(moves.length)];
            state = randomMove;
            currentPlayer = 1 + currentPlayer % 2;
            if (wins(state, currentPlayer)) break;
        }
        
        printField(state);
        System.out.println("Player " + currentPlayer + " wins!");
    }
}
