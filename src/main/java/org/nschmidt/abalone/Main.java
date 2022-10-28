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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    
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
        LOGGER.info("Wins: {}", wins(state, WHITE));
        
        LOGGER.info("Moves for player 2 : {}", allMoves(state, BLACK).length);
        
        state = INITIAL_FIELD;
        LOGGER.info("Moves for player 2 : {}", allMoves(state, BLACK).length);
        
        Random rnd = new Random(1337L);
 
        int[] wins = new int[3];
        for (int i = 0; i < 10; i++) {
            LOGGER.info("Game {} of 9 (WHITE begins) -> results: {}", i, Arrays.toString(wins));
            wins[playRound(Player.WHITE, rnd)] += 1;
        }
        
        for (int i = 0; i < 10; i++) {
            LOGGER.info("Game {} of 9 (BLACK begins) -> results: {}", i, Arrays.toString(wins));
            wins[playRound(Player.BLACK, rnd)] += 1;
        }
        
        LOGGER.info("Player 1 {} wins.", wins[1]);
        LOGGER.info("Player 2 {} wins.", wins[2]);
        LOGGER.info(" draw    {}", wins[0]);
    }

    private static int playRound(Player currentPlayer, Random rnd) {
        final List<Field> round = new ArrayList<>();
        Field state;
        state = INITIAL_FIELD;
        int moveCount = 0;
        while (moveCount < 100) {
            moveCount += 1;
            double currentScore = score(state, currentPlayer);
            Field[] moves = allMoves(state, currentPlayer);
            Field randomMove = moves[rnd.nextInt(moves.length)];
            
            double maxScore = -Double.MAX_VALUE;
            Field maxMove = randomMove;
            
            if (currentPlayer == WHITE) {
                maxMove = backtrack(state, currentPlayer, 10);
            } else if (currentScore == -Double.MAX_VALUE) {
                for (Field move : moves) {
                    double score = score(move, currentPlayer);
                    if (score > maxScore) {
                        maxScore = score;
                        maxMove = move;
                    }
                }
            } else {
                for (int i = 0; i < 100; i++) {
                    randomMove = moves[rnd.nextInt(moves.length)];
                    double score = score(randomMove, currentPlayer);
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
            LOGGER.info("-----------------------------");
            Field previous = Field.INITIAL_FIELD;
            for (Field field : round) {
                LOGGER.info("{}", field);
                field.printFieldDelta(previous);
                previous = field;
            }
            LOGGER.info("-----------------------------");
        }
        
        return currentPlayer.getNumber();
    }
}
