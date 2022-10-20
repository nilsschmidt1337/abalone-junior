package org.nschmidt.abalone;

import static org.nschmidt.abalone.MoveDetector.allMoves;
import static org.nschmidt.abalone.WinningChecker.wins;

import java.util.Arrays;
import java.util.Random;

public enum MonteCarloEvaluator {
    INSTANCE;
    
    public static long score(Field state, Player player) {
        final Player opponent = player.switchPlayer();
        if (wins(state, opponent)) return Long.MIN_VALUE;
        
        Field[] firstMoves = allMoves(state, player);
        long score = Arrays.asList(firstMoves).parallelStream().map(firstMove -> {
           final Random rnd = new Random();
           long simulatedScore = 0;
           for (int i = 0; i < 10; i++) {
               Field  currentMove = firstMove;
               Field[] moves;
               for (int j = 0; j < 5; j++) {
                   moves = allMoves(currentMove, opponent);
                   if (moves.length == 0) moves = new Field[]{currentMove};
                   currentMove = moves[rnd.nextInt(moves.length)];
                   if (wins(currentMove, player)) {
                       simulatedScore++;
                       break;
                   }
                   
                   moves = allMoves(currentMove, player);
                   if (moves.length == 0) moves = new Field[]{currentMove};
                   currentMove = moves[rnd.nextInt(moves.length)];
                   if (wins(currentMove, opponent)) {
                       simulatedScore--;
                       break;
                   }
               }
           }
           
           return simulatedScore;
        }).reduce(Long::sum).orElse(0L);
        return score;
    }
}
