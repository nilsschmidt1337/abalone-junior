package org.nschmidt.abalone;

import static org.nschmidt.abalone.FieldPrinter.printField;
import static org.nschmidt.abalone.Field.populateField;
import static org.nschmidt.abalone.Adjacency.BORDER_INDICIES;
import static org.nschmidt.abalone.WinningChecker.wins;
import static org.nschmidt.abalone.SingleMover.moveSingleMarble;
import static org.nschmidt.abalone.DoubleMover.moveTwoMarbles;
import static org.nschmidt.abalone.Attacker.performAttack;

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
    }
}
