package org.nschmidt.abalone;

import static org.nschmidt.abalone.FieldEvaluator.score;
import static org.nschmidt.abalone.MoveDetector.allMoves;
import static org.nschmidt.abalone.WinningChecker.wins;

import java.util.Arrays;

public enum OptimisationInTwoMovesChecker {
    INSTANCE;
    
    public static Field[] optimisationInTwoMoves(Field state, Player player) {
        final Player opponent = player.switchPlayer();
        final long initialScore = score(state, player);
        Field[] firstMoves = allMoves(state, player);
        return Arrays.asList(firstMoves).parallelStream().map(firstMove -> {
            final Field[] result = new Field[2];
            boolean hasSolution = false;
            int solutionCount = 0;
            
            // Bedingung 1: Gegner darf nicht gewinnen!
            hasSolution = !wins(firstMove, opponent);
            if (!hasSolution) return new Field[0];
            result[0] = firstMove;
            
            // Jetzt haben wir einen Zug von dem wir wissen, dass wir ihn sicher setzen können.
            // Jetzt zieht der Gegner
            Field[] opponentMoves = allMoves(firstMove, opponent);
            
            // Wenn der Gegner keinen Zug machen kann, sind wir wieder an der Reihe 
            if (opponentMoves.length == 0) opponentMoves = new Field[] {firstMove};
            
            solutionCount = 0;
            for (Field opponentMove : opponentMoves) {
                
                Field[] secondMoves = allMoves(opponentMove, player);
                for (Field secondMove : secondMoves) {
                    
                    // Bedingung 2: Gegner darf nicht gewinnen, wenn wir den zweiten Zug gemacht haben!
                    hasSolution = !wins(secondMove, opponent);
                    if (!hasSolution) continue;
                    
                    final long score = score(secondMove, player);
                    hasSolution = score >= initialScore;
                    
                    if (hasSolution) {
                        result[1] = secondMove;
                        break;
                    }
                }
                
                if (hasSolution) {
                    solutionCount++;
                }
            }
            
            // Prüfen, ob wir für jeden Gegnerzug eine schlagfertige Antwort gefunden haben
            if (solutionCount == opponentMoves.length) {
                // Das ist dann eine Lösung auf diesem Weg
                // (unser zweiter Zug kann dann in der Realität anders ausfallen, aber wir werden gewinnen) 
                return result;
            }
            
            return new Field[0];
        })
                .filter(r -> r.length == 2)
                .sorted((m1, m2) -> Long.compare(score(m2[0], player), score(m1[0], player)))
                .filter(r -> WinningInTwoMovesChecker.winsInTwoMoves(r[0], opponent).length != 2)
                .findFirst().orElse(new Field[0]);
    }
}
