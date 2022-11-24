package org.nschmidt.abalone.rest;

import static org.nschmidt.abalone.ai.Backtracker.backtrack;
import static org.nschmidt.abalone.playfield.Field.FIELD_SIZE;
import static org.nschmidt.abalone.playfield.Field.INITIAL_FIELD;
import static org.nschmidt.abalone.playfield.Field.lookAtField;

import org.nschmidt.abalone.playfield.Field;
import org.nschmidt.abalone.playfield.Player;
import org.springframework.stereotype.Service;

@Service
public class AbaloneService {

    public Player[] initialField() {
        return toArray(INITIAL_FIELD);
    }

    public Player[] answer(Player player, Player[] move) {
        Field answer = backtrack(Field.of(move), player);
        return toArray(answer);
    }

    private Player[] toArray(Field answer) {
        final Player[] result = new Player[FIELD_SIZE];
        for (int i = 0; i < FIELD_SIZE; i++) {
            result[i] = lookAtField(answer, i);
        }
        
        return result;
    }
}
